package istory.spi;

import java.io.Serializable;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.List;

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
 * @see istory.spi.UnsortedSetDiff
 */
public class LCSSortedSetDiff 
    extends AbstractLCSDiff<Object> 
    implements Serializable {

    // --- Properties ---

    /**
     * Original element sorted set
     */
    private Object[] orig = null;

    /**
     * Destination element sorted set
     */
    private Object[] dest = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original sorted set
     * @param destination Destination sorted set
     */
    public LCSSortedSetDiff(SortedSet original,
			    SortedSet destination) {

	super();

	Validate.notNull(original, "Null original sorted set");
	Validate.notNull(destination, "Null destination sorted set");

	this.orig = original.toArray();
	this.dest = destination.toArray();
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(Object value) {
	return ((SortedSet) value).size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected Object elementAt(Object value, int index) {
	Validate.notNull(value, "Null value");
	Validate.isTrue(value instanceof SortedSet,
			"Unsupported value type");

	Validate.isTrue(index >= 0, "Index less than 0");

	SortedSet set = (SortedSet) value;
	int len = set.size();

	Validate.isTrue(index < len,
			"Index greater than or equals to set size");

	Object res = null;
	Iterator iter = set.iterator();

	for (int i = 0; i < len && i <= index; i++) {
	    res = iter.next();
	} // end of for

	return res;
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized SortedSet patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (SortedSet) super.patch(orig);
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized SortedSet revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (SortedSet) super.revert(dest);
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
    protected Object originalElementAt(int index) {
	return this.orig[index];
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Object destinationElementAt(int index) {
	return this.dest[index];
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Change<Object> createChange(IntRange range) {
	return new SortedSetChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<Object> createChangeable(Object value) 
	throws DiffException {

	SortedSet set = (SortedSet) value;
	Comparator c = set.comparator();
	    
	if (c == null) {
	    return new ChangeableSortedSet();
	} else {
	    return new ChangeableSortedSet(c);
	} // end of else
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof LCSSortedSetDiff)) {
	    return false;
	} // end of if

	LCSSortedSetDiff other = (LCSSortedSetDiff) o;

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
     * Change in a sorted set of elements, 
     * some new value at specified range.
     *
     * @author Cedric Chantepie ()
     */
    protected class SortedSetChange extends Change<Object> {
	// --- Properties ---

	/**
	 * New value
	 */
	private ArrayList value = null;

	// --- Constructors ---

	/**
	 * {@inheritDoc}
	 */
	public SortedSetChange(IntRange range) {
	    super(range);
	} // end of <init>

	// ---

	/**
	 * Returns sorted set change at specified range.
	 */
	public List getValue() {
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void appendItem(Object item) {
	    Validate.notNull(item, "Null character");

	    if (this.value == null) {
		this.value = new ArrayList(1);
	    } // end of if

	    this.value.add(item);
	} // end of appendItem
    } // end of class SortedSetChange

    /**
     * Working object representing sorted set
     * on which change can be applied.
     *
     * @author Cedric Chantepie ()
     * @see SortedSetChange
     */
    protected class ChangeableSortedSet extends Changeable<Object> {
	// --- Properties ---

	/**
	 * Value we are working on
	 */
	private TreeSet value = null;

	// --- Constructors ---

	/**
	 * Bulk constructor.
	 */
	public ChangeableSortedSet() {
	    this.value = new TreeSet();
	} // end of <init>

	/**
	 * Comparator constructor.
	 */
	public ChangeableSortedSet(Comparator comparator) {
	    this.value = new TreeSet(comparator);
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 */
	public SortedSet getValue() {
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void append(Object element) {
	    this.value.add(element);
	} // end of append

	/**
	 * {@inheritDoc}
	 */
	public synchronized void insertChange(Change<Object> change) {
	    Object val = change.getValue();

	    Validate.isTrue((val instanceof List),
			    "Expected string as change value");

	    List list = (List) val;
	    IntRange r = change.getIndexRange();
	    
	    this.value.addAll(list);
	} // end of insertChange
    } // end of class ChangeableSortedSet
} // end of class LCSSortedSetDiff
