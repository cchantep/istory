package istory;

import istory.Diff;

/**
 * Manager diff between integers.
 *
 * @author Cedric Chantepie
 */
public class IntegerDiffManager implements DiffManagerSpi<Integer> {

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
    public Diff<Integer> diff(final Integer i1, final Integer i2) {
	return new IntegerDiff(i1.intValue(), i2.intValue());
    } // end of diff
} // end of class IntegerDiffManager
