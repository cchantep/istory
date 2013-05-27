package istory;


/**
 * Manager diff between strings.
 *
 * @author Cedric Chantepie
 */
public class SimpleStringDiffManager implements DiffManager<String> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public SimpleStringDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends String, B extends String> Diff<String> diff(final A a, final B b) {

	return new SimpleStringDiff(a, b);
    } // end of diff
} // end of class SimpleStringDiffManager
