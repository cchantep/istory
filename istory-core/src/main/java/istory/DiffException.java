package istory;

/**
 * Exception that occurs when patching or reverting objects.
 *
 * @author Cedric Chantepie ()
 */
public class DiffException extends Exception {
    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public DiffException() {
	super();
    } // end of <init>

    /**
     * Message constructor.
     *
     * @param message Exception message
     */
    public DiffException(String message) {
	super(message);
    } // end of <init>
} // end of class DiffException
