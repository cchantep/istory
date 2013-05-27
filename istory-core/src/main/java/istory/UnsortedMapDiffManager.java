package istory;

import java.util.Map;


/**
 * Manager diff between unsorted map.
 *
 * @param K Type of map keys
 * @param V Type of map values
 * @author Cedric Chantepie
 */
public class UnsortedMapDiffManager<K,V> implements DiffManager<Map<K,V>> {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public UnsortedMapDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public <A extends Map<K,V>, B extends Map<K,V>> Diff<Map<K,V>> diff(final A a, final B b) {

	return new UnsortedMapDiff<K,V>(a, b);
    } // end of diff
} // end of class UnsortedMapDiffManager
