package istory;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Version POJO test case.
 *
 * @author Cedric Chantepie
 */
public class VersionTest {

    /**
     */
    public void testCtor() {
	String[] pred = new String[] { "JJJ" };
	String[] suc = new String[0];
	Calendar v2Created = GregorianCalendar.getInstance();
	Version v1 = 
	    new Version(null,
			null,
			null,
			null,
			null,
			null);

	Version v2 = 
	    new Version("AAA-AAA",
			"XXX-XXX",
			v2Created,
			"YYY-YYY",
			pred,
			suc);


	assertNull("Version id should be null", v1.getId());
	assertNull("History id should be null", v1.getHistoryId());
	assertNull("Creation time should be null", v1.getCreated());
	assertNull("Name should be null", v1.getName());

	assertTrue("Predecessors should be empty", 
		   Arrays.equals(new String[0],
				 v1.getPredecessorNames()));

	assertTrue("Successors should be empty", 
		   Arrays.equals(new String[0],
				 v1.getSuccessorNames()));

	assertEquals("Unexpected version id",
		     "AAA-AAA", v2.getId());
	
	assertEquals("Unexpected history id", 
		     "XXX-XXX", v2.getHistoryId());
	
	assertEquals("Unexpected creation time",
		     v2Created, v2.getCreated());
	
	assertEquals("Unexpected name",
		     "YYY-YYY", v2.getName());

	assertTrue("Unexpected predecessors",
		   Arrays.equals(pred, v2.getPredecessorNames()));

	assertTrue("Unexpected successors",
		   Arrays.equals(suc, v2.getSuccessorNames()));

	tuEquals(v1, v2);
    } // end of testCtor

    /**
     */
    private void tuEquals(final Version v1, 
			  final Version v2) {

	assertFalse("v1 and v2 should not be equal",
		    v1.equals(v2));

	assertFalse("v1 should not be equal to null",
		    v1.equals(null));
	
	Version other = 
	    new Version(v1.getId(),
			v1.getHistoryId(),
			v1.getCreated(),
			v1.getName(),
			v1.getPredecessorNames(),
			v1.getSuccessorNames());

	assertFalse("Other version should not be equal to v2",
		    other.equals(v2));

	assertEquals("Other version should be equal to v1",
		     v1, other);

	assertTrue("Other version and v1 hash codes should be equal",
		   v1.hashCode() == other.hashCode());

	other = 
	    new Version(v2.getId(),
			v2.getHistoryId(),
			v2.getCreated(),
			v2.getName(),
			v2.getPredecessorNames(),
			v2.getSuccessorNames());

	assertFalse("Other version should not be equal to v1",
		    other.equals(v1));

	assertEquals("Other version should be equal to v2",
		     v2, other);

	assertTrue("Other version and v2 hash codes should be equal",
		   v2.hashCode() == other.hashCode());

    } // end of tuEquals
} // end of class VersionTest
