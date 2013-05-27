package istory;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for sorted map test
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedMapDiffTest extends TestCase {
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

	tuPatchSame(origA, destA);
	tuPatchDifferent(origA);
	tuPatchDifferent(destA);

	final Character[] origB = 
            new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };

	final Character[] destB = 
            new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuPatchSame(origB, destB);
	tuPatchDifferent(origB);
	tuPatchDifferent(destB);
    } // end of testPatch

    /**
     */
    private void tuPatchDifferent(final Character[] array) {
	final TreeMap<Character,Character> origMap = 
            new TreeMap<Character,Character>();

	final TreeMap<Character,Character> destMap = 
            new TreeMap<Character,Character>();

        for (final Character c : array) {
	    origMap.put(c, c);
	} // end of for

	int j = array.length-1;
	for (int i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuPatch(origMap, destMap);

	// ---

	final TreeMap<Character,Character> om = 
            new TreeMap<Character,Character>(rcharComp);

        for (final Character c : array) {
            om.put(c, c);
	} // end of for

	tuPatch(origMap, destMap);
    } // end of tuPatchDifferent

    /**
     */
    private void tuPatchSame(final Character[] orig, final Character[] dest) {
	final TreeMap<Character,Character> origMap = 
            new TreeMap<Character,Character>();

	final TreeMap<Character,Character> destMap = 
            new TreeMap<Character,Character>();

	for (int i = 0; i < orig.length || i < dest.length; i++) {
	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuPatch(origMap, destMap);

	final TreeMap<Character,Character> om = 
            new TreeMap<Character,Character>(rcharComp);

	final TreeMap<Character,Character> dm = 
            new TreeMap<Character,Character>(rcharComp);

	for (int i = 0; i < orig.length || i < dest.length; i++) {
	    if (i < orig.length) {
		om.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		dm.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuPatch(om, dm);
    } // end of tuPatchSame

    /**
     */
    private <T> void tuPatch(final SortedMap<T,T> orig, 
                             final SortedMap<T,T> dest) {

	final LCSSortedMapDiff<T,T> diff = 
	    new LCSSortedMapDiff<T,T>(orig, dest);

	SortedMap<T,T> patched = null;

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

	tuRevertSame(origA, destA);
	tuRevertDifferent(origA);
	tuRevertDifferent(destA);

	final Character[] origB = 
            new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };

	final Character[] destB = 
            new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuRevertSame(origB, destB);
	tuRevertDifferent(origB);
	tuRevertDifferent(destB);
    } // end of testRevert

    /**
     */
    private void tuRevertDifferent(final Character[] array) {
	final TreeMap<Character,Character> origMap = 
            new TreeMap<Character,Character>();

	final TreeMap<Character,Character> destMap = 
            new TreeMap<Character,Character>();

        for (final Character c : array) {
            origMap.put(c, c);
	} // end of for

	int j = array.length-1;
	for (int i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuRevert(origMap, destMap);

	// ---

	final TreeMap<Character,Character> om = 
            new TreeMap<Character,Character>(rcharComp);

        for (final Character c : array) {
	    om.put(c, c);
	} // end of for

	tuRevert(om, destMap);
    } // end of tuRevertDifferent

    /**
     */
    private void tuRevertSame(final Character[] orig, final Character[] dest) {
	final TreeMap<Character,Character> origMap = 
            new TreeMap<Character,Character>();

	final TreeMap<Character,Character> destMap = 
            new TreeMap<Character,Character>();

	for (int i = 0; i < orig.length || i < dest.length; i++) {
	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuRevert(origMap, destMap);

	final TreeMap<Character,Character> om = 
            new TreeMap<Character,Character>(rcharComp);

	final TreeMap<Character,Character> dm = 
            new TreeMap<Character,Character>(rcharComp);

	for (int i = 0; i < orig.length ||
		 i < dest.length; i++) {

	    if (i < orig.length) {
		om.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		dm.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuRevert(om, dm);
    } // end of tuRevertSame

    /**
     */
    private <T> void tuRevert(final SortedMap<T,T> orig,
                              final SortedMap<T,T> dest) {

	final LCSSortedMapDiff<T,T> diff = 
	    new LCSSortedMapDiff<T,T>(orig, dest);

	SortedMap<T,T> reverted = null;

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
} // end of class LCSSortedMapDiffTest
