package istory.spi;

import junit.framework.TestCase;
import junit.framework.Test;

import istory.DiffException;

/**
 * Diff for String test
 *
 * @author Cedric Chantepie ()
 */
public class SimpleStringDiffTest extends TestCase {
    
    /**
     */
    public void testPatch() {
	final String orig = "sdfjezrujdmdgkmdlfg sdf";
	final String dest = "sdflizer dfserizoper vf";

	SimpleStringDiff diff = 
	    new SimpleStringDiff(orig, dest);

	String patched = null;

	try {
	    patched = (String) diff.patch(orig);

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

	SimpleStringDiff diff = 
	    new SimpleStringDiff(orig, dest);

	String reverted = null;

	try {
	    reverted = (String) diff.revert(dest);

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
