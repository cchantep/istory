package istory.spi;

import java.io.Serializable;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.math.IntRange;

import istory.DiffException;

/**
 * Diff implementation for Collection (and arrays), excepted Set,
 * using LCS algorithm. Element order is kept, if you want to diff
 * without ordering consideration have a look to LCSSetDiff.
 *
 * @author Cedric Chantepie ()
 * @see istory.spi.LCSSortedSetDiff
 * @see istory.spi.UnsortedSetDiff
 */
public class LCSCollectionDiff 
    extends AbstractLCSDiff<Object> 
    implements Serializable {

    // --- Properties ---

    /**
     * Original elements
     */
    private Object[] orig = null;

    /**
     * Destination elements
     */
    private Object[] dest = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original Collection
     * @param destination Destination Collection
     */
    public LCSCollectionDiff(Collection original,
			     Collection destination) {

	super();

	Validate.notNull(original, "Null original collection");
	Validate.notNull(destination, "Null destination collection");
	Validate.isTrue(!(original instanceof Set), "Expected set");
	Validate.isTrue(!(destination instanceof Set), "Expected set");

	this.orig = original.toArray();
	this.dest = destination.toArray();
    } // end of <init>

    /**
     * Bulk constructor.
     *
     * @param original Original array
     * @param destination Destination array
     */
    public LCSCollectionDiff(Object[] original,
			     Object[] destination) {

	super();

	Validate.notNull(original, "Null original array");
	Validate.notNull(destination, "Null destination array");

	this.orig = original;
	this.dest = destination;
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(Object value) {
	if (value instanceof Collection) {
	    return ((Collection) value).size();
	} else {
	    return ((Object[]) value).length;
	} // end of else
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected Object elementAt(Object value, int index) {
	Validate.notNull(value, "Null value");

	Class clazz = value.getClass();

	if (!clazz.isArray() && !(value instanceof Collection)) {
	    throw new IllegalArgumentException("Expected collection " +
					       "or array");

	} // end of if

	Validate.isTrue(index >= 0, "Index less than 0");

	// ---

	if (value instanceof List) {
	    return this.elementAt((List) value, index);
	} // end of if

	Object[] array = null;

	if (value instanceof Collection) {
	    array = ((Collection) value).toArray();
	} else {
	    array = (Object[]) value;
	} // end of else

	Validate.notNull(array, "Invalid value type");
	Validate.isTrue(index < array.length,
			"Index greater than or equals to length");

	return array[index];
    } // end of elementAt

    /**
     * Returns element at |index| in given |list|.
     *
     * @param list List of elements
     * @param index Index of element in |list|
     */
    private Object elementAt(List list, int index) {
	Validate.isTrue(index < list.size(),
			"Index greater than or equals to list size");

	return list.get(index);
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized Collection patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (Collection) super.patch(orig);
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized Collection revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (Collection) super.revert(dest);
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
	return new CollectionChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<Object> createChangeable(Object value) 
	throws DiffException {

	return new ChangeableCollection();
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof LCSCollectionDiff)) {
	    return false;
	} // end of if

	LCSCollectionDiff other = (LCSCollectionDiff) o;

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
	return new HashCodeBuilder(31, 33).
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
     * Change in a Collection of elements, 
     * some new value at specified range.
     *
     * @author Cedric Chantepie ()
     */
    protected class CollectionChange extends Change<Object> {

	// --- Properties ---

	/**
	 * New value
	 */
	private ArrayList value = null;

	// --- Constructors ---

	/**
	 * {@inheritDoc}
	 */
	public CollectionChange(IntRange range) {
	    super(range);
	} // end of <init>

	// ---

	/**
	 * Returns Collection change at specified range.
	 */
	public Collection getValue() {
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
    } // end of class CollectionChange

    /**
     * Working object representing Collection value 
     * on which change can be applied.
     *
     * @author Cedric Chantepie ()
     * @see CollectionChange
     */
    protected class ChangeableCollection extends Changeable<Object> {
	// --- Properties ---

	/**
	 * Value we are working on
	 */
	private ArrayList value = null;

	// --- Constructors ---

	/**
	 * Bulk constructor.
	 */
	public ChangeableCollection() {
	    this.value = new ArrayList();
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 */
	public Collection getValue() {
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

	    Validate.isTrue((val instanceof Collection),
			    "Expected string as change value");

	    Collection col = (Collection) val;
	    IntRange r = change.getIndexRange();
	    
	    this.value.
		addAll(r.getMinimumInteger(),
		       col);

	} // end of insertChange
    } // end of class ChangeableCollection
} // end of class LCSCollectionDiff
