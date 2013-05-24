package istory;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashSet;

import junit.framework.TestCase;
import junit.framework.Test;

/**
 * Version iterator test case.
 *
 * @author Cedric Chantepie ()
 */
public class VersionIteratorTest extends TestCase {

    /**
     * Test constructor.
     */
    public void testCtor() {
	try {
	    new VersionIterator((HashSet) null);

	    fail("Should not have been able to create " +
		 "version iterator from null");

	} catch (Exception e) { }

	HashSet versions = new HashSet();
	
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
	    new Version(null,
			"XXX-XXX",
			v2Created,
			"YYY-YYY",
			pred,
			suc);

	versions.add(v1);
	versions.add("toto");
	versions.add(v2);

	try {
	    new VersionIterator(versions);

	    fail("Should not have been able to create " +
		 "version iterator from mixed collection");

	} catch (Exception e) { }

	versions.remove("toto");

	tuIterate(versions);
    } // end of testCtor

    /**
     */
    private void tuIterate(final HashSet versions) {
	int len = 0;
	VersionIterator iter = new VersionIterator(versions);

	Version v;
	for (; iter.hasNext(); len++) {
	    v = iter.nextVersion();

	    assertTrue("All versions should be from input collection",
		       versions.contains(v));

	} // end of if

	assertTrue("Should have found as many version as collection length",
		   versions.size() == len);

    } // end of tuIterate
} // end of class VersionIteratorTest
