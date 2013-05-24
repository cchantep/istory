package istory.spi;

import java.io.Serializable;

import java.util.SortedMap;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import istory.DiffException;
import istory.Diff;

/**
 * Diff implementation for (unsorted) Map.
 *
 * @author Cedric Chantepie ()
 * @see istory.spi.LCSSortedMapDiff
 * @todo Do not keep common
 */
public class UnsortedMapDiff implements Diff, Serializable {

    // --- Properties ---

    /**
     * Original element map
     */
    private Map orig = null;

    /**
     * Destination element map
     */
    private Map dest = null;

    /**
     * Only present in original
     */
    private Set<Map.Entry> removed = null;

    /**
     * Only present in destination
     */
    private Set<Map.Entry> added = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original map
     * @param destination Destination map
     */
    public UnsortedMapDiff(Map original,
			   Map destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");
	Validate.isTrue(!(original instanceof SortedMap),
			"Unexpected sorted map");
	Validate.isTrue(!(destination instanceof SortedMap),
			"Unexpected sorted map");

	this.orig = new HashMap(original);
	this.dest = new HashMap(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * Process original and destination to, 
     * first determine common elements,
     * then elements only in original 
     * and elements only in destination.
     */
    protected synchronized void process() {
	HashSet<Map.Entry> common = 
	    new HashSet<Map.Entry>(this.dest.entrySet());

	common.retainAll(this.orig.entrySet());

	this.removed = this.orig.entrySet();
	this.orig = null;
	
	this.removed.removeAll(common);

	this.added = this.dest.entrySet();
	this.dest = null;
	
	this.added.removeAll(common);
    } // end of processed

    /**
     * {@inheritDoc}
     */
    public synchronized Map patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    this.process();
	} // end of if

	Map origMap = (Map) orig;
	Iterator<Map.Entry> iter = origMap.entrySet().iterator();
	int cap = (origMap.size() - this.removed.size()) +
	    this.added.size();

	origMap = new HashMap(cap);
	
	Map.Entry entry;
	while (iter.hasNext()) {
	    entry = iter.next();

	    if (this.removed.contains(entry)) {
		continue;
	    } // end of if

	    origMap.put(entry.getKey(),
			entry.getValue());

	} // end of if

	iter = this.added.iterator();

	while (iter.hasNext()) {
	    entry = iter.next();

	    origMap.put(entry.getKey(), entry.getValue());
	} // end of while

	return origMap;
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized Map revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    this.process();
	} // end of if

	Map destMap = (Map) dest;
	Iterator<Map.Entry> iter = destMap.entrySet().iterator();
	int cap = (destMap.size() - this.added.size()) +
	    this.removed.size();

	destMap = new HashMap(cap);
	
	Map.Entry entry;
	while (iter.hasNext()) {
	    entry = iter.next();

	    if (this.added.contains(entry)) {
		continue;
	    } // end of if

	    destMap.put(entry.getKey(),
			entry.getValue());

	} // end of if

	iter = this.removed.iterator();

	while (iter.hasNext()) {
	    entry = iter.next();

	    destMap.put(entry.getKey(), entry.getValue());
	} // end of while

	return destMap;
    } // end of revert

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof UnsortedMapDiff)) {
	    return false;
	} // end of if

	UnsortedMapDiff other = (UnsortedMapDiff) o;

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
} // end of class UnsortedMapDiff
