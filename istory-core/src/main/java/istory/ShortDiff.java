package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for short.
 *
 * @author Cedric Chantepie
 */
public class ShortDiff implements Diff<Short>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final short oldValue;

    /**
     * New value
     */
    private final short newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public ShortDiff(final short oldValue, 
		     final short newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Short> Short patch(final V orig) throws DiffException {
	if (orig.shortValue() != this.oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Short> Short revert(final V dest) throws DiffException {
	if (dest.shortValue() != this.newValue) {
	    throw new DiffException();
	} // end of if

	return new Short(this.oldValue);	
    } // end of revert
} // end of class ShortDiff
