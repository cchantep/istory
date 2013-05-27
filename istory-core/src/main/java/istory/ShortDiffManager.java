package istory;

import istory.Diff;

/**
 * Manager diff between shorts.
 *
 * @author Cedric Chantepie
 */
public class ShortDiffManager implements DiffManagerSpi<Short> {

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
    public <A extends Short, B extends Short> Diff<Short> diff(final A a,
                                                               final B b) {

	return new ShortDiff(a, b);
    } // end of diff
} // end of class ShortDiffManager
