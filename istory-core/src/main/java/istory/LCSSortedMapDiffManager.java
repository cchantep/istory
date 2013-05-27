package istory;

import java.util.SortedMap;


/**
 * Manager diff between sortedMaps.
 *
 * @param K Type of map keys
 * @param V Type of map values
 * @author Cedric Chantepie
 */
public class LCSSortedMapDiffManager<K,V> 
    implements DiffManager<SortedMap<K,V>> {

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
    public <A extends SortedMap<K,V>, B extends SortedMap<K,V>> Diff<SortedMap<K,V>> diff(final A a, final B b) {
	return new LCSSortedMapDiff<K,V>(a, b);
    } // end of diff
} // end of class LCSSortedMapDiffManager
