package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for long test
 *
 * @author Cedric Chantepie ()
 */
public class LongDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	LongDiff diff = new LongDiff(3, 9);

	Long patched = null;

	try {
	    patched = (Long) diff.patch(new Long(3));

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
	LongDiff diff = new LongDiff(5, 7);

	Long reverted = null;

	try {
	    reverted = (Long) diff.revert(new Long(7));

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
