package istory;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.Validate;

/** 
 * Iterator over versions.
 *
 * @see istory.Version
 */
public class VersionIterator implements Iterator {
    // --- Properties ---

    /**
     * Underlying iterator.
     */
    private Iterator iterator = null;

    // --- Constructors ---

    /**
     * Iterator over versions from underlying iterator.
     *
     * @param iterator Underlying version iterator Iterator<Version>
     */
    public VersionIterator(Iterator iterator) {
	this.iterator = iterator;
    } // end of <init>

    /**
     * Iterate over version collection.
     *
     * @param versions Version collection Collection<Version>
     */
    public VersionIterator(final Collection versions) {
	Validate.notNull(versions, "Null version collection");

	// ensure type safety
	versions.toArray(new Version[versions.size()]); 

	this.iterator = versions.iterator();
    } // end of <init>

    // ---

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
	return this.iterator.hasNext();
    } // end of hasNext

    /**
     * {@inheritDoc}
     */
    public void remove() {
	this.iterator.remove();
    } // end of remove

    /**
     * {@inheritDoc}
     */
    public Object next() {
	return this.iterator.next();
    } // end of next

    /**
     * Returns next version. A type safe equivalent for next()
     * @see #next()
     */
    public Version nextVersion() {
	return (Version) this.iterator.next();
    } // end of nextVersion

} // end of class VersionIterator
