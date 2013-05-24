package istory.spi;

import java.util.SortedSet;

import istory.Diff;

/**
 * Manager diff between sortedSets.
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedSetDiffManager implements DiffManagerSpi {

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
    public Diff diff(Object o1, Object o2) {
	SortedSet s1 = (SortedSet) o1;
	SortedSet s2 = (SortedSet) o2;

	return new LCSSortedSetDiff(s1, s2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return (SortedSet.class.isAssignableFrom(c));
    } // end of accept
    
} // end of class LCSSortedSetDiffManager
