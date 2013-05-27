package istory;

import java.io.Serializable;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;

import istory.DiffException;

/**
 * Diff implementation for Collection (and arrays), excepted Set,
 * using LCS algorithm. Element order is kept, if you want to diff
 * without ordering consideration have a look to LCSSetDiff.
 *
 * @param E Type of collection elements
 * @author Cedric Chantepie
 * @see istory.spi.LCSSortedSetDiff
 * @see istory.spi.UnsortedSetDiff
 */
public class LCSCollectionDiff<E> 
    extends AbstractLCSDiff<Collection<E>,E> implements Serializable {

    // --- Properties ---

    /**
     * Original elements
     */
    private final List<E> orig;

    /**
     * Destination elements
     */
    private final List<E> dest;

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

        this.orig = new ArrayList<E>(original);
        this.dest = new ArrayList<E>(destination);
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

        this.orig = Arrays.asList(original);
        this.dest = Arrays.asList(destination);
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected <V extends Collection<E>> int count(final V value) {
        return value.size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected <V extends Collection<E>> E elementAt(final V value, final int index) {
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

            if (this.added == null) {
                processLcs();
                processDifferences();
            } // end of if

            return super.patch(orig);
        } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends Collection<E>> Collection<E> revert(final V dest) throws DiffException {

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
    protected Change<Collection<E>,E> createChange(final Range<Integer> range) {
        return new CollectionChange<E>(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected <V extends Collection<E>> Changeable<Collection<E>,E> createChangeable(V value) throws DiffException {

        return new ChangeableCollection<E>();
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof LCSCollectionDiff)) {
            return false;
        } // end of if

        final LCSCollectionDiff other = (LCSCollectionDiff) o;

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

        /**
         * {@inheritDoc}
         */
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
     * @param E Type of collection element
     * @author Cedric Chantepie
     * @see CollectionChange
     */
    protected class ChangeableCollection<E> extends Changeable<Collection<E>,E> {
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
} // end of class LCSCollectionDiff
