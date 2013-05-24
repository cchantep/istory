package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for boolean test
 *
 * @author Cedric Chantepie ()
 */
public class BooleanDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	BooleanDiff diff = new BooleanDiff(true, false);

	Boolean patched = null;

	try {
	    patched = (Boolean) diff.patch(Boolean.TRUE);

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
	BooleanDiff diff = new BooleanDiff(true, false);

	Boolean patched = null;

	try {
	    patched = (Boolean) diff.revert(Boolean.FALSE);

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
