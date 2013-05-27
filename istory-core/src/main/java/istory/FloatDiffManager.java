package istory;

import istory.Diff;

/**
 * Manager diff between floats.
 *
 * @author Cedric Chantepie
 */
public class FloatDiffManager implements DiffManagerSpi<Float> {

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
    public <A extends Float, B extends Float> Diff<Float> diff(final A a,
                                                               final B b) {

	return new FloatDiff(a, b);
    } // end of diff
} // end of class FloatDiffManager
