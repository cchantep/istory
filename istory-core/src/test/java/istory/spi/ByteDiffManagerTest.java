package istory.spi;

import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for byte diff manager
 *
 * @author Cedric Chantepie ()
 */
public class ByteDiffManagerTest extends TestCase {
    
    /**
     */
    public void testDiff() {
	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(Byte.class),
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
		       spi.accept(Byte.class));

	} // end of for

	assertTrue("Should have found only one matching provider",
		   i == 1);
	assertNotNull("Should have found diff manager", spi);

	Diff d = spi.diff(new Byte((byte)3), new Byte((byte)6));

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	assertTrue("Diff should match underlying data type",
		   (d instanceof ByteDiff));
	
    } // end of testDiff

} // end of class ByteDiffManagerTest
