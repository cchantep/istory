package istory;

import java.util.SortedSet;


/**
 * Manager diff between sortedSets.
 *
 * @author Cedric Chantepie
 */
public class LCSSortedSetDiffManager<E> 
    implements DiffManager<SortedSet<E>> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public LCSSortedSetDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends SortedSet<E>, B extends SortedSet<E>> Diff<SortedSet<E>> diff(final A s1, final B s2) {
	return new LCSSortedSetDiff<E>(s1, s2);
    } // end of diff
} // end of class LCSSortedSetDiffManager
