package istory.spi;

import istory.Diff;

/**
 * Manager diff between floats.
 *
 * @author Cedric Chantepie ()
 */
public class FloatDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public FloatDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Float i1 = (Float) o1;
	Float i2 = (Float) o2;

	return new FloatDiff(i1.floatValue(), i2.floatValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Float.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
