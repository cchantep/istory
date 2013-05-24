package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for double.
 *
 * @author Cedric Chantepie ()
 */
public class DoubleDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private double oldValue = -1;

    /**
     * New value
     */
    private double newValue = -1;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public DoubleDiff(double oldValue, 
		      double newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Double o = (Double) orig;
	double op = o.doubleValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Double(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Double d = (Double) dest;
	double dp = d.doubleValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Double(this.oldValue);	
    } // end of revert

} // end of class DoubleDiff
