package istory.spi;

import java.io.Serializable;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.math.IntRange;

import istory.DiffException;

/**
 * Diff implementation for sorted set using LCS algorithm. 
 *
 * @author Cedric Chantepie ()
 * @see istory.spi.LCSCollectionDiff
 * @see istory.spi.UnsortedMapDiff
 */
public class LCSSortedMapDiff 
    extends AbstractLCSDiff<Map.Entry> 
    implements Serializable {

    // --- Properties ---

    /**
     * Original element sorted set
     */
    private Map.Entry[] orig = null;

    /**
     * Destination element sorted set
     */
    private Map.Entry[] dest = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original sorted set
     * @param destination Destination sorted set
     */
    public LCSSortedMapDiff(SortedMap original,
			    SortedMap destination) {

	super();

	Validate.notNull(original, "Null original sorted set");
	Validate.notNull(destination, "Null destination sorted set");

	this.orig = (Map.Entry[]) original.entrySet().
	    toArray(new Map.Entry[original.size()]);

	this.dest = (Map.Entry[]) destination.entrySet().
	    toArray(new Map.Entry[destination.size()]);

    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(Object value) {
	return ((SortedMap) value).size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected Map.Entry elementAt(Object value, int index) {
	Validate.notNull(value, "Null value");
	Validate.isTrue(value instanceof SortedMap,
			"Unsupported value type");

	Validate.isTrue(index >= 0, "Index less than 0");

	SortedMap map = (SortedMap) value;
	int len = map.size();

	Validate.isTrue(index < len,
			"Index greater than or equals to set size");

	Map.Entry entry = null;
	Iterator<Map.Entry> iter = map.entrySet().iterator();

	for (int i = 0; i < len && i <= index; i++) {
	    entry = iter.next();
	} // end of for

	return entry;
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (SortedMap) super.patch(orig);
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized SortedMap revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (SortedMap) super.revert(dest);
    } // end of revert

    /**
     * {@inheritDoc}
     */
    protected int originalSize() {
	return this.orig.length;
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected int destinationSize() {
	return this.dest.length;
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Map.Entry originalElementAt(int index) {
	return this.orig[index];
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Map.Entry destinationElementAt(int index) {
	return this.dest[index];
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Change<Map.Entry> createChange(IntRange range) {
	return new SortedMapChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<Map.Entry> createChangeable(Object value) 
	throws DiffException {

	SortedMap map = (SortedMap) value;
	Comparator c = map.comparator();

	if (c == null) {
	    return new ChangeableSortedMap();
	} else {
	    return new ChangeableSortedMap(c);
	} // end of else
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof LCSSortedMapDiff)) {
	    return false;
	} // end of if

	LCSSortedMapDiff other = (LCSSortedMapDiff) o;

	return new EqualsBuilder().
	    appendSuper(super.equals(other)).
	    append(this.orig, other.orig).
	    append(this.dest, other.dest).
	    isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return new HashCodeBuilder(33, 35).
	    appendSuper(super.hashCode()).
	    append(this.orig).
	    append(this.dest).
	    toHashCode();

    } // end of hashCode

    /**
     * {@inheritDoc}
     */
    public String toString() {
	return new ToStringBuilder(this).
	    appendSuper(super.toString()).
	    append("original", this.orig).
	    append("destination", this.dest).
	    toString();

    } // end of toString

    // --- Inner classes ---

    /**
     * Change in a sorted map of elements, 
     * some new value at specified range.
     *
     * @author Cedric Chantepie ()
     */
    protected class SortedMapChange extends Change<Map.Entry> {
	// --- Properties ---

	/**
	 * New value
	 */
	private ArrayList<Map.Entry> value = null;

	// --- Constructors ---

	/**
	 * {@inheritDoc}
	 */
	public SortedMapChange(IntRange range) {
	    super(range);
	} // end of <init>

	// ---

	/**
	 * Returns sorted set change at specified range.
	 */
	public List<Map.Entry> getValue() {
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void appendItem(Map.Entry item) {
	    Validate.notNull(item, "Null character");

	    if (this.value == null) {
		this.value = new ArrayList<Map.Entry>(1);
	    } // end of if

	    this.value.add(item);
	} // end of appendItem
    } // end of class SortedMapChange

    /**
     * Working object representing sorted map
     * on which change can be applied.
     *
     * @author Cedric Chantepie ()
     * @see SortedMapChange
     */
    protected class ChangeableSortedMap extends Changeable<Map.Entry> {
	// --- Properties ---

	/**
	 * Value we are working on
	 */
	private TreeMap value = null;

	// --- Constructors ---

	/**
	 * Bulk constructor.
	 */
	public ChangeableSortedMap() {
	    this.value = new TreeMap();
	} // end of <init>

	/**
	 * Comparator constructor.
	 */
	public ChangeableSortedMap(Comparator comparator) {
	    this.value = new TreeMap(comparator);
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 */
	public SortedMap getValue() {
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void append(Map.Entry element) {
	    this.value.put(element.getKey(), 
			   element.getValue());

	} // end of append

	/**
	 * {@inheritDoc}
	 */
	public synchronized void insertChange(Change<Map.Entry> change) {
	    Object val = change.getValue();

	    Validate.isTrue((val instanceof List),
			    "Expected string as change value");

	    List<Map.Entry> list = (List<Map.Entry>) val;
	    IntRange r = change.getIndexRange();
	    Iterator<Map.Entry> iter = list.iterator();

	    Map.Entry entry;
	    while (iter.hasNext()) {
		entry = iter.next();

		this.value.put(entry.getKey(), entry.getValue());
	    } // end of while
	} // end of insertChange
    } // end of class ChangeableSortedMap
} // end of class LCSSortedMapDiff
