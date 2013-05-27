package istory;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for short test
 *
 * @author Cedric Chantepie ()
 */
public class ShortDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	ShortDiff diff = new ShortDiff((short)3, (short)9);

	Short patched = null;

	try {
	    patched = (Short) diff.patch(new Short((short)3));

	    assertEquals(new Short((short)9), patched);
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
	ShortDiff diff = new ShortDiff((short)5, (short)7);

	Short reverted = null;

	try {
	    reverted = (Short) diff.revert(new Short((short)7));

	    assertEquals(new Short((short)5), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class ShortDiffTest
