package istory.spi;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for Collection test
 *
 * @author Cedric Chantepie ()
 */
public class LCSCollectionDiffTest extends TestCase {

    /**
     */
    public void testCtorWithSet() {
	try {
	    HashSet orig = new HashSet();
	    HashSet dest = new HashSet();

	    orig.add("x");
	    dest.add("y");

	    new LCSCollectionDiff(orig, dest);
	} catch (Exception e) { /* OK */ }
    } // end of testCtorWithSet
    
    /**
     */
    public void testPatch() {
	Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuPatch(orig, dest);

	orig = new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };
	dest = new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	List origList = Arrays.asList(orig);
	List destList = Arrays.asList(dest);

	tuPatch(origList, destList);
    } // end of testPatch

    /**
     */
    private void tuPatch(final Object[] orig, 
			 final Object[] dest) {

	LCSCollectionDiff diff = 
	    new LCSCollectionDiff(orig, dest);

	Collection patched = null;

	try {
	    patched = (Collection) diff.patch(orig);

	    assertEquals(Arrays.asList(dest), patched);
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

	LCSCollectionDiff diff = 
	    new LCSCollectionDiff(orig, dest);

	Collection patched = null;

	try {
	    patched = (Collection) diff.patch(orig);

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

	List origList = Arrays.asList(orig);
	List destList = Arrays.asList(dest);

	tuRevert(origList, destList);
    } // end of testRevert

    /**
     */
    private void tuRevert(final Object[] orig,
			  final Object[] dest) {

	LCSCollectionDiff diff = 
	    new LCSCollectionDiff(orig, dest);

	Collection reverted = null;

	try {
	    reverted = (Collection) diff.revert(dest);

	    assertEquals(Arrays.asList(orig), reverted);
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

	LCSCollectionDiff diff = 
	    new LCSCollectionDiff(orig, dest);

	Collection reverted = null;

	try {
	    reverted = (Collection) diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class LCSCollectionDiffTest
