package istory;

/**
 * Manager diff between number.
 *
 * @author Cedric Chantepie
 */
public class NumberDiffManager<N extends Number> implements DiffManager<N> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public NumberDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends N, B extends N> Diff<N> diff(A a, B b) {
	return new NumberDiff<N>(a, b);
    } // end of diff
} // end of class NumberDiffManager
