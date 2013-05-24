package istory.spi;

import java.io.Serializable;

import istory.DiffException;
import istory.Diff;

/**
 * Diff for float.
 *
 * @author Cedric Chantepie ()
 */
public class FloatDiff implements Diff, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private float oldValue = -1f;

    /**
     * New value
     */
    private float newValue = -1f;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public FloatDiff(float oldValue, 
		     float newValue) {

	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	Float o = (Float) orig;
	float op = o.floatValue();
	
	if (op != oldValue) {
	    throw new DiffException();
	} // end of if

	return new Float(this.newValue);
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	Float d = (Float) dest;
	float dp = d.floatValue();
	
	if (dp != newValue) {
	    throw new DiffException();
	} // end of if

	return new Float(this.oldValue);	
    } // end of revert

} // end of class FloatDiff
