package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for long.
 *
 * @author Cedric Chantepie ()
 */
public class LongDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private long oldValue = -1;

    /**
     * New value
     */
    private long newValue = -1;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public LongDiff(long oldValue, 
		    long newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Long o = (Long) orig;
	long op = o.longValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Long(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Long d = (Long) dest;
	long dp = d.longValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Long(this.oldValue);	
    } // end of revert

} // end of class LongDiff
