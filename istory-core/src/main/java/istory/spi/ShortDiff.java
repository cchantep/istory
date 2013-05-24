package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for short.
 *
 * @author Cedric Chantepie ()
 */
public class ShortDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private short oldValue = -1;

    /**
     * New value
     */
    private short newValue = -1;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public ShortDiff(short oldValue, 
		     short newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Short o = (Short) orig;
	short op = o.shortValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Short(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Short d = (Short) dest;
	short dp = d.shortValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Short(this.oldValue);	
    } // end of revert

} // end of class ShortDiff
