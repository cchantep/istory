package istory;

/**
 * Manager diff between supported objects.
 *
 * @author Cedric Chantepie
 */
public interface DiffManager<T> {
    
    /**
     * Returns diff between |o1| and |o2|.
     *
     * @param v1 Value #1
     * @param v2 Value #2
     * @see #accept
     */
    public <A extends T, B extends T> Diff<T> diff(A v1, B v2);
    
} // end of interface DiffManager
