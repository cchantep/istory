package istory;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Diff implementation for (unsorted) Set.
 * Collection can be accepted, in order to be diffed without ordering 
 * consideration. In such case they are treated internally
 * as unordered set.
 *
 * @param E Type of set elements
 * @author Cedric Chantepie
 * @see LCSCollectionDiff
 */
public class UnsortedSetDiff<E> implements Diff<Set<E>>, Serializable {

    // --- Properties ---

    /**
     * Original element set
     */
    private final HashSet<E> orig;

    /**
     * Destination element set
     */
    private final HashSet<E> dest;

    /**
     * Only present in original
     */
    private Set<E> removed = null;

    /**
     * Only present in destination
     */
    private Set<E> added = null;

    // --- Constructors ---

    /**
     * Bulk constructor. Collections are turned into HashSet.
     *
     * @param original Original Collection
     * @param destination Destination Collection
     */
    public UnsortedSetDiff(final Set<E> original, final Set<E> destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");

	this.orig = new HashSet<E>(original);
	this.dest = new HashSet<E>(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * Process original and destination to, 
     * first determine common elements,
     * then elements only in original 
     * and elements only in destination.
     */
    protected synchronized void process() {
	final HashSet<E> common = new HashSet<E>(this.dest);

	common.retainAll(this.orig);

	this.removed = this.orig;
	this.removed.removeAll(common);

	this.added = this.dest;
	this.added.removeAll(common);
    } // end of processed

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends Set<E>> Set<E> patch(final V orig) 
	throws DiffException {

	if (this.added == null) {
	    this.process();
	} // end of if

	final int cap = (orig.size() - this.removed.size()) +
	    this.added.size();

	final HashSet<E> set = new HashSet<E>(cap);
	
        for (final E e : orig) {
	    if (this.removed.contains(e)) {
		continue;
	    } // end of if

	    set.add(e);
	} // end of if

	set.addAll(this.added);

	return set;
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends Set<E>> Set<E> revert(final V dest) 
	throws DiffException {

	if (this.removed == null) {
	    this.process();
	} // end of if

	final int cap = (dest.size() - this.added.size()) +
	    this.removed.size();

	final HashSet<E> set = new HashSet<E>(cap);
	
        for (final E e : dest) {
	    if (this.added.contains(e)) {
		continue;
	    } // end of if

	    set.add(e);
	} // end of if

	set.addAll(this.removed);

	return set;
    } // end of revert

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
	if (o == null || !(o instanceof UnsortedSetDiff)) {
	    return false;
	} // end of if

	final UnsortedSetDiff other = (UnsortedSetDiff) o;

	return new EqualsBuilder().
	    append(this.orig, other.orig).
	    append(this.dest, other.dest).
	    append(this.removed, other.removed).
	    append(this.added, other.added).
	    isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return new HashCodeBuilder(31, 33).
	    append(this.orig).
	    append(this.dest).
	    append(this.removed).
	    append(this.added).
	    toHashCode();

    } // end of hashCode

    /**
     * {@inheritDoc}
     */
    public String toString() {
	return new ToStringBuilder(this).
	    append("original", this.orig).
	    append("destination", this.dest).
	    append("removed", this.removed).
	    append("added", this.added).
	    toString();

    } // end of toString
} // end of class UnsortedSetDiff
