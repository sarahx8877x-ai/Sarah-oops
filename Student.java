package model;

public class Student extends User implements Storable {

    private String email;
    private String phone;
    private TimeAllowance allowance;

    // university id is the username
    public Student(String id, String password, String name, String email, String phone, int minutes) {
        super(id, password, name);
        this.email = email;
        this.phone=phone;
        allowance = new TimeAllowance(minutes);
    }

    public String getUniversityId() {
        return getUsername();
    }

    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public TimeAllowance getAllowance() { return allowance; }

    public String getRole() {
        return "Student";
    }

    // id;password;name;email;phone;allocated;used;reserved
    public String toLine() {
        String line = getUsername() + ";" + getPassword() + ";" + getFullName() + ";" + email + ";" + phone;
        line = line + ";" + allowance.getAllocated() + ";" + allowance.getUsed() + ";" + allowance.getReserved();
        return line;
    }

    /* makes a student from a saved line. null if the line is wrong */
    public static Student fromLine(String line) {
        String[] p = line.split(";");
        if (p.length != 8)
            return null;
        int allocated = Integer.parseInt(p[5]);
        int used = Integer.parseInt(p[6]);
        int reserved=Integer.parseInt(p[7]);
        if (allocated < 0 || used < 0 || reserved < 0 || allocated - used - reserved < 0)
            return null;
        Student s = new Student(p[0], p[1], p[2], p[3], p[4], allocated);
        s.allowance = new TimeAllowance(allocated, used, reserved);
        return s;
    }
}