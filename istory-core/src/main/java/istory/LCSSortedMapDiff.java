package istory;

import java.io.Serializable;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;

import istory.DiffException;

/**
 * Diff implementation for sorted set using LCS algorithm. 
 *
 * @param K Type of map keys
 * @param V Type of map values
 * @author Cedric Chantepie
 * @see istory.spi.LCSCollectionDiff
 * @see istory.spi.UnsortedMapDiff
 */
public class LCSSortedMapDiff<K,V> 
    extends AbstractLCSDiff<SortedMap<K,V>, Map.Entry<K,V>> 
    implements Serializable {

    // --- Properties ---

    /**
     * Original element sorted set
     */
    private final SortedMap<K,V> orig;

    /**
     * Destination element sorted set
     */
    private final SortedMap<K,V> dest;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original sorted set
     * @param destination Destination sorted set
     */
    public LCSSortedMapDiff(SortedMap<K,V> original,
                            SortedMap<K,V> destination) {

        super();

        Validate.notNull(original, "Null original sorted set");
        Validate.notNull(destination, "Null destination sorted set");

        this.orig = original;
        this.dest = destination;
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected <M extends SortedMap<K,V>> int count(final M value) {
        return value.size();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected <M extends SortedMap<K,V>> Map.Entry<K,V> elementAt(final M value, final int index) {

        Validate.notNull(value, "Null value");
        Validate.isTrue(index >= 0, "Index less than 0");

        final int len = value.size();

        Validate.isTrue(index < len, 
                        "Index greater than or equals to set size");

        final Object[] entries = value.entrySet().toArray();

        @SuppressWarnings("unchecked")
        final Map.Entry<K,V> entry = (Map.Entry<K,V>) entries[index];

        return entry;
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized <M extends SortedMap<K,V>> SortedMap<K,V> patch(final M orig) throws DiffException {

            if (this.added == null) {
                processLcs();
                processDifferences();
            } // end of if

            return super.patch(orig);
        } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <M extends SortedMap<K,V>> SortedMap<K,V> revert(final M dest) throws DiffException {

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
    protected Map.Entry<K,V> originalElementAt(final int index) {
        final Object[] array = this.orig.entrySet().toArray();

        @SuppressWarnings("unchecked")
        final Map.Entry<K,V> entry = (Map.Entry<K,V>) array[index];

        return entry;
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Map.Entry<K,V> destinationElementAt(final int index) {
        final Object[] array = this.dest.entrySet().toArray();

        @SuppressWarnings("unchecked")
        final Map.Entry<K,V> entry = (Map.Entry<K,V>) array[index];

        return entry;
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Change<SortedMap<K,V>, Map.Entry<K,V>> createChange(final Range<Integer> range) {

        final Comparator<? super K> c = this.orig.comparator();

        return new SortedMapChange<K,V>(range, c);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected <M extends SortedMap<K,V>> Changeable<SortedMap<K,V>, Map.Entry<K,V>> createChangeable(M value) throws DiffException {

        final Comparator<? super K> c = value.comparator();

        return (c == null)
            ? new ChangeableSortedMap<K,V>()
            : new ChangeableSortedMap<K,V>(c);

    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof LCSSortedMapDiff)) {
            return false;
        } // end of if

        final LCSSortedMapDiff other = (LCSSortedMapDiff) o;

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
     * Change in a sorted map of elements, some new value at specified range.
     *
     * @param K Type of map keys
     * @param V Type of map values
     * @author Cedric Chantepie
     */
    protected class SortedMapChange<K,V> 
        extends Change<SortedMap<K,V>, Map.Entry<K,V>> {

        // --- Properties ---

        /**
         * New value
         */
        private final TreeMap<K,V> value;

        // --- Constructors ---

        /**
         * {@inheritDoc}
         */
        public SortedMapChange(final Range<Integer> range,
                               final Comparator<? super K> comparator) {

            super(range);

            this.value = (comparator == null) 
                ? new TreeMap<K,V>() 
                : new TreeMap<K,V>(comparator);

        } // end of <init>

        // ---

        /**
         * Returns sorted set change at specified range.
         */
        public SortedMap<K,V> getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized <I extends Map.Entry<K,V>> void appendItem(final I item) {
                Validate.notNull(item, "Null item");
                
                this.value.put(item.getKey(), item.getValue());
            } // end of appendItem
    } // end of class SortedMapChange

    /**
     * Working object representing sorted map on which change can be applied.
     *
     * @param K Type of map keys
     * @param V Type of map values
     * @author Cedric Chantepie
     * @see SortedMapChange
     */
    protected class ChangeableSortedMap<K,V> 
        extends Changeable<SortedMap<K,V>, Map.Entry<K,V>> {

        // --- Properties ---

        /**
         * Value we are working on
         */
        private final TreeMap<K,V> value;

        // --- Constructors ---

        /**
         * Bulk constructor.
         */
        public ChangeableSortedMap() {
            this.value = new TreeMap<K,V>();
        } // end of <init>

        /**
         * Comparator constructor.
         */
        public ChangeableSortedMap(final Comparator<? super K> comparator) {
            this.value = new TreeMap<K,V>(comparator);
        } // end of <init>

        // --- Properties accessors ---

        /**
         * Returns value with current changes.
         */
        public SortedMap<K,V> getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized <M extends Map.Entry<K,V>> void append(M element) {
                this.value.put(element.getKey(), element.getValue());
            } // end of append

        /**
         * {@inheritDoc}
         */
        public synchronized void insertChange(Change<SortedMap<K,V>, Map.Entry<K,V>> change) {

                this.value.putAll(change.getValue());
            } // end of insertChange
    } // end of class ChangeableSortedMap
} // end of class LCSSortedMapDiff
