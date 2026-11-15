package model;

// registration request of a new student
public class AccessRequest implements Storable {

    private String universityId;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String status;     // Pending, Approved or Rejected

    public AccessRequest(String universityId, String fullName, String email, String phone, String password) {
        this.universityId = universityId;
        this.fullName = fullName;
        this.email=email;
        this.phone = phone;
        this.password=password;
        status = "Pending";
    }

    public String getUniversityId() { return universityId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public String getStatus() { return status; }

    public boolean isPending() {
        return status.equals("Pending");
    }

    /* only a pending request can change, so it cannot be approved twice */
    public boolean approve() {
        if (!isPending())
            return false;
        status = "Approved";
        return true;
    }

    public boolean reject() {
        if(status.equals("Pending")) {
            status="Rejected";
            return true;
        }
        return false;
    }

    public String toLine() {
        return universityId + ";" + fullName + ";" + email + ";" + phone + ";" + password + ";" + status;
    }

    public static AccessRequest fromLine(String line) {
        String[] p = line.split(";");
        if (p.length != 6)
            return null;
        if (!p[5].equals("Pending") && !p[5].equals("Approved") && !p[5].equals("Rejected"))
            return null;
        AccessRequest r = new AccessRequest(p[0], p[1], p[2], p[3], p[4]);
        r.status = p[5];
        return r;
    }
}