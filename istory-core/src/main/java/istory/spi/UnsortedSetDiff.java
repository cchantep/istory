package istory.spi;

import java.io.Serializable;

import java.util.Collection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import istory.DiffException;
import istory.Diff;

/**
 * Diff implementation for (unsorted) Set.
 * Collection can be accepted, in order to be diffed without ordering 
 * consideration. In such case they are treated internally
 * as unordered set.
 *
 * @author Cedric Chantepie ()
 * @see istory.spi.LCSCollectionDiff
 * @see istory.spi.LCSSortedSetDiff
 * @todo Do not keep common
 */
public class UnsortedSetDiff implements Diff, Serializable {

    // --- Properties ---

    /**
     * Original element set
     */
    private Set orig = null;

    /**
     * Destination element set
     */
    private Set dest = null;

    /**
     * Only present in original
     */
    private Set removed = null;

    /**
     * Only present in destination
     */
    private Set added = null;

    // --- Constructors ---

    /**
     * Bulk constructor. Collections are turned into HashSet.
     *
     * @param original Original Collection
     * @param destination Destination Collection
     */
    public UnsortedSetDiff(Collection original,
			   Collection destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");
	Validate.isTrue(!(original instanceof SortedSet),
			"Unexpected sorted set");
	Validate.isTrue(!(destination instanceof SortedSet),
			"Unexpected sorted set");

	this.orig = new HashSet(original);
	this.dest = new HashSet(destination);
    } // end of <init>

    /**
     * Bulk constructor.
     *
     * @param original Original set
     * @param destination Destination set
     */
    public UnsortedSetDiff(Set original,
			   Set destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");
	Validate.isTrue(!(original instanceof SortedSet),
			"Unexpected sorted set");
	Validate.isTrue(!(destination instanceof SortedSet),
			"Unexpected sorted set");

	this.orig = new HashSet(original);
	this.dest = new HashSet(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * Process original and destination to, 
     * first determine common elements,
     * then elements only in original 
     * and elements only in destination.
     */
    protected synchronized void process() {
	HashSet common = new HashSet(this.dest);

	common.retainAll(this.orig);

	this.removed = this.orig;
	this.orig = null;
	
	this.removed.removeAll(common);

	this.added = this.dest;
	this.dest = null;
	
	this.added.removeAll(common);
    } // end of processed

    /**
     * {@inheritDoc}
     */
    public synchronized Set patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    this.process();
	} // end of if

	Collection origCol = (Collection) orig;
	Iterator iter = origCol.iterator();
	int cap = (origCol.size() - this.removed.size()) +
	    this.added.size();

	HashSet set = new HashSet(cap);
	
	Object obj;
	while (iter.hasNext()) {
	    obj = iter.next();

	    if (this.removed.contains(obj)) {
		continue;
	    } // end of if

	    set.add(obj);
	} // end of if

	set.addAll(this.added);

	return set;
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized Set revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    this.process();
	} // end of if

	Collection destCol = (Collection) dest;
	Iterator iter = destCol.iterator();
	int cap = (destCol.size() - this.added.size()) +
	    this.removed.size();

	HashSet set = new HashSet(cap);
	
	Object obj;
	while (iter.hasNext()) {
	    obj = iter.next();

	    if (this.added.contains(obj)) {
		continue;
	    } // end of if

	    set.add(obj);
	} // end of if

	set.addAll(this.removed);

	return set;
    } // end of revert

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof UnsortedSetDiff)) {
	    return false;
	} // end of if

	UnsortedSetDiff other = (UnsortedSetDiff) o;

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
