package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for CharSequence test
 *
 * @author Cedric Chantepie
 */
public class LCSCharSequenceDiffTest {
    
    /**
     */
    public void testPatch() {
	tuPatch("axacxab", "bxyxaz");
	tuPatch("xaiyxa", "dxayzbj");
    } // end of testPatch

    /**
     */
    private void tuPatch(final CharSequence orig, final CharSequence dest) {
	final LCSCharSequenceDiff diffA = 
	    new LCSCharSequenceDiff(orig, dest);

	CharSequence patched = null;

	try {
	    patched = diffA.patch(orig);

	    assertEquals(dest, patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diffA.patch(patched);
	} catch (DiffException e) { /* OK */ }

	// Char arrays
	final LCSCharSequenceDiff diffB = 
            new LCSCharSequenceDiff(orig.toString().toCharArray(), 
                                    dest.toString().toCharArray());

	try {
	    patched = diffB.patch(orig);

	    assertEquals(dest, patched);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diffB.patch(patched);
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

	final LCSCharSequenceDiff diffA = 
	    new LCSCharSequenceDiff(orig, dest);

	CharSequence reverted = null;

	try {
	    reverted = diffA.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diffA.revert(reverted);
	} catch (DiffException e) { /* OK */ }

	// Char arrays
	final LCSCharSequenceDiff diffB = 
	    new LCSCharSequenceDiff(orig.toString().toCharArray(), 
				    dest.toString().toCharArray());

	try {
	    reverted = diffB.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diffB.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert
} // end of class LCSCharSequenceDiffTest
