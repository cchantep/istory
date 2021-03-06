package istory;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;

/**
 * Diff implementation for char sequence using LCS algorithm.
 *
 * @author Cedric Chantepie
 */
public class LCSCharSequenceDiff 
    extends AbstractLCSDiff<CharSequence,Character> implements Serializable {

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original string
     * @param destination Destination string
     */
    public LCSCharSequenceDiff(final CharSequence original,
                               final CharSequence destination) {

        super();

        Validate.notNull(original, "Null original string");
        Validate.notNull(destination, "Null destination string");

        processLcs(original, destination);
        processDifferences(original, destination);
    } // end of <init>

    /**
     * Bulk constructor.
     *
     * @param original Original characters
     * @param destination Destination characters
     */
    public LCSCharSequenceDiff(final char[] original, 
                               final char[] destination) {

        this(new String(original), new String(destination));
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(final CharSequence value) {
        return value.length();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected Character elementAt(final CharSequence value, final int index) {
        Validate.notNull(value, "Null value");
        Validate.isTrue(index >= 0, "Index less than 0");

        // ---

        Validate.isTrue(index < value.length(),
                        "Index greater than or equals to length");

        return value.charAt(index);
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends CharSequence> CharSequence patch(final V orig) throws DiffException {

            return super.patch(orig);
        } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized <V extends CharSequence> CharSequence revert(final V dest) throws DiffException {

            return super.revert(dest);
        } // end of revert

    /**
     * {@inheritDoc}
     */
    protected Change<CharSequence,Character> createChange(final CharSequence original, final Range<Integer> range) {

        return new CharSequenceChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<CharSequence,Character> createChangeable(final CharSequence container) throws DiffException {

        return new ChangeableCharSequence();
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof LCSCharSequenceDiff)) {
            return false;
        } // end of if

        final LCSCharSequenceDiff other = (LCSCharSequenceDiff) o;

        return new EqualsBuilder().
            appendSuper(super.equals(other)).
            isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return new HashCodeBuilder(27, 29).
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
     * Change in string, some new characters at specified range.
     *
     * @author Cedric Chantepie
     */
    protected class CharSequenceChange extends Change<CharSequence,Character> {

        // --- Properties ---

        /**
         * Buffer for new characters
         */
        private final StringBuffer value = new StringBuffer();

        // --- Constructors ---

        public CharSequenceChange(final Range<Integer> range) {
            super(range);
        } // end of <init>

        // ---

        /**
         * Returns CharSequence change at specified range.
         */
        public CharSequence getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized void appendItem(final Character item) {
                Validate.notNull(item, "Null character");

                this.value.append(item);
            } // end of appendItem
    } // end of class CharSequenceChange

    /**
     * CharSequence on which we can applied change.
     *
     * @author Cedric Chantepie
     * @see CharSequenceChange
     */
    protected class ChangeableCharSequence extends Changeable<CharSequence,Character> {
        // --- Properties ---

        /**
         * Buffer for CharSequence value on which we work
         */
        private final StringBuffer value;

        // --- Constructors ---

        /**
         * Bulk constructor.
         */
        public ChangeableCharSequence() {
            this.value = new StringBuffer();
        } // end of <init>

        // --- Properties accessors ---

        /**
         * Returns value with current changes.
         */
        public CharSequence getValue() {
            return this.value;
        } // end of getValue

        /**
         * {@inheritDoc}
         */
        public synchronized void append(final Character element) {
                this.value.append(element);
            } // end of append

        /**
         * {@inheritDoc}
         */
        public synchronized void insertChange(final Change<CharSequence,Character> change) {

                this.value.
                    insert(change.getIndexRange().getMinimum(),
                           change.getValue());

            } // end of insertChange
    } // end of class ChangeableCharSequence
} // end of class LCSCharSequenceDiff
