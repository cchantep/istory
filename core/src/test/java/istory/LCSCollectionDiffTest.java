package istory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for Collection test
 *
 * @author Cedric Chantepie
 */
public class LCSCollectionDiffTest {

    /**
     */
    public void testCtorWithSet() {
	try {
	    final HashSet<String> orig = new HashSet<String>();
	    final HashSet<String> dest = new HashSet<String>();

	    orig.add("x");
	    dest.add("y");

	    new LCSCollectionDiff<String>(orig, dest);
	} catch (Exception e) { /* OK */ }
    } // end of testCtorWithSet
    
    /**
     */
    public void testPatch() {
	final Character[] origA = { 'a', 'x', 'a', 'c', 'x', 'a', 'b' };
	final Character[] destA = { 'b', 'x', 'y', 'x', 'a', 'z' };

	tuPatch(origA, destA);

	final Character[] origB = 
            new Character[] { 'x', 'a', 'i', 'y', 'x', 'a' };

	final Character[] destB = 
            new Character[] { 'd', 'x', 'a', 'y', 'z', 'b', 'j' };

	final List<Character> origList = Arrays.asList(origB);
	final List<Character> destList = Arrays.asList(destB);

	tuPatch(origList, destList);
    } // end of testPatch

    /**
     */
    private <T> void tuPatch(final T[] orig, final T[] dest) {
        tuPatch(Arrays.asList(orig), Arrays.asList(dest));
    } // end of tuPatch

    /**
     */
    private <T> void tuPatch(final Collection<T> orig, 
                             final Collection<T> dest) {

	final LCSCollectionDiff<T> diff = new LCSCollectionDiff<T>(orig, dest);

	Collection<T> patched = null;

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

	final List<Character> origList = Arrays.asList(origB);
	final List<Character> destList = Arrays.asList(destB);

	tuRevert(origList, destList);
    } // end of testRevert

    /**
     */
    private <T> void tuRevert(final T[] orig, final T[] dest) {
        tuRevert(Arrays.asList(orig), Arrays.asList(dest));
    } // end of testRevert

    /**
     */
    private <T> void tuRevert(final Collection<T> orig, 
                              final Collection<T> dest) {

	final LCSCollectionDiff<T> diff = 
	    new LCSCollectionDiff<T>(orig, dest);

	Collection<T> reverted = null;

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
} // end of class LCSCollectionDiffTest
