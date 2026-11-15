package model;

// extra minutes given by the admin, with the reason
public class TimeGrant implements Storable {

    private String studentId;
    private int minutes;
    private String reason;
    private String grantedBy;
    private SimTime time;

    public TimeGrant(String studentId, int minutes, String reason, String grantedBy, SimTime time) {
        this.studentId = studentId;
        this.minutes=minutes;
        this.reason = reason;
        this.grantedBy = grantedBy;
        this.time=time;
    }

    public String toLine() {
        return studentId + ";" + minutes + ";" + reason + ";" + grantedBy + ";" + time;
    }

    public static TimeGrant fromLine(String line) {
        String[] p = line.split(";");
        if (p.length != 5)
            return null;
        TimeGrant g = new TimeGrant(p[0], Integer.parseInt(p[1]), p[2], p[3], SimTime.fromText(p[4]));
        return g;
    }
}