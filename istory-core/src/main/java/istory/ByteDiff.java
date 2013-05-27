package istory;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for byte.
 *
 * @author Cedric Chantepie
 */
public class ByteDiff implements Diff<Byte>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final byte oldValue;

    /**
     * New value
     */
    private final byte newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public ByteDiff(final byte oldValue, final byte newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends Byte> Byte patch(final V orig) throws DiffException {
	if (orig.byteValue() != oldValue) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends Byte> Byte revert(final V dest) throws DiffException {
	if (dest.byteValue() != newValue) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class ByteDiff
