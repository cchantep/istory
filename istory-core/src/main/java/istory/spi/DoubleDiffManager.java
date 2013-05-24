package istory.spi;

import istory.Diff;

/**
 * Manager diff between doubles.
 *
 * @author Cedric Chantepie ()
 */
public class DoubleDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public DoubleDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Double i1 = (Double) o1;
	Double i2 = (Double) o2;

	return new DoubleDiff(i1.doubleValue(), i2.doubleValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Double.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
