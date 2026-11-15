package model;

public class Administrator extends User {

    /* fixed account from the project description */
    public Administrator() {
        super("admin", "gpu2026", "Administrator");
    }

    public String getRole() {
        return "Administrator";
    }
}