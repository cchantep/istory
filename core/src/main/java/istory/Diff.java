package istory;

/**
 * Set of changes and modifications.
 *
 * @author Cedric Chantepie
 */
public interface Diff<T> {

    /**
     * Apply modifications to given |original| object.
     *
     * @param original Object to be patched
     * @return A patched object
     * @throws DiffException if |original| cannot be patched
     * @see #revert
     */
    public <V extends T> T patch(V original) throws DiffException;

    /**
     * Revert modifications from given |destination| object.
     *
     * @param destination Object to be reverted
     * @return Original object
     * @throws DiffException if |destination| cannot be reverted
     * @see #patch
     */
    public <V extends T> T revert(V destination) throws DiffException;

} // end of class Diff
