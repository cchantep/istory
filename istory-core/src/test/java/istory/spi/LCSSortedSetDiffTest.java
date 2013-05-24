package istory.spi;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for sorted set test
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedSetDiffTest extends TestCase {
    static final Comparator rcharComp = new Comparator() {
	    public int compare(Object a, Object b) {
		Character cha = (Character) a;
		Character chb = (Character) b;

		if (cha == null && chb == null) {
		    return 0;
		} else if (cha == null && chb != null) {
		    return 1;
		} else if (cha != null && chb == null) {
		    return -1;
		} // end of else

		return chb.compareTo(cha);
	    }
	};


    /**
     */
    public void testPatch() {
	Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuPatch(orig, dest);

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuPatch(orig, dest);
    } // end of testPatch

    /**
     */
    private void tuPatch(final Object[] orig,
			 final Object[] dest) {

	TreeSet origSet = new TreeSet(Arrays.asList(orig));
	TreeSet destSet = new TreeSet(Arrays.asList(dest));

	tuPatch(origSet, destSet);

	origSet = new TreeSet(rcharComp);
	destSet = new TreeSet(rcharComp);

	origSet.addAll(Arrays.asList(orig));
	destSet.addAll(Arrays.asList(dest));

	tuPatch(origSet, destSet);
    } // end of tuPatch

    /**
     */
    private void tuPatch(final SortedSet orig, 
			 final SortedSet dest) {

	LCSSortedSetDiff diff = 
	    new LCSSortedSetDiff(orig, dest);

	SortedSet patched = null;

	try {
	    patched = (SortedSet) diff.patch(orig);

	    assertEquals(dest, patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.patch(patched);
	} catch (DiffException e) { /* OK */ }
    } // end of tuPatch

    /**
     */
    public void testRevert() {
	Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuRevert(orig, dest);

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuRevert(orig, dest);
    } // end of testRevert

    /**
     */
    private void tuRevert(final Object[] orig,
			  final Object[] dest) {

	TreeSet origSet = new TreeSet(Arrays.asList(orig));
	TreeSet destSet = new TreeSet(Arrays.asList(dest));

	tuRevert(origSet, destSet);

	origSet = new TreeSet(rcharComp);
	destSet = new TreeSet(rcharComp);

	origSet.addAll(Arrays.asList(orig));
	destSet.addAll(Arrays.asList(dest));

	tuRevert(origSet, destSet);
    } // end of tuRevert

    /**
     */
    private void tuRevert(final SortedSet orig,
			  final SortedSet dest) {

	LCSSortedSetDiff diff = 
	    new LCSSortedSetDiff(orig, dest);

	SortedSet reverted = null;

	try {
	    reverted = (SortedSet) diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class LCSSortedSetDiffTest
