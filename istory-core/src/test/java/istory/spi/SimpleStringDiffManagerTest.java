package istory.spi;

import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for String diff manager
 *
 * @author Cedric Chantepie ()
 */
public class SimpleStringDiffManagerTest extends TestCase {
    
    /**
     */
    public void testDiff() {
	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(String.class),
				false);

	int i = 0;
	Object o;
	DiffManagerSpi spi = null;
	SimpleStringDiffManager m = null;
	for (; services.hasNext(); i++) {
	    o = services.next();

	    assertTrue("Found provider should " +
		       "implement DiffManagerSpi", 
		       (o instanceof DiffManagerSpi));
	    
	    spi = (DiffManagerSpi) o;

	    assertTrue("Found provider should accept filtered class",
		       spi.accept(String.class));

	    if (spi instanceof SimpleStringDiffManager) {
		m = (SimpleStringDiffManager) spi;
	    } // end of if
	} // end of for

	assertTrue("Should have found only one matching provider",
		   i == 2);
	assertNotNull("Should have found diff manager", m);

	Diff d = m.diff("tototo", "tatota");

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	
    } // end of testDiff

} // end of class SimpleStringDiffManagerTest
