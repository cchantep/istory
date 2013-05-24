package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for integer.
 *
 * @author Cedric Chantepie ()
 */
public class IntegerDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private int oldValue = -1;

    /**
     * New value
     */
    private int newValue = -1;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public IntegerDiff(int oldValue, 
		       int newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Integer o = (Integer) orig;
	int op = o.intValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Integer(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Integer d = (Integer) dest;
	int dp = d.intValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Integer(this.oldValue);	
    } // end of revert

} // end of class IntegerDiff
