package istory.spi;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Arrays;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for sortedSet diff manager
 *
 * @author Cedric Chantepie ()
 */
public class LCSSortedSetDiffManagerTest extends TestCase {
    
    /**
     */
    public void testDiff() {
	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(SortedSet.class),
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
		       spi.accept(SortedSet.class));

	} // end of for

	assertTrue("Should have found only one matching provider",
		   i == 1);
	assertNotNull("Should have found diff manager", spi);

	TreeSet orig = 
	    new TreeSet(Arrays.
			asList(new Character[] { 'a' }));

	TreeSet dest =
	    new TreeSet(Arrays.
			asList(new Character[] { 'b' }));

	Diff d = spi.diff(orig, dest);

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	assertTrue("Diff should match underlying data type",
		   (d instanceof LCSSortedSetDiff));
	
    } // end of testDiff

} // end of class LCSSortedSetDiffManagerTest
