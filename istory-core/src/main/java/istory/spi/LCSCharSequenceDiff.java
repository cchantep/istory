package istory.spi;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.math.IntRange;

import istory.DiffException;

/**
 * Diff implementation for char sequence using LCS algorithm.
 *
 * @author Cedric Chantepie ()
 * @todo Remove orig and dest when processed
 */
public class LCSCharSequenceDiff 
    extends AbstractLCSDiff<Character> 
    implements Serializable {

    // --- Properties ---

    /**
     * Original string
     */
    private CharSequence orig = null;

    /**
     * Destination string
     */
    private CharSequence dest = null;

    // --- Constructors ---

    /**
     * Bulk constructor.
     *
     * @param original Original characters
     * @param destination Destination characters
     */
    public LCSCharSequenceDiff(char[] original,
			 char[] destination) {

	this(new String(original),
	     new String(destination));

    } // end of <init>

    /**
     * Bulk constructor.
     *
     * @param original Original string
     * @param destination Destination string
     */
    public LCSCharSequenceDiff(CharSequence original,
			 CharSequence destination) {

	super();

	Validate.notNull(original, "Null original string");
	Validate.notNull(destination, "Null destination string");

	this.orig = original;
	this.dest = destination;
    } // end of <init>

    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    protected int count(Object value) {
	return ((CharSequence) value).length();
    } // end of count

    /**
     * {@inheritDoc}
     */
    protected Character elementAt(Object value, int index) {
	if (!(value instanceof CharSequence)) {
	    throw new IllegalArgumentException("Expected CharSequence");
	} // end of if

	Validate.notNull(value, "Null value");
	Validate.isTrue(index >= 0, "Index less than 0");

	// ---

	CharSequence valStr = (CharSequence) value;

	Validate.isTrue(index < valStr.length(),
			"Index greater than or equals to length");

	return valStr.charAt(index);
    } // end of elementAt

    /**
     * {@inheritDoc}
     */
    public synchronized CharSequence patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (CharSequence) super.patch(orig);
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public synchronized CharSequence revert(Object dest) 
	throws DiffException {

	if (this.removed == null) {
	    processLcs();
	    processDifferences();

	    this.orig = this.dest = null;
	} // end of if

	return (CharSequence) super.revert(dest);
    } // end of revert

    /**
     * {@inheritDoc}
     */
    protected int originalSize() {
	return this.orig.length();
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected int destinationSize() {
	return this.dest.length();
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Character originalElementAt(int index) {
	return this.orig.charAt(index);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Character destinationElementAt(int index) {
	return this.dest.charAt(index);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Change<Character> createChange(IntRange range) {
	return new CharSequenceChange(range);
    } // end of originalSize

    /**
     * {@inheritDoc}
     */
    protected Changeable<Character> createChangeable(Object value) 
	throws DiffException {

	return new ChangeableCharSequence();
    } // end of createChangeable

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof LCSCharSequenceDiff)) {
	    return false;
	} // end of if

	LCSCharSequenceDiff other = (LCSCharSequenceDiff) o;

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
	return new HashCodeBuilder(27, 29).
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
     * Change in string, some new characters at specified range.
     *
     * @author Cedric Chantepie ()
     */
    protected class CharSequenceChange extends Change<Character> {

	// --- Properties ---

	/**
	 * Buffer for new characters
	 */
	private StringBuffer value = null;

	// --- Constructors ---

	/**
	 * {@inheritDoc}
	 */
	public CharSequenceChange(IntRange range) {
	    super(range);
	} // end of <init>

	// ---

	/**
	 * Returns CharSequence change at specified range.
	 */
	public CharSequence getValue() {
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value.toString();
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void appendItem(Character item) {
	    Validate.notNull(item, "Null character");

	    if (this.value == null) {
		this.value = new StringBuffer(1);
	    } // end of if

	    this.value.append(item);
	} // end of appendItem
    } // end of class CharSequenceChange

    /**
     * CharSequence on which we can applied change.
     *
     * @author Cedric Chantepie ()
     * @see CharSequenceChange
     */
    protected class ChangeableCharSequence extends Changeable<Character> {
	// --- Properties ---

	/**
	 * Buffer for CharSequence value on which we work
	 */
	private StringBuffer value = null;

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
	    if (this.value == null) {
		return null;
	    } // end of if

	    return this.value.toString();
	} // end of getValue

	/**
	 * {@inheritDoc}
	 */
	public synchronized void append(Character element) {
	    this.value.append(element.charValue());
	} // end of append

	/**
	 * {@inheritDoc}
	 */
	public synchronized void insertChange(Change<Character> change) {
	    Object val = change.getValue();

	    if (!(val instanceof CharSequence)) {
		throw new IllegalArgumentException("Expected string " +
						   "as change value");

	    } // end of if

	    CharSequence valStr = (CharSequence) val;
	    IntRange r = change.getIndexRange();
	    
	    this.value.
		insert(r.getMinimumInteger(),
		       valStr);

	} // end of insertChange
    } // end of class ChangeableCharSequence
} // end of class LCSCharSequenceDiff
