package istory.spi;

import java.util.SortedMap;
import java.util.Iterator;
import java.util.TreeMap;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for sortedMap diff manager
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedMapDiffManagerTest extends TestCase {
    
    /**
     */
    public void testDiff() {
	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(SortedMap.class),
				false);

	int i = 0;
	Object o;
	DiffManagerSpi spi = null;
	for (; services.hasNext(); i++) {
	    o = services.next();

	    assertTrue("Found provider should " +
		       "implement DiffManagerSpi", 
		       (o instanceof DiffManagerSpi));

	    spi = (DiffManagerSpi) o;

	    assertTrue("Found provider should accept filtered class",
		       spi.accept(SortedMap.class));

	} // end of for

	assertTrue("Should have found only one matching provider",
		   i == 1);
	assertNotNull("Should have found diff manager", spi);

	TreeMap orig = new TreeMap();
	TreeMap dest = new TreeMap();

	orig.put("a", "b");

	dest.put("c", "d");

	Diff d = spi.diff(orig, dest);

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	assertTrue("Diff should match underlying data type",
		   (d instanceof LCSSortedMapDiff));
	
    } // end of testDiff

} // end of class LCSSortedMapDiffManagerTest
