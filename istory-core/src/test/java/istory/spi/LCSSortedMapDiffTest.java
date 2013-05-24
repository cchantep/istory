package istory.spi;

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

	tuPatchSame(orig, dest);
	tuPatchDifferent(orig);
	tuPatchDifferent(dest);

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuPatchSame(orig, dest);
	tuPatchDifferent(orig);
	tuPatchDifferent(dest);
    } // end of testPatch

    /**
     */
    private void tuPatchDifferent(final Object[] array) {
	TreeMap origMap = new TreeMap();
	TreeMap destMap = new TreeMap();

	int i = 0;
	for (; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	int j = array.length-1;
	for (i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuPatch(origMap, destMap);

	// ---

	origMap = new TreeMap(rcharComp);

	for (i = 0; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	tuPatch(origMap, destMap);
    } // end of tuPatchDifferent

    /**
     */
    private void tuPatchSame(final Object[] orig,
			     final Object[] dest) {

	TreeMap origMap = new TreeMap();
	TreeMap destMap = new TreeMap();

	for (int i = 0; i < orig.length ||
		 i < dest.length; i++) {

	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuPatch(origMap, destMap);

	origMap = new TreeMap(rcharComp);
	destMap = new TreeMap(rcharComp);

	for (int i = 0; i < orig.length ||
		 i < dest.length; i++) {

	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuPatch(origMap, destMap);
    } // end of tuPatchSame

    /**
     */
    private void tuPatch(final SortedMap orig, 
			 final SortedMap dest) {

	LCSSortedMapDiff diff = 
	    new LCSSortedMapDiff(orig, dest);

	SortedMap patched = null;

	try {
	    patched = (SortedMap) diff.patch(orig);

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

	tuRevertSame(orig, dest);
	tuRevertDifferent(orig);
	tuRevertDifferent(dest);

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	tuRevertSame(orig, dest);
	tuRevertDifferent(orig);
	tuRevertDifferent(dest);
    } // end of testRevert

    /**
     */
    private void tuRevertDifferent(final Object[] array) {
	TreeMap origMap = new TreeMap();
	TreeMap destMap = new TreeMap();

	int i = 0;
	for (; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	int j = array.length-1;
	for (i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuRevert(origMap, destMap);

	// ---

	origMap = new TreeMap(rcharComp);

	for (i = 0; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	tuRevert(origMap, destMap);
    } // end of tuRevertDifferent

    /**
     */
    private void tuRevertSame(final Object[] orig,
			      final Object[] dest) {

	TreeMap origMap = new TreeMap();
	TreeMap destMap = new TreeMap();

	for (int i = 0; i < orig.length ||
		 i < dest.length; i++) {

	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuRevert(origMap, destMap);

	origMap = new TreeMap(rcharComp);
	destMap = new TreeMap(rcharComp);

	for (int i = 0; i < orig.length ||
		 i < dest.length; i++) {

	    if (i < orig.length) {
		origMap.put(orig[i], orig[i]);
	    } // end of if

	    if (i < dest.length) {
		destMap.put(dest[i], dest[i]);
	    } // end of if
	} // end of for

	tuRevert(origMap, destMap);
    } // end of tuRevertSame

    /**
     */
    private void tuRevert(final SortedMap orig,
			  final SortedMap dest) {

	LCSSortedMapDiff diff = 
	    new LCSSortedMapDiff(orig, dest);

	SortedMap reverted = null;

	try {
	    reverted = (SortedMap) diff.revert(dest);

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
