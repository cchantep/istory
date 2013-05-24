package istory.spi;

import java.lang.reflect.Array;

import java.io.Serializable;

import java.util.ArrayList;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.math.IntRange;

import istory.DiffException;
import istory.Diff;

/**
 * Diff based on LCS algorithm.
 *
 * Inspired from http://www.bioalgorithms.info/downloads/code/LCS.java
 * @author Cedric Chantepie ()
 */
public abstract class AbstractLCSDiff<T> 
    implements Diff {

    // --- Constants ---

    // These are "constants" which indicate a direction 
    // in the backtracking array.

    /**
     * Do not move
     */
    private static final int NEITHER = 0;
    
    /**
     * Go up in in the backtracking
     */
    private static final int UP = 1;

    /**
     * Go left in the backtracking
     */
    private static final int LEFT = 2;

    /**
     * Go up and left in the backtracking
     */
    private static final int UP_AND_LEFT = 3;

    // --- Properties ---

    /**
     * Longest common subsequence
     */
    private CommonElement<T>[] lcs = null;

    /**
     * Changes from lcs to original,
     * so removed from destination
     */
    protected ArrayList<Change<T>> removed = null;

    /**
     * Changes from lcs to destination,
     * so added elements
     */
    protected ArrayList<Change<T>> added = null;

    // ---

    /**
     * Returns the number of element in original (first) container.
     *
     * @see #destinationSize()
     */
    protected abstract int originalSize();

    /**
     * Returns the number of element in destination (second) container.
     *
     * @see #originalSize()
     */
    protected abstract int destinationSize();

    /**
     * Returns the element at |index| in original container.
     *
     * @param index Index in original container
     * @see #destinationElementAt(int)
     */
    protected abstract T originalElementAt(int index);

    /**
     * Returns the element at |index| in destination container.
     *
     * @param index Index in destination container
     * @see #originalElementAt(int)
     */
    protected abstract T destinationElementAt(int index);

    /**
     * Returns true if element |a| and element |b| are equals.
     * Could overidden by subclass for more control.
     *
     * @param a An element abitrarily named "a"
     * @param b An element abitrarily named "b"
     * @see org.apache.commons.lang.builder.EqualsBuilder
     */
    protected boolean areEquals(T a, T b) {
	return new EqualsBuilder().
	    append(a, b).
	    isEquals();

    } // end of areEquals

    /**
     * Process original and destination in order to obtain
     * the longest common subsequence between these two.
     * This is needed to be able to determine changes for
     * original and destination (processDifferences),
     * and so is needed to patch and revert.
     *
     * @see #processDifferences()
     * @see #patch(java.lang.Object)
     * @see #revert(java.lang.Object)
     */
    protected void processLcs() {
	// size
	int n = originalSize();
	int m = destinationSize();

	// checkerboards
	int S[][] = new int[n+1][m+1];
	int R[][] = new int[n+1][m+1];

	int ii;
	int jj;

	// It is important to use <=, not <.
	// The next two for-loops are initialization

	for (ii = 0; ii <= n; ++ii) {
	    S[ii][0] = 0;
	    R[ii][0] = UP;
	} // end of for

	for (jj = 0; jj <= m; ++jj) {
	    S[0][jj] = 0;
	    R[0][jj] = LEFT;
	} // end of for

	// This is the main dynamic programming loop 
	// that computes the score and backtracking arrays.
	T a;
	T b;
	for (ii = 1; ii <= n; ++ii) {
	    a = this.originalElementAt(ii-1);

	    for (jj = 1; jj <= m; ++jj) { 
		b = this.destinationElementAt(jj-1);

		if (this.areEquals(a, b)) { // common
		    // 1 more to the score
		    S[ii][jj] = S[ii-1][jj-1] + 1; 

		    // UP for previous element in original container
		    // LEFT because common element for
		    // previous element in original should be before
		    // current common on in destination container
		    R[ii][jj] = UP_AND_LEFT;
		} else {
		    // propagate score and wait for direction
		    S[ii][jj] = S[ii-1][jj-1] + 0;
		    R[ii][jj] = NEITHER;
		} // end of else

		if (S[ii-1][jj] >= S[ii][jj]) {
		    // common element for current original element
		    // not already found, 
		    // now that is ok so go up to previous element
		    S[ii][jj] = S[ii-1][jj];
		    R[ii][jj] = UP;
		} // end of if

		if (S[ii][jj-1] >= S[ii][jj]) {
		    // common element already found so go it
		    S[ii][jj] = S[ii][jj-1];
		    R[ii][jj] = LEFT;
		} // end of if
	    } // end of for
	} // end of for

	// The length of the longest substring is S[n][m]
	ii = n; 
	jj = m;
	int pos = S[ii][jj] - 1;

	this.lcs = (CommonElement<T>[]) Array.
	    newInstance(CommonElement.class, pos+1);

	// Trace the backtracking matrix.
	while (ii > 0 || jj > 0) {
	    if (R[ii][jj] == UP_AND_LEFT) {
		ii--;
		jj--;

		this.lcs[pos--] = 
		    new CommonElement<T>(this.originalElementAt(ii),
					 ii, jj);

		continue; 
	    } // end of if

	    if (R[ii][jj] == UP) {
		ii--;
	    } else if (R[ii][jj] == LEFT) {
		jj--;
	    } // end of else if
	} // end of while
    } // end of processLcs

    /**
     * Create a change instance.
     *
     * @param range Initial change range
     */
    protected abstract Change<T> createChange(IntRange range);

    /**
     * Process original and destination according lcs, 
     * that must have beeing obtained previously (processLcs),
     * in order to determine changes from lcs to original (removed)
     * and changes from lcs to destination (added). All this is needed
     * to be able to patch and revert
     *
     * @see #processLcs()
     * @see #patch(java.lang.Object)
     * @see #revert(java.lang.Object)
     */
    protected void processDifferences() {
	if (this.lcs == null) {
	    throw new IllegalStateException("Should have called " +
					    "processLcs before");

	} // end of if

	this.removed = new ArrayList<Change<T>>();

	int len = this.originalSize();
	int c = 0;
	int idx = (this.lcs.length != 0)  
	    ? this.lcs[c].getOriginalIndex()
	    : -1;
	int last = -1;
	IntRange r = null;
	Change<T> change = null;

	for (int i = 0; i < len; i++) {
	    if (i == idx) {
		if (++c < this.lcs.length) {
		    idx = this.lcs[c].getOriginalIndex();
		} // end of if

		continue;
	    } // end of if

	    // ---

	    if (i != (last+1) && change != null) {
		this.removed.add(change);

		change = null;
		r = null;
	    } // end of

	    if (change == null) { // new range
		change = this.createChange(r = new IntRange(i, i));
	    } else { // complete range
		r = new IntRange(r.getMinimumInteger(), i);

		change.setIndexRange(r);
	    } // end of else

	    change.appendItem(this.originalElementAt(i));

	    last = i;
	} // end of for

	if (change != null) { // remaining change at end of loop
	    this.removed.add(change);
	} // end of if

	// ---

	this.added = new ArrayList<Change<T>>();

	len = this.destinationSize();
	c = 0;
	idx = (this.lcs.length != 0)  
	    ? this.lcs[c].getDestinationIndex()
	    : -1;
	last = -1;
	change = null;
	r = null;

	for (int i = 0; i < len; i++) {
	    if (i == idx) {
		if (++c < this.lcs.length) {
		    idx = this.lcs[c].getDestinationIndex();
		} // end of if

		continue;
	    } // end of if

	    // ---

	    if (i != (last+1) && change != null) {
		this.added.add(change);

		r = null;
		change = null;
	    } // end of

	    if (r == null) { // new range
		change = this.createChange(r = new IntRange(i, i));
	    } else { // complete range
		r = new IntRange(r.getMinimumInteger(), i);

		change.setIndexRange(r);
	    } // end of else

	    change.appendItem(this.destinationElementAt(i));

	    last = i;
	} // end of for

	if (change != null) { // remaining change at end of loop
	    this.added.add(change);
	} // end of if

	this.lcs = null;
    } // end of processDifferences

    /**
     * Create a changeable value.
     *
     * @param value Base value to be patched or reverted
     */
    protected abstract Changeable<T> createChangeable(Object value)
	throws DiffException;

    /**
     * Returns element at |index| in |value|.
     *
     * @param value Value in which element is search at |index|
     * @param index Expected index of element to returned in |value|
     */
    protected abstract T elementAt(Object value, int index);

    /**
     * Returns number of elements in |value|.
     *
     * @param value Counted value
     * @throws ClassCastException if value is not of an accepted type
     */
    protected abstract int count(Object value);

    /**
     * {@inheritDoc}
     */
    public Object patch(Object orig) 
	throws DiffException {

	if (this.added == null) {
	    throw new IllegalStateException("processDifferences " +
					    "before");

	} // end of if

	if (this.removed == null) {
	    throw new IllegalStateException("processDifferences " +
					    "before");

	} // end of if

	Validate.notNull(orig, "Object to patch is null");

	// ---

	Changeable<T> val = this.createChangeable(orig);
	int len = this.count(orig);
	int ridx = 0;
	int rlen = this.removed.size();
	int alen = this.added.size();
	IntRange r = (rlen > 0) 
	    ? this.removed.get(ridx).getIndexRange() : null;
	int rmax = (r != null) ? r.getMaximumInteger() : -2;
	int aidx = 0;
	Change<T> change = (alen > 0) 
	    ? this.added.get(aidx) : null;
	int amin = (change != null) 
	    ? change.getIndexRange().getMinimumInteger() : -2;
	int cidx = -1; // before first index

	for (int i = 0; i < len; i++) {
	    if (r == null || !r.containsInteger(i)) {
		// Append common element i
		val.append(this.elementAt(orig, i));

		cidx++; // wait next common element
	    } // end of if

	    if (cidx == amin) { 
		// Insert change at current common element index
		val.insertChange(change);

		if (++aidx < alen) { // go to next change
		    change = this.added.get(aidx);
		    amin = change.getIndexRange().getMinimumInteger();
		} else {
		    change = null;
		    amin = -1;
		} // end of else
	    } // end of if

	    if (i == rmax) { 
		// going out from current remove range ...
		if (++ridx < rlen) { 
		    // ... so try to go to next one
		    r = this.removed.get(ridx).getIndexRange();
		    rmax = r.getMaximumInteger();
		} else {
		    r = null;
		    rmax = -1;
		} // end of else
	    } // end of if
	} // end of for

	if (change != null) { // remaining change at end
	    val.insertChange(change);
	} // end of if

	return val.getValue();
    } // end of patch

    /**
     * {@inheritDoc}
     */
    public Object revert(Object dest)
	throws DiffException {

	if (this.added == null) {
	    throw new IllegalStateException("processDifferences " +
					    "before");

	} // end of if

	if (this.removed == null) {
	    throw new IllegalStateException("processDifferences " +
					    "before");

	} // end of if

	Validate.notNull(dest, "Object to revert is null");

	// ---

	Changeable<T> val = this.createChangeable(dest);
	int len = this.count(dest);
	int ridx = 0;
	int rlen = this.added.size();
	int alen = this.removed.size();
	IntRange r = (rlen > 0) 
	    ? this.added.get(ridx).getIndexRange() : null;
	int rmax = (r != null) ? r.getMaximumInteger() : -2;
	int aidx = 0;
	Change<T> change = (alen > 0) 
	    ? this.removed.get(aidx) : null;
	int amin = (change != null) 
	    ? change.getIndexRange().getMinimumInteger() : -2;
	int cidx = -1; // before first index

	for (int i = 0; i < len; i++) {
	    if (r == null || !r.containsInteger(i)) {
		// Append common element i
		val.append(this.elementAt(dest, i));

		cidx++; // wait next common element
	    } // end of if

	    if (cidx == amin) { 
		// Insert change at current common element index
		val.insertChange(change);

		if (++aidx < alen) { // go to next change
		    change = this.removed.get(aidx);
		    amin = change.getIndexRange().getMinimumInteger();
		} else {
		    change = null;
		    amin = -1;
		} // end of else
	    } // end of if

	    if (i == rmax) { 
		// going out from current remove range ...
		if (++ridx < rlen) { 
		    // ... so try to go to next one
		    r = this.added.get(ridx).getIndexRange();
		    rmax = r.getMaximumInteger();
		} else {
		    r = null;
		    rmax = -1;
		} // end of else
	    } // end of if
	} // end of for

	if (change != null) { // remaining change at end
	    val.insertChange(change);
	} // end of if

	return val.getValue();
    } // end of revert

    /**
     * {@inheritDoc}
     * @throws ClassCastException if |o| is not a 
     * LCS diff implementation. Subclass should add an instanceof 
     * test before calling this super equals.
     */
    public boolean equals(Object o) {
	if (o == null) {
	    return false;
	} // end of if

	AbstractLCSDiff other = (AbstractLCSDiff) o;

	return new EqualsBuilder().
	    append(this.removed, other.removed).
	    append(this.lcs, other.lcs).
	    append(this.added, other.added).
	    isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return new HashCodeBuilder(25, 27).
	    append(this.removed).
	    append(this.lcs).
	    append(this.added).
	    toHashCode();

    } // end of hashCode
	
    /**
     * {@inheritDoc}
     */
    public String toString() {
	return new ToStringBuilder(this).
	    append("removed", this.removed).
	    append("lcs", this.lcs).
	    append("added", this.added).
	    toString();

    } // end of toString

    // --- Inner class ---

    /**
     * Element present in both original and destination.
     *
     * @author Cedric Chantepie ()
     */
    protected class CommonElement<T> implements Serializable {
	// --- Properties ---

	/**
	 * Value in common
	 */
	private T value = null;

	/**
	 * Index of value in original (first) container
	 * [-1]
	 */
	private int origIndex = -1;

	/**
	 * Index of value in destination (second) container
	 */
	private int destIndex = -1;

	// --- Constructors ---

	/**
	 * Bulk constructor
	 *
	 * @param value
	 * @param origIndex
	 * @param destIndex
	 */
	public CommonElement(T value,
			     int origIndex,
			     int destIndex) {

	    this.value = value;
	    this.origIndex = origIndex;
	    this.destIndex = destIndex;
	} // end of <init>
	
	// --- Properties accessors ---
	
	/**
	 * Returns element value.
	 */
	public T getValue() {
	    return this.value;
	} // end of getValue

	/**
	 * Returns index of value in original container.
	 *
	 * @see #getDestinationIndex()
	 */
	public int getOriginalIndex() {
	    return this.origIndex;
	} // end of getOriginalIndex

	/**
	 * Returns index of value in destination container.
	 *
	 * @see #getOriginalIndex()
	 */
	public int getDestinationIndex() {
	    return this.destIndex;
	} // end of getDestinationIndex

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof CommonElement)) {
		return false;
	    } // end of if

	    CommonElement other = (CommonElement) o;

	    return new EqualsBuilder().
		append(this.value, other.value).
		append(this.origIndex, other.origIndex).
		append(this.destIndex, other.destIndex).
		isEquals();

	} // end of equals

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
	    return new HashCodeBuilder(7, 9).
		append(this.value).
		append(this.origIndex).
		append(this.destIndex).
		toHashCode();

	} // end of hashCode

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
	    return new ToStringBuilder(this).
		append("value", this.value).
		append("originalIndex", this.origIndex).
		append("destinationIndex", this.destIndex).
		toString();

	} // end of toString
    } // end of class CommonElement

    /**
     * Change from lcs, to original or destination, 
     * at specified index range.
     *
     * @author Cedric Chantepie ()
     */
    protected abstract class Change<T> implements Serializable {
	// --- Properties ---

	/**
	 * Index range in original or destination.
	 */
	private IntRange indexRange = null;

	// --- Constructors ---

	/**
	 * Range constructor.
	 *
	 * @param range Initial index range
	 */
	public Change(IntRange range) {
	    this.indexRange = range;
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Sets new index |range|.
	 *
	 * @param range Index range for this change
	 * @see #getIndexRange()
	 */
	public void setIndexRange(IntRange range) {
	    this.indexRange = range;
	} // end of setIndexRange

	/**
	 * Returns index range.
	 *
	 * @see #setIndexRange(org.apache.commons.lang.math.IntRange)
	 */
	public IntRange getIndexRange() {
	    return this.indexRange;
	} // end of getIndexRange

	/**
	 * Returns global value for all change 
	 * items covered by range.
	 *
	 * @see #appendItem(T)
	 */
	public abstract Object getValue();

	/**
	 * Appends a change item.
	 *
	 * @param item Item to be appended to change value
	 * @see #getValue()
	 */
	public abstract void appendItem(T item);

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof Change)) {
		return false;
	    } // end of if

	    Change other = (Change) o;

	    return new EqualsBuilder().
		append(this.indexRange, other.indexRange).
		append(this.getValue(), other.getValue()).
		isEquals();

	} // end of equals

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
	    return new HashCodeBuilder(9, 11).
		append(this.indexRange).
		append(this.getValue()).
		toHashCode();

	} // end of hashCode

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
	    return new ToStringBuilder(this).
		append("indexRange", this.indexRange).
		append("value", this.getValue()).
		toString();

	} // end of toString
    } // end of class Change

    /**
     * Changeable value we can work with to apply determined changes.
     *
     * @author Cedric Chantepie ()
     */
    public abstract class Changeable<T> implements Serializable {
	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 *
	 * @see #append(T)
	 * @see #insertChange(Change<T>)
	 */
	public abstract Object getValue();

	/**
	 * Append element to value.
	 *
	 * @param element Element to be appended
	 * @see #getValue()
	 */
	public abstract void append(T element);

	/**
	 * Insert |change| into value.
	 *
	 * @param change Change to be applied, 
	 * inserted into current value
	 * @see #getValue()
	 */
	public abstract void insertChange(Change<T> change);

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof Changeable)) {
		return false;
	    } // end of if

	    Changeable other = (Changeable) o;

	    return new EqualsBuilder().
		append(this.getValue(), other.getValue()).
		isEquals();

	} // end of equals

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
	    return new HashCodeBuilder(11, 13).
		append(this.getValue()).
		toHashCode();

	} // end of hashCode

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
	    return new ToStringBuilder(this).
		append("value", this.getValue()).
		toString();

	} // end of toString
    } // end of class Changeable
} // end of class AbstractLCSDiff
