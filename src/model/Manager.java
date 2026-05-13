package model;

// represents a manager user with full system access
public class Manager extends User {

    public static final String ROLE = "MANAGER"; // role identifier constant

    /* Manager constructor:
     * - passes credentials and role to superclass */
    public Manager(String username, String password) {
        super(username, password, Manager.ROLE);
    }

}