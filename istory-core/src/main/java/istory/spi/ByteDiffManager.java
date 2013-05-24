package istory.spi;

import istory.Diff;

/**
 * Manager diff between bytes.
 *
 * @author Cedric Chantepie ()
 */
public class ByteDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public ByteDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	Byte i1 = (Byte) o1;
	Byte i2 = (Byte) o2;

	return new ByteDiff(i1.byteValue(), i2.byteValue());
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return Byte.class.isAssignableFrom(c);
    } // end of accept
    
} // end of interface DiffManagerSpi
