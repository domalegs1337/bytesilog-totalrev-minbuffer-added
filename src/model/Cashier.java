package model;

// represents a cashier user with limited system access
public class Cashier extends User {

	public static final String ROLE = "CASHIER"; // role identifier constant

    /* Cashier constructor:
     * - passes credentials and role to superclass */
    public Cashier(String username, String password) {
        super(username, password, Cashier.ROLE);
    }

}