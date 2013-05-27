package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for boolean test
 *
 * @author Cedric Chantepie 
 */
public class BooleanDiffTest {
    
    /**
     */
    public void testPatch() {
	final BooleanDiff diff = new BooleanDiff(true, false);
	Boolean patched = null;

	try {
	    patched = diff.patch(Boolean.TRUE);

	    assertEquals(Boolean.FALSE, patched);
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
	final BooleanDiff diff = new BooleanDiff(true, false);
	Boolean patched = null;

	try {
	    patched = diff.revert(Boolean.FALSE);

	    assertEquals(Boolean.TRUE, patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.patch(patched);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert
} // end of class BooleanDiffTest
