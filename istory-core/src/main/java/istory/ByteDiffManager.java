package istory;

import istory.Diff;

/**
 * Manager diff between bytes.
 *
 * @author Cedric Chantepie
 */
public class ByteDiffManager implements DiffManagerSpi<Byte> {

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
    public <A extends Byte, B extends Byte> Diff<Byte> diff(final A a, 
                                                            final B b) {

	return new ByteDiff(a, b);
    } // end of diff
} // end of class ByteDiffManager
