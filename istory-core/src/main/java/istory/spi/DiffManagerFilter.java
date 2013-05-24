package istory.spi;

import java.util.HashMap;

import org.apache.commons.lang.Validate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import istory.DiffManagerFactory.Filter;

/**
 * Services filter for diff managers.
 *
 * @author Cedric Chantepie
 */
public class DiffManagerFilter implements Filter<DiffManagerSpi> {
    // --- Shared ---

    /**
     * Categorized instances
     */
    private static final HashMap<Class,DiffManagerFilter> instances;

    static {
	instances = new HashMap<Class,DiffManagerFilter>();
    } // end of <cinit>

    // --- Properties ---

    /**
     * Class to diff.
     */
    private Class clazz;

    // --- Constructors ---

    /**
     * Filter for class |c|.
     *
     * @param c Class to diff
     */
    private DiffManagerFilter(final Class c) {
	this.clazz = c;
    } // end of <init>

    /**
     * Returns filter instance for class |c|.
     *
     * @param c Class to diff
     */
    public static synchronized DiffManagerFilter getInstance(final Class c) {
	Validate.notNull(c, "Cannot get filter for null class");

	if (instances.containsKey(c)) {
	    return instances.get(c);
	} // end of if

	final DiffManagerFilter f = new DiffManagerFilter(c);

	instances.put(c, f);

	return f;
    } // end of getInstance

    // ---

    /**
     * {@inheritDoc}
     */
    public String toString() {
	return new ToStringBuilder(this).
	    append("clazz", this.clazz).
	    toString();

    } // end of toString

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return new HashCodeBuilder(3, 5).
	    append(clazz).
	    toHashCode();

    } // end of hashCode

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof DiffManagerFilter)) {
	    return false;
	} // end of if

	final DiffManagerFilter other = (DiffManagerFilter) o;

	return new EqualsBuilder().
	    append(this.clazz, other.clazz).
	    isEquals();

    } // end of equals
    
    // --- Implementation ---

    /**
     * {@inheritDoc}
     */
    public boolean accepts(final DiffManagerSpi provider) {
	if (provider == null) {
	    return false;
	} // end of if

	return provider.accept(this.clazz);
    } // end of filter
} // end of class DiffManagerFilter
