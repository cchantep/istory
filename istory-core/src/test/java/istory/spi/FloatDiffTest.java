package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for float test
 *
 * @author Cedric Chantepie ()
 */
public class FloatDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	FloatDiff diff = new FloatDiff(3f, 9f);

	Float patched = null;

	try {
	    patched = (Float) diff.patch(new Float(3f));

	    assertEquals(new Float(9f), patched);
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
	FloatDiff diff = new FloatDiff(5f, 7f);

	Float reverted = null;

	try {
	    reverted = (Float) diff.revert(new Float(7f));

	    assertEquals(new Float(5f), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class FloatDiffTest
