package istory.spi;

import java.util.LinkedHashMap;
import java.util.WeakHashMap;
import java.util.SortedMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for Map diff manager
 *
 * @author Cedric Chantepie ()
 */
public class UnsortedMapDiffManagerTest extends TestCase {

    /**
     */
    public void testDiff() {
	tuDiff(ConcurrentHashMap.class);
	tuDiff(HashMap.class);
	tuDiff(Hashtable.class);
	tuDiff(LinkedHashMap.class);
	tuDiff(WeakHashMap.class);
    } // end of testDiff
    
    /**
     */
    private void tuDiff(Class mapClass) {
	assertNotNull("Cannot test collection diff with null class",
		      mapClass);

	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(Map.class),
				false);

	int i = 0;
	Object o;
	DiffManagerSpi spi = null;
	DiffManagerSpi m = null;

	for (; services.hasNext(); i++) {
	    o = services.next();

	    assertTrue("Found provider should " +
		       "implement DiffManagerSpi", 
		       (o instanceof DiffManagerSpi));

	    spi = (DiffManagerSpi) o;

	    assertTrue("Found provider should accept filtered class",
		       spi.accept(Map.class));

	    if (!spi.accept(SortedMap.class)) {
		m = spi;
	    } // end of if
	} // end of for

	assertTrue("Should have found only one matching provider",
		   i >= 1);
	assertNotNull("Should have found diff manager", spi);
	assertNotNull("Should have found diff manager", m);
	assertFalse("Provider should not accept sorted map",
		    m.accept(SortedMap.class));

	Map map1 = null;
	Map map2 = null;

	try {
	    map1 = (Map) mapClass.newInstance();
	    map2 = (Map) mapClass.newInstance();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Fails to create collection instance: " + 
		 mapClass.getName());

	} // end of catch
	    
	map1.put("a", "b");
	map2.put("c", "d");

	Diff d = m.diff(map1, map2);

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	
    } // end of testDiff

} // end of class UnsortedMapDiffManagerTest
