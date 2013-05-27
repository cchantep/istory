package istory;

import java.io.Serializable;

import java.util.ArrayList;

import org.apache.commons.lang3.Validate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.Range;

/**
 * Diff based on LCS algorithm.
 * Inspired from http://www.bioalgorithms.info/downloads/code/LCS.java
 *
 * @param C Collection/Sequence type
 * @param E Type of element(s) contained by the collection
 * @author Cedric Chantepie
 */
public abstract class AbstractLCSDiff<C,E> implements Diff<C> {

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
    private ArrayList<CommonElement<E>> lcs = null;

    /**
     * Changes from lcs to original,
     * so removed from destination
     */
    protected ArrayList<Change<C,E>> removed = null;

    /**
     * Changes from lcs to destination,
     * so added elements
     */
    protected ArrayList<Change<C,E>> added = null;

    // ---

    /**
     * Returns the number of element in original (first) container.
     *
     * @see #destinationSize
     */
    protected abstract int originalSize();

    /**
     * Returns the number of element in destination (second) container.
     *
     * @see #originalSize
     */
    protected abstract int destinationSize();

    /**
     * Returns the element at |index| in original container.
     *
     * @param index Index in original container
     * @see #destinationElementAt(int)
     */
    protected abstract E originalElementAt(int index);

    /**
     * Returns the element at |index| in destination container.
     *
     * @param index Index in destination container
     * @see #originalElementAt(int)
     */
    protected abstract E destinationElementAt(int index);

    /**
     * Returns true if element |a| and element |b| are equals.
     * Could overidden by subclass for more control.
     *
     * @param a Element A
     * @param b Element B
     * @see org.apache.commons.lang3.builder.EqualsBuilder
     */
    protected <A extends E, B extends E> boolean areEquals(final A a, 
                                                           final B b) {

	return new EqualsBuilder().append(a, b).isEquals();
    } // end of areEquals

    /**
     * Process original and destination in order to obtain
     * the longest common subsequence between these two.
     * This is needed to be able to determine changes for
     * original and destination (processDifferences),
     * and so is needed to patch and revert.
     *
     * @see #processDifferences
     * @see #patch
     * @see #revert
     */
    protected void processLcs() {
	// size
	final int n = originalSize();
	final int m = destinationSize();

	// checkerboards
	final int S[][] = new int[n+1][m+1];
	final int R[][] = new int[n+1][m+1];

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
	E a;
	E b;
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

	this.lcs = new ArrayList<CommonElement<E>>();

        for (int i = 0; i < pos+1; i++) {
            this.lcs.add(null);
        } // end of for

	// Trace the backtracking matrix.
	while (ii > 0 || jj > 0) {
	    if (R[ii][jj] == UP_AND_LEFT) {
		ii--;
		jj--;

		this.lcs.set(pos--,
                             new CommonElement<E>(this.originalElementAt(ii), 
                                                  ii, jj));

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
    protected abstract Change<C,E> createChange(Range<Integer> range);

    /**
     * Process original and destination according lcs, 
     * that must have beeing obtained previously (processLcs),
     * in order to determine changes from lcs to original (removed)
     * and changes from lcs to destination (added). All this is needed
     * to be able to patch and revert
     *
     * @see #processLcs()
     * @see #patch
     * @see #revert
     */
    protected void processDifferences() {
	if (this.lcs == null) {
	    throw new IllegalStateException("Should have called " +
					    "processLcs before");

	} // end of if

	this.removed = new ArrayList<Change<C,E>>();

	int len = this.originalSize();
	int c = 0;
	int idx = (this.lcs.isEmpty()) ? -1 
            : this.lcs.get(c).getOriginalIndex();

	int last = -1;
	Range<Integer> r = null;
	Change<C,E> change = null;

	for (int i = 0; i < len; i++) {
	    if (i == idx) {
		if (++c < this.lcs.size()) {
		    idx = this.lcs.get(c).getOriginalIndex();
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
		change = this.createChange(r = Range.between(i, i));
	    } else { // complete range
		r = Range.between(r.getMinimum(), i);

		change.setIndexRange(r);
	    } // end of else

	    change.appendItem(this.originalElementAt(i));

	    last = i;
	} // end of for

	if (change != null) { // remaining change at end of loop
	    this.removed.add(change);
	} // end of if

	// ---

	this.added = new ArrayList<Change<C,E>>();

	len = this.destinationSize();
	c = 0;
	idx = (this.lcs.isEmpty()) ? -1
	    : this.lcs.get(c).getDestinationIndex();

	last = -1;
	change = null;
	r = null;

	for (int i = 0; i < len; i++) {
	    if (i == idx) {
		if (++c < this.lcs.size()) {
		    idx = this.lcs.get(c).getDestinationIndex();
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
		change = this.createChange(r = Range.between(i, i));
	    } else { // complete range
                r = Range.between(r.getMinimum(), i);

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
    protected abstract <V extends C> Changeable<C,E> createChangeable(V value)
	throws DiffException;

    /**
     * Returns element at |index| in |value|.
     *
     * @param value Value in which element is search at |index|
     * @param index Expected index of element to returned in |value|
     */
    protected abstract <V extends C> E elementAt(V value, int index);

    /**
     * Returns number of elements in |value|.
     *
     * @param value Counted value
     */
    protected abstract <V extends C> int count(V value);

    /**
     * {@inheritDoc}
     */
    public <V extends C> C patch(final V orig) throws DiffException {
	if (this.added == null) {
	    throw new IllegalStateException("processDifferences before");
	} // end of if

	if (this.removed == null) {
	    throw new IllegalStateException("processDifferences before");
	} // end of if

	Validate.notNull(orig, "Object to patch is null");

	// ---

	final Changeable<C,E> val = this.createChangeable(orig);
	final int len = this.count(orig);
	int ridx = 0;
	final int rlen = this.removed.size();
	final int alen = this.added.size();
	Range<Integer> r = (rlen > 0) 
	    ? this.removed.get(ridx).getIndexRange() : null;
	int rmax = (r != null) ? r.getMaximum() : -2;
	int aidx = 0;
	Change<C,E> change = (alen > 0) 
	    ? this.added.get(aidx) : null;
	int amin = (change != null) 
	    ? change.getIndexRange().getMinimum() : -2;
	int cidx = -1; // before first index

	for (int i = 0; i < len; i++) {
	    if (r == null || !r.contains(i)) {
		// Append common element i
		val.append(this.elementAt(orig, i));

		cidx++; // wait next common element
	    } // end of if

	    if (cidx == amin) { 
		// Insert change at current common element index
		val.insertChange(change);

		if (++aidx < alen) { // go to next change
		    change = this.added.get(aidx);
		    amin = change.getIndexRange().getMinimum();
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
		    rmax = r.getMaximum();
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
    public <V extends C> C revert(final V dest) throws DiffException {
	if (this.added == null) {
	    throw new IllegalStateException("processDifferences before");
	} // end of if

	if (this.removed == null) {
	    throw new IllegalStateException("processDifferences before");
	} // end of if

	Validate.notNull(dest, "Object to revert is null");

	// ---

	final Changeable<C,E> val = this.createChangeable(dest);
	final int len = this.count(dest);
	int ridx = 0;
	final int rlen = this.added.size();
	final int alen = this.removed.size();
	Range<Integer> r = (rlen > 0) 
	    ? this.added.get(ridx).getIndexRange() : null;
	int rmax = (r != null) ? r.getMaximum() : -2;
	int aidx = 0;
	Change<C,E> change = (alen > 0) 
	    ? this.removed.get(aidx) : null;
	int amin = (change != null) 
	    ? change.getIndexRange().getMinimum() : -2;
	int cidx = -1; // before first index

	for (int i = 0; i < len; i++) {
	    if (r == null || !r.contains(i)) {
		// Append common element i
		val.append(this.elementAt(dest, i));

		cidx++; // wait next common element
	    } // end of if

	    if (cidx == amin) { 
		// Insert change at current common element index
		val.insertChange(change);

		if (++aidx < alen) { // go to next change
		    change = this.removed.get(aidx);
		    amin = change.getIndexRange().getMinimum();
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
		    rmax = r.getMaximum();
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
    public boolean equals(final Object o) {
	if (o == null) {
	    return false;
	} // end of if

	final AbstractLCSDiff other = (AbstractLCSDiff) o;

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
     * @author Cedric Chantepie
     */
    protected class CommonElement<T> implements Serializable {
	// --- Properties ---

	/**
	 * Value in common
	 */
	private final T value;

	/**
	 * Index of value in original (first) container
	 */
	private final int origIndex;

	/**
	 * Index of value in destination (second) container
	 */
	private final int destIndex;

	// --- Constructors ---

	/**
	 * Bulk constructor
	 *
	 * @param v Value
	 * @param oi Original index
	 * @param di Destination index
	 */
	public CommonElement(final T v, final int oi, final int di) {
	    this.value = v;
	    this.origIndex = oi;
	    this.destIndex = di;
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
	 * @see #getDestinationIndex
	 */
	public int getOriginalIndex() {
	    return this.origIndex;
	} // end of getOriginalIndex

	/**
	 * Returns index of value in destination container.
	 *
	 * @see #getOriginalIndex
	 */
	public int getDestinationIndex() {
	    return this.destIndex;
	} // end of getDestinationIndex

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(final Object o) {
	    if (o == null || !(o instanceof CommonElement)) {
		return false;
	    } // end of if

	    final CommonElement other = (CommonElement) o;

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
            return String.
                format("CommonElement(%s, %s, %s)",
                       this.value, this.origIndex, this.destIndex);

	} // end of toString
    } // end of class CommonElement

    /**
     * Change from lcs, to original or destination, 
     * at specified index range.
     *
     * @param C Collection/sequence type
     * @param E Type of collection/sequence elements
     * @author Cedric Chantepie
     */
    protected abstract class Change<C,E> implements Serializable {
	// --- Properties ---

	/**
	 * Index range in original or destination
	 */
	private Range<Integer> indexRange = null;

	// --- Constructors ---

	/**
	 * Range constructor.
	 *
	 * @param range Initial index range
	 */
	public Change(final Range<Integer> range) {
	    this.indexRange = range;
	} // end of <init>

	// --- Properties accessors ---

	/**
	 * Sets new index |range|.
	 *
	 * @param range Index range for this change
	 * @see #getIndexRange
	 */
	public void setIndexRange(final Range<Integer> range) {
	    this.indexRange = range;
	} // end of setIndexRange

	/**
	 * Returns index range.
	 *
	 * @see #setIndexRange(org.apache.commons.lang3.Range)
	 */
	public Range<Integer> getIndexRange() {
	    return this.indexRange;
	} // end of getIndexRange

	/**
	 * Returns global value for all change 
	 * items covered by range.
	 *
	 * @see #appendItem
	 */
	public abstract C getValue();

	/**
	 * Appends a change item.
	 *
	 * @param item Item to be appended to change value
	 * @see #getValue
	 */
	public abstract <V extends E> void appendItem(V item);

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(final Object o) {
	    if (o == null || !(o instanceof Change)) {
		return false;
	    } // end of if

            @SuppressWarnings("unchecked")
            final Change<C,E> other = (Change<C,E>) o;

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
     * @param C Collection/sequence type
     * @param E Type of collection/sequence elements
     * @author Cedric Chantepie
     */
    public abstract class Changeable<C,E> implements Serializable {
	// --- Properties accessors ---

	/**
	 * Returns value with current changes.
	 *
	 * @see #append
	 * @see #insertChange
	 */
	public abstract C getValue();

	/**
	 * Append element to value.
	 *
	 * @param element Element to be appended
	 * @see #getValue
	 */
	public abstract <V extends E> void append(V element);

	/**
	 * Insert |change| into value.
	 *
	 * @param change Change to be applied, 
	 * inserted into current value
	 * @see #getValue()
	 */
	public abstract void insertChange(Change<C,E> change);

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(final Object o) {
	    if (o == null || !(o instanceof Changeable)) {
		return false;
	    } // end of if

            @SuppressWarnings("unchecked")
            final Changeable<C,E> other = (Changeable<C,E>) o;

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
