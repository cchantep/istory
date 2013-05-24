package istory.spi;

import java.util.Collection;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for unsorted set test
 *
 * @author Cedric Chantepie ()
 */
public class UnsortedSetDiffTest extends TestCase {

    /**
     */
    public void testCtorWithSortedSet() {
	try {
	    TreeSet orig = new TreeSet();
	    TreeSet dest = new TreeSet();

	    orig.add("x");
	    dest.add("y");

	    new UnsortedSetDiff(orig, dest);
	} catch (Exception e) { /* OK */ }
    } // end of testCtorWithSortedSet
    
    /**
     */
    public void testPatch() {
	Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	List origList = Arrays.asList(orig);
	List destList = Arrays.asList(dest);

	tuPatch(new HashSet(origList), new HashSet(destList));

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	origList = Arrays.asList(orig);
	destList = Arrays.asList(dest);

	tuPatch(origList, destList);
    } // end of testPatch

    /**
     */
    private void tuPatch(final Set orig, 
			 final Set dest) {

	UnsortedSetDiff diff = 
	    new UnsortedSetDiff(orig, dest);

	Set patched = null;

	try {
	    patched = (Set) diff.patch(orig);

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
    private void tuPatch(final Collection orig, 
			 final Collection dest) {

	UnsortedSetDiff diff = 
	    new UnsortedSetDiff(orig, dest);

	Set patched = null;

	try {
	    patched = (Set) diff.patch(orig);

	    assertEquals(new HashSet(dest), patched);
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

	List origList = Arrays.asList(orig);
	List destList = Arrays.asList(dest);

	tuRevert(new HashSet(origList), new HashSet(destList));

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	origList = Arrays.asList(orig);
	destList = Arrays.asList(dest);

	tuRevert(origList, destList);
    } // end of testRevert

    /**
     */
    private void tuRevert(final Set orig,
			  final Set dest) {

	UnsortedSetDiff diff = 
	    new UnsortedSetDiff(orig, dest);

	Set reverted = null;

	try {
	    reverted = (Set) diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

    /**
     */
    private void tuRevert(final Collection orig,
			  final Collection dest) {

	UnsortedSetDiff diff = 
	    new UnsortedSetDiff(orig, dest);

	Set reverted = null;

	try {
	    reverted = (Set) diff.revert(dest);

	    assertEquals(new HashSet(orig), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class UnsortedSetDiffTest
