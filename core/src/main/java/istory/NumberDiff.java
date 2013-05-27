package istory;

import java.io.Serializable;

/**
 * Diff for integer.
 *
 * @author Cedric Chantepie
 */
public class NumberDiff<N extends Number> implements Diff<N>, Serializable {
    // --- Properties ---

    /**
     * Old value
     */
    private final N oldValue;

    /**
     * New value
     */
    private final N newValue;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param oldValue 
     * @param newValue
     */
    public NumberDiff(final N oldValue, final N newValue) {
	this.oldValue = oldValue;
	this.newValue = newValue;
    } // end of if
    
    // ---

    /**
     * {@inheritDoc}
     */
    public <V extends N> N patch(final V orig) throws DiffException {
        if (!oldValue.equals(orig)) {
	    throw new DiffException();
	} // end of if

	return this.newValue;
    } // end of catch

    /**
     * {@inheritDoc}
     */
    public <V extends N> N revert(final V dest) throws DiffException {
	if (!dest.equals(newValue)) {
	    throw new DiffException();
	} // end of if

	return this.oldValue;
    } // end of revert
} // end of class NumberDiff
