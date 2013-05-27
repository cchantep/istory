package istory;

import java.util.HashMap;
import java.util.Map;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for  map test
 *
 * @author Cedric Chantepie
 */
public class UnsortedMapDiffTest {
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
    private <T> void tuPatchDifferent(final T[] array) {
	final HashMap<T,T> origMap = new HashMap<T,T>();
	final HashMap<T,T> destMap = new HashMap<T,T>();

        for (final T t : array) {
	    origMap.put(t, t);
	} // end of for

	int j = array.length-1;
	for (int i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuPatch(origMap, destMap);
    } // end of tuPatchDifferent

    /**
     */
    private <T> void tuPatchSame(final T[] orig, final T[] dest) {
	final HashMap<T,T> origMap = new HashMap<T,T>();
	final HashMap<T,T> destMap = new HashMap<T,T>();

	for (int i = 0; i < orig.length || i < dest.length; i++) {
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
    private <T> void tuPatch(final Map<T,T> orig, final Map<T,T> dest) {
	final UnsortedMapDiff<T,T> diff = 
            new UnsortedMapDiff<T,T>(orig, dest);

	Map<T,T> patched = null;

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
    private <T> void tuRevertDifferent(final T[] array) {
	final HashMap<T,T> origMap = new HashMap<T,T>();
	final HashMap<T,T> destMap = new HashMap<T,T>();

        for (final T t : array) {
	    origMap.put(t, t);
	} // end of for

	int j = array.length-1;
	for (int i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuRevert(origMap, destMap);
    } // end of tuRevertDifferent

    /**
     */
    private <T> void tuRevertSame(final T[] orig, final T[] dest) {
	final HashMap<T,T> origMap = new HashMap<T,T>();
	final HashMap<T,T> destMap = new HashMap<T,T>();

	for (int i = 0; i < orig.length || i < dest.length; i++) {
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
    private <T> void tuRevert(final Map<T,T> orig, final Map<T,T> dest) {
	final UnsortedMapDiff<T,T> diff = new UnsortedMapDiff<T,T>(orig, dest);

	Map<T,T> reverted = null;

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
} // end of class UnsortedMapDiffTest
