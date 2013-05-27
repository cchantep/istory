package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for double test
 *
 * @author Cedric Chantepie
 */
public class DoubleDiffTest {
    
    /**
     */
    public void testPatch() {
	final DoubleDiff diff = new DoubleDiff(3, 9);
	Double patched = null;

	try {
	    patched = diff.patch(new Double(3));

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
	final DoubleDiff diff = new DoubleDiff(5, 7);
	Double reverted = null;

	try {
	    reverted = diff.revert(new Double(7));

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
