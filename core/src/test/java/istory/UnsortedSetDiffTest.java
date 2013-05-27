package istory;

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
	    final TreeSet<String> orig = new TreeSet<String>();
	    final TreeSet<String> dest = new TreeSet<String>();

	    orig.add("x");
	    dest.add("y");

	    new UnsortedSetDiff<String>(orig, dest);
	} catch (Exception e) { /* OK */ }
    } // end of testCtorWithSortedSet
    
    /**
     */
    public void testPatch() {
	final Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	final Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuPatch(new HashSet<Character>(Arrays.asList(orig)), 
                new HashSet<Character>(Arrays.asList(dest)));

    } // end of testPatch

    /**
     */
    private <T> void tuPatch(final Set<T> orig, final Set<T> dest) {
	final UnsortedSetDiff<T> diff = new UnsortedSetDiff<T>(orig, dest);

	Set<T> patched = null;

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
	final Character[] orig = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	final Character[] dest = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuRevert(new HashSet<Character>(Arrays.asList(orig)), 
                 new HashSet<Character>(Arrays.asList(dest)));

    } // end of testRevert

    /**
     */
    private <T> void tuRevert(final Set<T> orig, final Set<T> dest) {
	final UnsortedSetDiff<T> diff = new UnsortedSetDiff<T>(orig, dest);
	Set<T> reverted = null;

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
} // end of class UnsortedSetDiffTest
