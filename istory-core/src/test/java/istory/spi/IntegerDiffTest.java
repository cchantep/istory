package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for integer test
 *
 * @author Cedric Chantepie ()
 */
public class IntegerDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	IntegerDiff diff = new IntegerDiff(3, 9);

	Integer patched = null;

	try {
	    patched = (Integer) diff.patch(new Integer(3));

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
	IntegerDiff diff = new IntegerDiff(5, 7);

	Integer reverted = null;

	try {
	    reverted = (Integer) diff.revert(new Integer(7));

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
