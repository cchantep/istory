package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for CharSequence test
 *
 * @author Cedric Chantepie ()
 */
public class LCSCharSequenceDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	tuPatch("axacxab", "bxyxaz");
	tuPatch("xaiyxa", "dxayzbj");
    } // end of testPatch

    /**
     */
    private void tuPatch(final CharSequence orig, 
			 final CharSequence dest) {

	LCSCharSequenceDiff diff = 
	    new LCSCharSequenceDiff(orig, dest);

	CharSequence patched = null;

	try {
	    patched = (CharSequence) diff.patch(orig);

	    assertEquals(dest, patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.patch(patched);
	} catch (DiffException e) { /* OK */ }

	// Char arrays
	diff = new LCSCharSequenceDiff(orig.toString().toCharArray(), 
				       dest.toString().toCharArray());

	try {
	    patched = (CharSequence) diff.patch(orig);

	    assertEquals(dest, patched);
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
	tuRevert("axacxab", "bxyxaz");
	tuRevert("xaiyxa", "dxayzbj");
    } // end of testRevert

    /**
     */
    private void tuRevert(final CharSequence orig,
			  final CharSequence dest) {

	LCSCharSequenceDiff diff = 
	    new LCSCharSequenceDiff(orig, dest);

	CharSequence reverted = null;

	try {
	    reverted = (CharSequence) diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }

	// Char arrays
	diff = 
	    new LCSCharSequenceDiff(orig.toString().toCharArray(), 
				    dest.toString().toCharArray());

	try {
	    reverted = (CharSequence) diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert

} // end of class LCSCharSequenceDiffTest
