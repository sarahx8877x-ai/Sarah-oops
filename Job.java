package model;

/* one job.
   Queued can go to Running or Cancelled
   Running can go to Completed or Cancelled */
public class Job implements Storable {

    private int jobNumber;
    private String jobId;
    private String studentId;
    private String title;
    private String applicationName;
    private String description;
    private String taskSize;
    private int workloadUnits;
    private int estimatedDuration;
    private SimTime submissionTime;
    private String status;
    private int assignedGpuId;     // 0 = no gpu yet
    private SimTime startTime;
    private SimTime finishTime;

    // cancel info
    private SimTime cancelTime;
    private String cancelledBy;
    private String cancelReason;

    private boolean settled;

    public Job(int number, String studentId, String title, String app, String desc, String size, SimTime submitted) {
        jobNumber = number;
        jobId = "J" + number;
        this.studentId = studentId;
        this.title=title;
        applicationName = app;
        description = desc;
        taskSize=size;
        workloadUnits = TaskSize.unitsFor(size);
        estimatedDuration = workloadUnits;
        submissionTime = submitted;
        status = "Queued";
        assignedGpuId=0;
        settled = false;
    }

    public boolean start(int gpuId, SimTime now) {
        if (!status.equals("Queued"))
            return false;
        status = "Running";
        assignedGpuId = gpuId;
        startTime=now;
        return true;
    }

    public boolean complete(SimTime now) {
        if (status.equals("Running")) {
            status="Completed";
            finishTime = now;
            return true;
        }
        return false;
    }

    // works only for queued or running jobs
    public boolean cancel(String by, SimTime now, String why) {
        if (status.equals("Completed") || status.equals("Cancelled"))
            return false;

        if (status.equals("Running"))
            finishTime = now;
        status = "Cancelled";
        cancelTime=now;
        cancelledBy = by;
        cancelReason = why;
        return true;
    }

    /* so the allowance is settled one time only */
    public boolean markSettled() {
        if (settled == true)
            return false;
        settled = true;
        return true;
    }

    // minutes on a gpu. waiting does not count
    public int getRuntime(SimTime now) {
        if (startTime == null)
            return 0;
        if (finishTime != null)
            return startTime.minutesUntil(finishTime);
        else
            return startTime.minutesUntil(now);
    }

    public int getRemaining(SimTime now) {
        return estimatedDuration - getRuntime(now);
    }

    public int getQueueWait() {
        if (startTime == null) return 0;
        int w = submissionTime.minutesUntil(startTime);
        return w;
    }

    public int getCharged() {
        if (settled && startTime != null && finishTime != null)
            return startTime.minutesUntil(finishTime);
        return 0;
    }

    public boolean isQueued() { return status.equals("Queued"); }
    public boolean isRunning() { return status.equals("Running"); }

    public int getJobNumber() { return jobNumber; }
    public String getJobId() { return jobId; }
    public String getStudentId() { return studentId; }
    public String getTitle() { return title; }
    public String getApplicationName() { return applicationName; }
    public String getDescription() { return description; }
    public String getTaskSize() { return taskSize; }
    public int getWorkloadUnits() { return workloadUnits; }
    public int getEstimatedDuration() { return estimatedDuration; }
    public SimTime getSubmissionTime() { return submissionTime; }
    public String getStatus() { return status; }
    public int getAssignedGpuId() { return assignedGpuId; }
    public SimTime getStartTime() { return startTime; }
    public SimTime getFinishTime() { return finishTime; }
    public SimTime getCancelTime() { return cancelTime; }
    public String getCancelledBy() { return cancelledBy; }
    public String getCancelReason() { return cancelReason; }

    /* one line for the jobs file. missing values are saved as the word null */
    public String toLine() {
        String line = jobNumber + ";" + studentId + ";" + title + ";" + applicationName + ";" + description + ";" + taskSize;
        line = line + ";" + submissionTime + ";" + status + ";" + assignedGpuId;
        line = line + ";" + startTime + ";" + finishTime + ";" + cancelTime;
        line = line + ";" + cancelledBy + ";" + cancelReason + ";" + settled;
        return line;
    }

    // makes a job from a saved line. null if the line is wrong
    public static Job fromLine(String line) {
        String[] p = line.split(";");
        if (p.length != 15)
            return null;

        Job j = new Job(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4], p[5], SimTime.fromText(p[6]));
        if (j.workloadUnits == 0 || j.submissionTime == null)
            return null;

        j.status = p[7];
        if (!j.status.equals("Queued") && !j.status.equals("Running")
                && !j.status.equals("Completed") && !j.status.equals("Cancelled"))
            return null;

        j.assignedGpuId = Integer.parseInt(p[8]);
        j.startTime = SimTime.fromText(p[9]);
        j.finishTime=SimTime.fromText(p[10]);
        j.cancelTime = SimTime.fromText(p[11]);
        if (!p[12].equals("null"))
            j.cancelledBy = p[12];
        if (!p[13].equals("null"))
            j.cancelReason = p[13];
        j.settled = p[14].equals("true");

        // a running job must have a GPU and a start time
        if (j.status.equals("Running") && (j.assignedGpuId < 1 || j.assignedGpuId > 4 || j.startTime == null))
            return null;
        return j;
    }
}