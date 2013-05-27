package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for boolean.
 *
 * @author Cedric Chantepie
 */
public class BooleanDiff implements Diff<Boolean>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final boolean oldValue;

    /**
     * New value
     */
    private final boolean newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public BooleanDiff(final boolean oldValue, final boolean newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Boolean> Boolean patch(final V orig) 
        throws DiffException {

	if (orig.booleanValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Boolean> Boolean revert(final V dest) 
        throws DiffException {

	if (dest.booleanValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;	
    } // end of revert
} // end of class BooleanDiff
