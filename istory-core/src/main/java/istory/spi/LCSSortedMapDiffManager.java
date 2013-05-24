package istory.spi;

import java.util.SortedMap;

import istory.Diff;

/**
 * Manager diff between sortedMaps.
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedMapDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public LCSSortedMapDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	SortedMap s1 = (SortedMap) o1;
	SortedMap s2 = (SortedMap) o2;

	return new LCSSortedMapDiff(s1, s2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return (SortedMap.class.isAssignableFrom(c));
    } // end of accept
    
} // end of class LCSSortedMapDiffManager
