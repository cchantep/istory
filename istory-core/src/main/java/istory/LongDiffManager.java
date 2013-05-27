package istory;

import istory.Diff;

/**
 * Manager diff between longs.
 *
 * @author Cedric Chantepie
 */
public class LongDiffManager implements DiffManagerSpi<Long> {

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
    public <A extends Long, B extends Long> Diff<Long> diff(final A a, 
                                                            final B b) {

	return new LongDiff(a.longValue(), b.longValue());
    } // end of diff
} // end of class LongDiffManager
