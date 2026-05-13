package model;

// represents a single cash transaction record
public class Transaction {

    public static final String CASH_IN = "CASH_IN";   // cash added to the register
    public static final String CASH_OUT = "CASH_OUT"; // cash removed from the register
    public static final String SALE = "SALE";         // cash received from a sale

    private String transactionId;
    private String type;        // CASH_IN, CASH_OUT, or SALE
    private double amount;
    private String description;
    private String timestamp;

    /* Transaction constructor:
     * - initializes all fields for a cash record */
    public Transaction(String transactionId, String type,
                       double amount, String description, String timestamp) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getType() {
        return this.type;
    }

    public double getAmount() {
        return this.amount;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

}