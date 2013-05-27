package istory;

import istory.Diff;

/**
 * Manager diff between doubles.
 *
 * @author Cedric Chantepie
 */
public class DoubleDiffManager implements DiffManagerSpi<Double> {

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
    public <A extends Double, B extends Double> Diff<Double> diff(final A a, 
                                                                  final B b) {

	return new DoubleDiff(a, b);
    } // end of diff
} // end of class DoubleDiffManager
