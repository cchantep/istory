package istory;

import java.io.Serializable;

import java.util.Collection;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;

/**
 * Diff implementation for Collection (and arrays) using LCS algorithm. 
 * Element order is kept, if you want to diff
 * without ordering consideration have a look to UnsortedSetDiff.
 *
 * @param E Type of collection elements
 * @author Cedric Chantepie
 * @see UnsortedSetDiff
 */
public class LCSCollectionDiff<E> 
    extends AbstractLCSDiff<Collection<E>,E> implements Serializable {

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original Collection
     * @param destination Destination Collection
     */
    public LCSCollectionDiff(final Collection<E> original,
                             final Collection<E> destination) {

        super();

        Validate.notNull(original, "Null original collection");
        Validate.notNull(destination, "Null destination collection");

        processLcs(original, destination);
        processDifferences(original, destination);
    } // end of <init>

    /**
     * Bulk constructor.
     *
     * @param original Original array
     * @param destination Destination array
     */
    public LCSCollectionDiff(final E[] original, final E[] destination) {
        super();

        Validate.notNull(original, "Null original array");
        Validate.notNull(destination, "Null destination array");

        final List<E> origList = Arrays.asList(original);
        final List<E> destList = Arrays.asList(destination);

        processLcs(origList, destList);
        processDifferences(origList, destList);
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(final Collection<E> container) {
        return container.size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected E elementAt(final Collection<E> value, final int index) {
        Validate.notNull(value, "Null value");
        Validate.isTrue(index >= 0, "Index less than 0");
        Validate.isTrue(index < value.size(),
                        "Index greater than or equals to length");

        // ---

        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            final List<E> list = (List<E>) value;

            return this.elementAt(list, index);
        } // end of if

        // ---

        @SuppressWarnings("unchecked")
        final E elem = (E) value.toArray()[index];

        return elem;
    } // end of elementAt

    /**
     * Returns element at |index| in given |list|.
     *
     * @param list List of elements
     * @param index Index of element in |list|
     */
    private E elementAt(final List<E> list, final int index) {
        Validate.isTrue(index < list.size(),
                        "Index greater than or equals to list size");

        return list.get(index);
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends Collection<E>> Collection<E> patch(final V orig) throws DiffException {
            
            return super.patch(orig);
        } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends Collection<E>> Collection<E> revert(final V dest) throws DiffException {

            return super.revert(dest);
        } // end of revert

    /**
     * {@inheritDoc}
     */
    protected Change<Collection<E>,E> createChange(final Collection<E> original, final Range<Integer> range) {

        return new CollectionChange<E>(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<Collection<E>,E> createChangeable(Collection<E> value) throws DiffException {

        if (!(value instanceof SortedSet)) {
            return new ChangeableCollection();
        } else {
            @SuppressWarnings("unchecked")
            final Comparator<? super E> c = ((SortedSet) value).comparator();
            
            return (c == null) 
                ? new ChangeableSortedSet()
                :  new ChangeableSortedSet(c);

        } // end of else
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof LCSCollectionDiff)) {
            return false;
        } // end of if

        final LCSCollectionDiff other = (LCSCollectionDiff) o;

        return new EqualsBuilder().
            appendSuper(super.equals(other)).
            isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return new HashCodeBuilder(31, 33).
            appendSuper(super.hashCode()).
            toHashCode();

    } // end of hashCode

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this).
            appendSuper(super.toString()).
            toString();

    } // end of toString

    // --- Inner classes ---

    /**
     * Change in a Collection of elements, some new value at specified range.
     *
     * @param E Type of collection element
     * @author Cedric Chantepie
     */
    protected class CollectionChange<E> extends Change<Collection<E>,E> {

        // --- Properties ---

        /**
         * New value
         */
        private final ArrayList<E> value = new ArrayList<E>();

        // --- Constructors ---

        public CollectionChange(final Range<Integer> range) {
            super(range);
        } // end of <init>

        // ---

        /**
         * Returns Collection change at specified range.
         */
        public Collection<E> getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized <V extends E> void appendItem(final V item) {
                Validate.notNull(item, "Null item");
                
                this.value.add(item);
            } // end of appendItem
    } // end of class CollectionChange

    /**
     * Working object representing Collection value 
     * on which change can be applied.
     *
     * @param I Type of collection element
     * @author Cedric Chantepie
     * @see CollectionChange
     */
    protected class ChangeableCollection extends Changeable<Collection<E>,E> {

        // --- Properties ---

        /**
         * Value we are working on
         */
        private final ArrayList<E> value;

        // --- Constructors ---

        /**
         * Bulk constructor.
         */
        public ChangeableCollection() {
            this.value = new ArrayList<E>();
        } // end of <init>

        // --- Properties accessors ---

        /**
         * Returns value with current changes.
         */
        public Collection<E> getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized <V extends E> void append(final V element) {
                this.value.add(element);
            } // end of append

        /**
         * {@inheritDoc}
         */
        public synchronized void insertChange(final Change<Collection<E>,E> change) {
                this.value.
                    addAll(change.getIndexRange().getMinimum(),
                           change.getValue());

            } // end of insertChange
    } // end of class ChangeableCollection

    /**
     * Working object representing sorted set on which change can be applied.
     *
     * @param E Type of set element
     * @author Cedric Chantepie
     */
    protected class ChangeableSortedSet extends Changeable<Collection<E>,E> {
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
	public synchronized void insertChange(final Change<Collection<E>,E> change) {
                this.value.addAll(change.getValue());
	} // end of insertChange
    } // end of class ChangeableSortedSet
} // end of class LCSCollectionDiff
