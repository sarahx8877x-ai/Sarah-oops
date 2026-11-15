package model;

public class GPU {

    private int id;
    private Job currentJob;    // null = free

    public GPU(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public String getName() { return "GPU" + id; }
    public Job getCurrentJob() { return currentJob; }

    public boolean isFree() {
        if (currentJob == null)
            return true;
        return false;
    }

    public void assign(Job job) {
        currentJob = job;
    }

    public void release() {
        currentJob=null;
    }

    // minutes left for the running job
    public int getRemaining(SimTime now) {
        if (currentJob == null)
            return 0;
        return currentJob.getRemaining(now);
    }
}