package istory.spi;

import java.util.SortedMap;
import java.util.Map;

import istory.Diff;

import org.apache.commons.lang.Validate;

/**
 * Manager diff between unsorted map.
 *
 * @author Cedric Chantepie ()
 */
public class UnsortedMapDiffManager implements DiffManagerSpi {

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
    public Diff diff(Object o1, Object o2) {
	Validate.isTrue(!(o1 instanceof SortedMap));
	Validate.isTrue(!(o2 instanceof SortedMap));

	if (o1 instanceof Map) {
	    Map s1 = (Map) o1;
	    Map s2 = (Map) o2;
	    
	    return new UnsortedMapDiff(s1, s2);
	} // end of if

	// ---

	Map c1 = (Map) o1;
	Map c2 = (Map) o2;

	return new UnsortedMapDiff(c1, c2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return (Map.class.isAssignableFrom(c) &&
		!SortedMap.class.isAssignableFrom(c));

    } // end of accept
    
} // end of class UnsortedMapDiffManager
