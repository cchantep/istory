package istory.spi;

import istory.Diff;

/**
 * Manager diff between strings.
 *
 * @author Cedric Chantepie ()
 */
public class SimpleStringDiffManager implements DiffManagerSpi {

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    public SimpleStringDiffManager() {
    } // end of <init>

    // --- Implementation ---
    
    /**
     * {@inheritDoc}
     */
    public Diff diff(Object o1, Object o2) {
	String s1 = (String) o1;
	String s2 = (String) o2;

	return new SimpleStringDiff(s1, s2);
    } // end of diff

    /**
     * {@inheritDoc}
     */
    public boolean accept(Class c) {
	return String.class.isAssignableFrom(c);
    } // end of accept
    
} // end of class SimpleStringDiffManager
