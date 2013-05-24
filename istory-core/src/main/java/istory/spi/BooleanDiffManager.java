package istory.spi;

import istory.Diff;

/**
 * Manager diff between booleans.
 *
 * @author Cedric Chantepie ()
 */
public class BooleanDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public BooleanDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Boolean i1 = (Boolean) o1;
	Boolean i2 = (Boolean) o2;

	return new BooleanDiff(i1.booleanValue(), i2.booleanValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Boolean.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
