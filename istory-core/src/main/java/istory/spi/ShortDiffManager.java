package istory.spi;

import istory.Diff;

/**
 * Manager diff between shorts.
 *
 * @author Cedric Chantepie ()
 */
public class ShortDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public ShortDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Short i1 = (Short) o1;
	Short i2 = (Short) o2;

	return new ShortDiff(i1.shortValue(), i2.shortValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Short.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
