package istory;

import java.util.Collection;


/**
 * Manager diff between collections.
 *
 * @param E Type of collection element
 * @author Cedric Chantepie
 */
public class LCSCollectionDiffManager<E>
    implements DiffManager<Collection<E>> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public LCSCollectionDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends Collection<E>, B extends Collection<E>> Diff<Collection<E>> diff(final A a, final B b) {

	return new LCSCollectionDiff<E>(a, b);
    } // end of diff
} // end of class LCSCollectionDiffManager
