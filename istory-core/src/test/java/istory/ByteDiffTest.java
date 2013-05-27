package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for byte test
 *
 * @author Cedric Chantepie
 */
public class ByteDiffTest {
    
    /**
     */
    public void testPatch() {
	final NumberDiff<Byte> diff = new NumberDiff<Byte>((byte)3, (byte)9);
	Byte patched = null;

	try {
	    patched = diff.patch(new Byte((byte)3));

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
	final NumberDiff<Byte> diff = new NumberDiff<Byte>((byte)5, (byte)7);
	Byte reverted = null;

	try {
	    reverted = diff.revert(new Byte((byte)7));

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
