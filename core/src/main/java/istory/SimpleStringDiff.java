package istory;

import java.io.Serializable;

/**
 * Diff for String.
 *
 * @author Cedric Chantepie
 */
public class SimpleStringDiff implements Diff<String>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final String oldValue;

    /**
     * New value
     */
    private final String newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public SimpleStringDiff(final String oldValue, final String newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends String> String patch(final V orig) throws DiffException {
	if ((this.oldValue != null || orig != null) && 
	    (this.oldValue == null || !this.oldValue.equals(orig))) {

	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends String> String revert(final V dest) throws DiffException {
	if ((this.newValue != null || dest != null) && 
	    (this.newValue == null || !this.newValue.equals(dest))) {

	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class SimpleStringDiff
