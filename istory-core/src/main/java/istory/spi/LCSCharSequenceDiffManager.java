package istory.spi;

import istory.Diff;

/**
 * Manager diff between char sequences.
 *
 * @author Cedric Chantepie ()
 */
public class LCSCharSequenceDiffManager implements DiffManagerSpi {

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
    public Diff diff(Object o1, Object o2) {
	CharSequence s1 = (CharSequence) o1;
	CharSequence s2 = (CharSequence) o2;

	return new LCSCharSequenceDiff(s1, s2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return CharSequence.class.isAssignableFrom(c);
    } // end of accept
    
} // end of class LCSCharSequenceDiffManager
