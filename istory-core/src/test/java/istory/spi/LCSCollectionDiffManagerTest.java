package istory.spi;

import java.util.PriorityQueue;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.Stack;

import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.TestCase;
import junit.framework.Test;


import istory.Diff;

/**
 * Test for Collection diff manager
 *
 * @author Cedric Chantepie ()
 */
public class LCSCollectionDiffManagerTest extends TestCase {

    /**
     */
    public void testDiff() {
	tuDiff(ArrayList.class);
	tuDiff(LinkedList.class);
	tuDiff(Vector.class);
	tuDiff(Stack.class);
	tuDiff(LinkedBlockingQueue.class);
	tuDiff(PriorityQueue.class);
    } // end of testDiff
    
    /**
     */
    private void tuDiff(Class collectionClass) {
	assertNotNull("Cannot test collection diff with null class",
		      collectionClass);

	ServiceRegistry reg = ServiceRegistry.getInstance();
	Iterator services = reg.
	    getServiceProviders(this.getClass().getClassLoader(),
				DiffManagerSpi.class,
				DiffManagerFilter.
				getInstance(collectionClass),
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
		       spi.accept(Collection.class));

	    assertTrue("Found provider should accept filtered class",
		       spi.accept(collectionClass));

	    if (spi instanceof LCSCollectionDiffManager) {
		m = spi;
	    } // end of if
	} // end of for

	assertTrue("Should have found only one matching provider",
		   i >= 1);
	assertNotNull("Should have found diff manager", m);

	Collection col1 = null;
	Collection col2 = null;

	try {
	    col1 = (Collection) collectionClass.newInstance();
	    col2 = (Collection) collectionClass.newInstance();
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("Fails to create collection instance: " + 
		 collectionClass.getName());

	} // end of catch
	    
	col1.add("a");
	col2.add("b");

	Diff d = m.diff(col1, col2);

	assertNotNull("Diff manager should have been able " +
		      "to create diff", d);
	
    } // end of testDiff

} // end of class LCSCollectionDiffManagerTest
