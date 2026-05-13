package model;

// abstract superclass for all user types in the system
public class User {

    private String username; // login username
    private String password; // login password
    private String role;     // MANAGER or CASHIER

    /* User constructor:
     * - initializes username, password, and role */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /* getUsername method:
     * - returns the username of this user */
    public String getUsername() {
        return this.username;
    }

    /* getPassword method:
     * - returns the password of this user */
    public String getPassword() {
        return this.password;
    }

    /* getRole method:
     * - returns the role of this user */
    public String getRole() {
        return this.role;
    }

    /* checkPassword method:
     * - returns true if the given input matches this user's password */
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

}