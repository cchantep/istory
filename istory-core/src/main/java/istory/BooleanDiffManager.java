package istory;


/**
 * Manager diff between booleans.
 *
 * @author Cedric Chantepie
 */
public class BooleanDiffManager implements DiffManager<Boolean> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public BooleanDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends Boolean, B extends Boolean> Diff<Boolean> diff(final A a, final B b) {

	return new BooleanDiff(a, b);
    } // end of diff
} // end of class BooleanDiffManager
