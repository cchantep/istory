package istory;

import java.io.Serializable;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;


/**
 * Diff implementation for sorted set using LCS algorithm. 
 *
 * @param E Type of set elements
 * @author Cedric Chantepie
 * @see istory.spi.LCSCollectionDiff
 * @see istory.spi.UnsortedSetDiff
 */
public class LCSSortedSetDiff<E> 
    extends AbstractLCSDiff<SortedSet<E>,E> implements Serializable {

    // --- Properties ---

    /**
     * Original element sorted set
     */
    private final ArrayList<E> orig;

    /**
     * Destination element sorted set
     */
    private final ArrayList<E> dest;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original sorted set
     * @param destination Destination sorted set
     */
    public LCSSortedSetDiff(final SortedSet<E> original,
			    final SortedSet<E> destination) {

	super();

	Validate.notNull(original, "Null original sorted set");
	Validate.notNull(destination, "Null destination sorted set");

	this.orig = new ArrayList<E>(original);
	this.dest = new ArrayList<E>(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected <V extends SortedSet<E>> int count(final V value) {
	return value.size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected <V extends SortedSet<E>> E elementAt(final V value, 
                                                   final int index) {

	Validate.notNull(value, "Null value");
	Validate.isTrue(index >= 0, "Index less than 0");
	Validate.isTrue(index < value.size(),
			"Index greater than or equals to set size");

        final Object[] array = value.toArray();

        @SuppressWarnings("unchecked")
        final E elem = (E) array[index];

        return elem;
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends SortedSet<E>> SortedSet<E> patch(final V orig) throws DiffException {

	if (this.added == null) {
	    processLcs();
	    processDifferences();
	} // end of if

	return super.patch(orig);
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends SortedSet<E>> SortedSet<E> revert(final V dest) throws DiffException {

	if (this.removed == null) {
	    processLcs();
	    processDifferences();
	} // end of if

	return super.revert(dest);
    } // end of revert

    /**
     * {@inheritDoc}
     */
    protected int originalSize() {
	return this.orig.size();
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected int destinationSize() {
	return this.dest.size();
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected E originalElementAt(final int index) {
	return this.orig.get(index);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected E destinationElementAt(final int index) {
	return this.dest.get(index);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Change<SortedSet<E>,E> createChange(final Range<Integer> range) {
	return new SortedSetChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected <V extends SortedSet<E>> Changeable<SortedSet<E>,E> createChangeable(V value) throws DiffException {

	final Comparator<? super E> c = value.comparator();

        return (c == null) 
            ? new ChangeableSortedSet<E>()
            :  new ChangeableSortedSet<E>(c);

    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
	if (o == null || !(o instanceof LCSSortedSetDiff)) {
	    return false;
	} // end of if

	final LCSSortedSetDiff other = (LCSSortedSetDiff) o;

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
     * Change in a sorted set of elements, some new value at specified range.
     *
     * @param E Type of set element
     * @author Cedric Chantepie
     */
    protected class SortedSetChange extends Change<SortedSet<E>,E> {
	// --- Properties ---

	/**
	 * New value
	 */
	private final TreeSet<E> value = new TreeSet<E>();

	// --- Constructors ---

	/**
	 * {@inheritDoc}
	 */
	public SortedSetChange(final Range<Integer> range) {
	    super(range);
	} // end of <init>

	// ---

	/**
	 * Returns sorted set change at specified range.
	 */
	public SortedSet<E> getValue() {
            return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized <V extends E> void appendItem(final V item) {
	    Validate.notNull(item, "Null item");

	    this.value.add(item);
	} // end of appendItem
    } // end of class SortedSetChange

    /**
     * Working object representing sorted set on which change can be applied.
     *
     * @param E Type of set element
     * @author Cedric Chantepie
     * @see SortedSetChange
     */
    protected class ChangeableSortedSet<E> extends Changeable<SortedSet<E>,E> {
	// --- Properties ---

	/**
	 * Value we are working on
	 */
	private final TreeSet<E> value;

	// --- Constructors ---

	/**
	 * Bulk constructor.
	 */
	public ChangeableSortedSet() {
	    this.value = new TreeSet<E>();
	} // end of <init>

	/**
	 * Comparator constructor.
	 */
	public ChangeableSortedSet(final Comparator<? super E> comparator) {
	    this.value = new TreeSet<E>(comparator);
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 */
	public SortedSet<E> getValue() {
	    return this.value;
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized <V extends E> void append(V element) {
	    this.value.add(element);
	} // end of append

	/**
	 * {@inheritDoc}
	 */
	public synchronized void insertChange(final Change<SortedSet<E>,E> change) {
                this.value.addAll(change.getValue());
	} // end of insertChange
    } // end of class ChangeableSortedSet
} // end of class LCSSortedSetDiff
