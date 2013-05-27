package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for long.
 *
 * @author Cedric Chantepie
 */
public class LongDiff implements Diff<Long>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final long oldValue;

    /**
     * New value
     */
    private final long newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public LongDiff(final long oldValue, final long newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Long> Long patch(V orig) throws DiffException {
	if (orig.longValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Long> Long revert(V dest) throws DiffException {
	if (dest.longValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class LongDiff
