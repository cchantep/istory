package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for boolean.
 *
 * @author Cedric Chantepie ()
 */
public class BooleanDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private boolean oldValue;

    /**
     * New value
     */
    private boolean newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public BooleanDiff(boolean oldValue, 
		       boolean newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Boolean o = (Boolean) orig;
	boolean op = o.booleanValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Boolean(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Boolean d = (Boolean) dest;
	boolean dp = d.booleanValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Boolean(this.oldValue);	
    } // end of revert

} // end of class BooleanDiff
