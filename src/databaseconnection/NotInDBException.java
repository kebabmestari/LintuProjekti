/**
 * Exception which is thrown when a bird or fish is not found from DB
 * @author Samuel
 */
public class NotInDBException extends Exception {

    private String queriedName = "unnamed"; 
    private String type = "unset";
    
    public NotInDBException() {
        super("The species was not found from the database. Check your spelling.");
    }

    /**
     * Constructs an instance of <code>NotInDBException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotInDBException(String msg, String type) {
        super(msg);
        queriedName = msg;
        this.type = type;
    }
    
    public String getName(){
        return queriedName;
    }
    
    public String getType(){
        return type;
    }
}
