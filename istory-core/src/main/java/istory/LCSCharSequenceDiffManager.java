package istory;


/**
 * Manager diff between char sequences.
 *
 * @author Cedric Chantepie
 */
public class LCSCharSequenceDiffManager 
    implements DiffManager<CharSequence> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public LCSCharSequenceDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends CharSequence, B extends CharSequence> Diff<CharSequence> diff(final A a, final B b) {
	return new LCSCharSequenceDiff(a, b);
    } // end of diff
} // end of class LCSCharSequenceDiffManager
