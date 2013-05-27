package istory;

import istory.DiffException;

import static org.junit.Assert.*;

/**
 * Diff for String test
 *
 * @author Cedric Chantepie
 */
public class SimpleStringDiffTest {
    
    /**
     */
    public void testPatch() {
	final String orig = "sdfjezrujdmdgkmdlfg sdf";
	final String dest = "sdflizer dfserizoper vf";

	final SimpleStringDiff diff = 
	    new SimpleStringDiff(orig, dest);

	String patched = null;

	try {
	    patched = diff.patch(orig);

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
	final String orig = "dfgmsdlkf sdferzepr dfg";
	final String dest = "drgler dfgzerzedfsdfjk";

	final SimpleStringDiff diff = 
	    new SimpleStringDiff(orig, dest);

	String reverted = null;

	try {
	    reverted = diff.revert(dest);

	    assertEquals(orig, reverted);
	} catch (DiffException e) {
	    e.printStackTrace();
	    fail("Fails to apply patch");
	} // end of catch

	try {
	    diff.revert(reverted);
	} catch (DiffException e) { /* OK */ }
    } // end of testRevert
} // end of class SimpleStringDiffTest
