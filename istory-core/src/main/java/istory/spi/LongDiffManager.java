package istory.spi;

import istory.Diff;

/**
 * Manager diff between longs.
 *
 * @author Cedric Chantepie ()
 */
public class LongDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public LongDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Long i1 = (Long) o1;
	Long i2 = (Long) o2;

	return new LongDiff(i1.longValue(), i2.longValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Long.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
