package istory.spi;

import istory.Diff;

/**
 * Manager diff between supported objects.
 *
 * @author Cedric Chantepie ()
 */
public interface DiffManagerSpi {
    
    /**
     * Returns diff between |o1| and |o2|.
     *
     * @param o1
     * @param o2
     * @throws ClassCastException if |o1| or |o2| 
     * are of unsupported class.
     * @see #accept(java.lang.Class)
     */
    public Diff diff(Object o1, Object o2);

    /**
     * Returns true if accepts class |c| 
     * as argument types for diff.
     *
     * @param c Class to be checked
     * @see #diff(java.lang.Object,java.lang.Object)
     */
    public boolean accept(Class c);
    
} // end of interface DiffManagerSpi
