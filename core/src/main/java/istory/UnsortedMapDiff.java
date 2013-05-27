package istory;

import java.io.Serializable;

import java.util.SortedMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;


/**
 * Diff implementation for (unsorted) Map.
 *
 * @param K Type of map keys
 * @param V Type of map values
 * @author Cedric Chantepie
 * @see istory.spi.LCSSortedMapDiff
 * @todo Do not keep common
 */
public class UnsortedMapDiff<K,V> implements Diff<Map<K,V>>, Serializable {

    // --- Properties ---

    /**
     * Original element map
     */
    private final HashMap<K,V> orig;

    /**
     * Destination element map
     */
    private final HashMap<K,V> dest;

    /**
     * Only present in original
     */
    private Set<Map.Entry<K,V>> removed = null;

    /**
     * Only present in destination
     */
    private Set<Map.Entry<K,V>> added = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original map
     * @param destination Destination map
     */
    public UnsortedMapDiff(final Map<K,V> original,
			   final Map<K,V> destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");

	this.orig = new HashMap<K,V>(original);
	this.dest = new HashMap<K,V>(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * Process original and destination to, 
     * first determine common elements,
     * then elements only in original 
     * and elements only in destination.
     */
    protected synchronized void process() {
            final HashSet<Map.Entry<K,V>> common = 
                new HashSet<Map.Entry<K,V>>(this.dest.entrySet());

	common.retainAll(this.orig.entrySet());

	this.removed = this.orig.entrySet();
	this.removed.removeAll(common);

	this.added = this.dest.entrySet();
	this.added.removeAll(common);
    } // end of processed

    /**
     * {@inheritDoc}
     */
    public synchronized <M extends Map<K,V>> Map<K,V> patch(final M orig) 
	throws DiffException {

	if (this.added == null) {
	    this.process();
	} // end of if

	final int cap = (orig.size() - this.removed.size()) + this.added.size();
	final HashMap<K,V> origMap = new HashMap<K,V>(cap);
	
	for (final Map.Entry<K,V> entry : orig.entrySet()) {
	    if (this.removed.contains(entry)) {
		continue;
	    } // end of if

	    origMap.put(entry.getKey(), entry.getValue());
	} // end of if

        for (final Map.Entry<K,V> entry : this.added) {
	    origMap.put(entry.getKey(), entry.getValue());
	} // end of while

	return origMap;
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <M extends Map<K,V>> Map<K,V> revert(final M dest) 
	throws DiffException {

	if (this.removed == null) {
	    this.process();
	} // end of if

        // ---

	final int cap = (dest.size() - this.added.size()) + this.removed.size();
	final HashMap<K,V> destMap = new HashMap<K,V>(cap);
	
	for (final Map.Entry<K,V> entry : dest.entrySet()) {
	    if (this.added.contains(entry)) {
		continue;
	    } // end of if

	    destMap.put(entry.getKey(), entry.getValue());
	} // end of if

        for (final Map.Entry<K,V> entry : this.removed) {
	    destMap.put(entry.getKey(), entry.getValue());
	} // end of while

	return destMap;
    } // end of revert

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
	if (o == null || !(o instanceof UnsortedMapDiff)) {
	    return false;
	} // end of if

	final UnsortedMapDiff other = (UnsortedMapDiff) o;

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
