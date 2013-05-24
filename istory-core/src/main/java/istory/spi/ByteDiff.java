package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for byte.
 *
 * @author Cedric Chantepie ()
 */
public class ByteDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private byte oldValue = -1;

    /**
     * New value
     */
    private byte newValue = -1;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public ByteDiff(byte oldValue, 
		    byte newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Byte o = (Byte) orig;
	byte op = o.byteValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Byte(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Byte d = (Byte) dest;
	byte dp = d.byteValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Byte(this.oldValue);	
    } // end of revert

} // end of class ByteDiff
