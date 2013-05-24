package istory.spi;

import istory.Diff;

/**
 * Manager diff between integers.
 *
 * @author Cedric Chantepie ()
 */
public class IntegerDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public IntegerDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Integer i1 = (Integer) o1;
	Integer i2 = (Integer) o2;

	return new IntegerDiff(i1.intValue(), i2.intValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Integer.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
