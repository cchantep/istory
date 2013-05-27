package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for integer.
 *
 * @author Cedric Chantepie
 */
public class IntegerDiff implements Diff<Integer>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final int oldValue;

    /**
     * New value
     */
    private final int newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public IntegerDiff(final int oldValue, final int newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Integer> Integer patch(final V orig) 
        throws DiffException {

	if (orig.intValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Integer> Integer revert(final V dest) 
        throws DiffException {

	if (dest.intValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class IntegerDiff
