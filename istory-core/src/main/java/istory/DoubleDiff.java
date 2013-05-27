package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for double.
 *
 * @author Cedric Chantepie
 */
public class DoubleDiff implements Diff<Double>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final double oldValue;

    /**
     * New value
     */
    private final double newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public DoubleDiff(final double oldValue, final double newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Double> Double patch(final V orig) throws DiffException {
	if (orig.doubleValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Double> Double revert(final V dest) throws DiffException {
	if (dest.doubleValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;	
    } // end of revert
} // end of class DoubleDiff
