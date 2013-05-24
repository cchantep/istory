package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for double test
 *
 * @author Cedric Chantepie ()
 */
public class DoubleDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	DoubleDiff diff = new DoubleDiff(3, 9);

	Double patched = null;

	try {
	    patched = (Double) diff.patch(new Double(3));

	    assertEquals(new Double(9), patched);
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
	DoubleDiff diff = new DoubleDiff(5, 7);

	Double reverted = null;

	try {
	    reverted = (Double) diff.revert(new Double(7));

	    assertEquals(new Double(5), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class DoubleDiffTest
