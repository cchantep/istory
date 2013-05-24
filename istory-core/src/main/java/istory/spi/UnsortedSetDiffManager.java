package istory.spi;

import java.util.Collection;
import java.util.SortedSet;
import java.util.Set;

import istory.Diff;

import org.apache.commons.lang.Validate;

/**
 * Manager diff between unsorted set.
 *
 * @author Cedric Chantepie ()
 */
public class UnsortedSetDiffManager implements DiffManagerSpi {

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
    public Diff diff(Object o1, Object o2) {
	Validate.isTrue(!(o1 instanceof SortedSet));
	Validate.isTrue(!(o2 instanceof SortedSet));

	if (o1 instanceof Set) {
	    Set s1 = (Set) o1;
	    Set s2 = (Set) o2;
	    
	    return new UnsortedSetDiff(s1, s2);
	} // end of if

	// ---

	Collection c1 = (Collection) o1;
	Collection c2 = (Collection) o2;

	return new UnsortedSetDiff(c1, c2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return (Collection.class.isAssignableFrom(c) &&
		!SortedSet.class.isAssignableFrom(c));

    } // end of accept
    
} // end of class UnsortedSetDiffManager
