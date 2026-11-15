package model;

// parent of Student and Administrator
public abstract class User {

    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password=password;
        this.fullName = fullName;
    }

    public String getUsername() { return username; }
    public String getFullName() {
        return fullName;
    }
    public String getPassword() { return password; }

    public boolean checkPassword(String typed) {
        if (password.equals(typed))
            return true;
        else
            return false;
    }

    public abstract String getRole();
}