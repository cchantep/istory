package istory;

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
    static final Comparator<? super Character> rcharComp = 
        new Comparator<Character>() {
        public int compare(final Character cha, final Character chb) {
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
	final Character[] origA = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	final Character[] destA = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuPatch(origA, destA);

	Character[] origB = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	Character[] destB = 
            new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuPatch(origB, destB);
    } // end of testPatch

    /**
     */
    private void tuPatch(final Character[] orig, final Character[] dest) {
	final TreeSet<Character> origSet = 
            new TreeSet<Character>(Arrays.asList(orig));

	final TreeSet<Character> destSet = 
            new TreeSet<Character>(Arrays.asList(dest));

	tuPatch(origSet, destSet);

	final TreeSet<Character> os = new TreeSet<Character>(rcharComp);
	final TreeSet<Character> ds = new TreeSet<Character>(rcharComp);

	os.addAll(Arrays.asList(orig));
	ds.addAll(Arrays.asList(dest));

	tuPatch(os, ds);
    } // end of tuPatch

    /**
     */
    private <T> void tuPatch(final SortedSet<T> orig, final SortedSet<T> dest) {
	final LCSSortedSetDiff<T> diff = 
	    new LCSSortedSetDiff<T>(orig, dest);

	SortedSet<T> patched = null;

	try {
	    patched = diff.patch(orig);

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
	final Character[] origA = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	final Character[] destA = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuRevert(origA, destA);

	final Character[] origB = 
            new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };

	final Character[] destB = 
            new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuRevert(origB, destB);
    } // end of testRevert

    /**
     */
    private void tuRevert(final Character[] orig, final Character[] dest) {
	final TreeSet<Character> origSet = 
            new TreeSet<Character>(Arrays.asList(orig));

	final TreeSet<Character> destSet = 
            new TreeSet<Character>(Arrays.asList(dest));

	tuRevert(origSet, destSet);

	final TreeSet<Character> os = new TreeSet<Character>(rcharComp);
	final TreeSet<Character> ds = new TreeSet<Character>(rcharComp);

	os.addAll(Arrays.asList(orig));
	ds.addAll(Arrays.asList(dest));

	tuRevert(os, ds);
    } // end of tuRevert

    /**
     */
    private <T> void tuRevert(final SortedSet<T> orig, 
                              final SortedSet<T> dest) {

	final LCSSortedSetDiff<T> diff = 
	    new LCSSortedSetDiff<T>(orig, dest);

	SortedSet<T> reverted = null;

	try {
	    reverted = diff.revert(dest);

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
