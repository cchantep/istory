package istory;

import java.util.Set;


/**
 * Manager diff between unsorted set.
 *
 * @param E Type of set elements
 * @author Cedric Chantepie
 */
public class UnsortedSetDiffManager<E> implements DiffManager<Set<E>> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public UnsortedSetDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends Set<E>, B extends Set<E>> Diff<Set<E>> diff(final A a, final B b) {

	return new UnsortedSetDiff<E>(a, b);
    } // end of diff
} // end of class UnsortedSetDiffManager
