package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for integer test
 *
 * @author Cedric Chantepie
 */
public class IntegerDiffTest {
    
    /**
     */
    public void testPatch() {
	final NumberDiff<Integer> diff = new NumberDiff<Integer>(3, 9);
	Integer patched = null;

	try {
	    patched = diff.patch(new Integer(3));

	    assertEquals(new Integer(9), patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.patch(patched);
	} catch (DiffException e) { /* OK */ }
    } // end of testPatch

    /**
     */
    public void testRevert() {
	final NumberDiff<Integer> diff = new NumberDiff<Integer>(5, 7);
	Integer reverted = null;

	try {
	    reverted = diff.revert(new Integer(7));

	    assertEquals(new Integer(5), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert
} // end of class IntegerDiffTest
