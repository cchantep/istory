package istory.spi;

import java.util.Collection;
import java.util.Set;

import istory.Diff;

/**
 * Manager diff between collections.
 *
 * @author Cedric Chantepie ()
 */
public class LCSCollectionDiffManager implements DiffManagerSpi {

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
    public Diff diff(Object o1, Object o2) {
	Collection s1 = (Collection) o1;
	Collection s2 = (Collection) o2;

	return new LCSCollectionDiff(s1, s2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return (Collection.class.isAssignableFrom(c) &&
		!Set.class.isAssignableFrom(c));

    } // end of accept
    
} // end of class LCSCollectionDiffManager
