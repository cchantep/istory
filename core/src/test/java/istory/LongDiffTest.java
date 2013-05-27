package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for long test
 *
 * @author Cedric Chantepie
 */
public class LongDiffTest {
    
    /**
     */
    public void testPatch() {
	final NumberDiff<Long> diff = new NumberDiff<Long>(3l, 9l);
	Long patched = null;

	try {
	    patched = diff.patch(new Long(3));

	    assertEquals(new Long(9), patched);
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
	final NumberDiff<Long> diff = new NumberDiff<Long>(5l, 7l);
	Long reverted = null;

	try {
	    reverted = diff.revert(new Long(7));

	    assertEquals(new Long(5), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert
} // end of class LongDiffTest
