package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for byte test
 *
 * @author Cedric Chantepie ()
 */
public class ByteDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	ByteDiff diff = new ByteDiff((byte)3, (byte)9);

	Byte patched = null;

	try {
	    patched = (Byte) diff.patch(new Byte((byte)3));

	    assertEquals(new Byte((byte)9), patched);
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
	ByteDiff diff = new ByteDiff((byte)5, (byte)7);

	Byte reverted = null;

	try {
	    reverted = (Byte) diff.revert(new Byte((byte)7));

	    assertEquals(new Byte((byte)5), reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class ByteDiffTest
