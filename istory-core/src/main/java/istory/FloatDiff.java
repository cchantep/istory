package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for float.
 *
 * @author Cedric Chantepie
 */
public class FloatDiff implements Diff<Float>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final float oldValue;

    /**
     * New value
     */
    private final float newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public FloatDiff(final float oldValue, final float newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Float> Float patch(final V orig) throws DiffException {
	if (orig.floatValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Float> Float revert(final V dest) throws DiffException {
	if (dest.floatValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class FloatDiff
