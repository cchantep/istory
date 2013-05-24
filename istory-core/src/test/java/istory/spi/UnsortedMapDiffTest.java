package istory.spi;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for  map test
 *
 * @author Cedric Chantepie ()
 */
public class UnsortedMapDiffTest extends TestCase {
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
	HashMap origMap = new HashMap();
	HashMap destMap = new HashMap();

	int i = 0;
	for (; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	int j = array.length-1;
	for (i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuPatch(origMap, destMap);
    } // end of tuPatchDifferent

    /**
     */
    private void tuPatchSame(final Object[] orig,
			     final Object[] dest) {

	HashMap origMap = new HashMap();
	HashMap destMap = new HashMap();

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
    private void tuPatch(final Map orig, 
			 final Map dest) {

	UnsortedMapDiff diff = 
	    new UnsortedMapDiff(orig, dest);

	Map patched = null;

	try {
	    patched = (Map) diff.patch(orig);

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
	HashMap origMap = new HashMap();
	HashMap destMap = new HashMap();

	int i = 0;
	for (; i < array.length; i++) {
	    origMap.put(array[i], array[i]);
	} // end of for

	int j = array.length-1;
	for (i = 0; i < array.length && j >= 0; i++, j--) {
	    destMap.put(array[i], array[j]);
	} // end of for

	tuRevert(origMap, destMap);
    } // end of tuRevertDifferent

    /**
     */
    private void tuRevertSame(final Object[] orig,
			      final Object[] dest) {

	HashMap origMap = new HashMap();
	HashMap destMap = new HashMap();

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
    private void tuRevert(final Map orig,
			  final Map dest) {

	UnsortedMapDiff diff = 
	    new UnsortedMapDiff(orig, dest);

	Map reverted = null;

	try {
	    reverted = (Map) diff.revert(dest);

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
