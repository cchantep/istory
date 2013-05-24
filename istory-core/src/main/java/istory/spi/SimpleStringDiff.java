package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for String.
 *
 * @author Cedric Chantepie ()
 */
public class SimpleStringDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private String oldValue = null;

    /**
     * New value
     */
    private String newValue = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public SimpleStringDiff(String oldValue, 
			    String newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	String o = (String) orig;
	
	if ((this.oldValue != null || o != null) && 
	    (this.oldValue == null || !this.oldValue.equals(o))) {

	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	String d = (String) dest;

	if ((this.newValue != null || d != null) && 
	    (this.newValue == null || !this.newValue.equals(d))) {

	    throw new DiffException();
	} // end of if

	return new String(this.oldValue);	
    } // end of revert

} // end of class SimpleStringDiff
