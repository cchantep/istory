package istory;

/**
 * Set of changes and modifications.
 *
 * @author Cedric Chantepie ()
 */
public interface Diff {

    /**
     * Apply modifications to given |orig|inal object.
     *
     * @param orig Object to be patched
     * @return A patched object
     * @throws DiffException if |orig| cannot be patched
     * @see revert(java.lang.Object)
     */
    public Object patch(Object orig) 
	throws DiffException;

    /**
     * Revert modifications from given |dest|ination object.
     *
     * @param dest Object to be reverted
     * @return Orginal object
     * @throws DiffException if |dest| cannot be reverted
     * @see patch(java.lang.Object)
     */
    public Object revert(Object dest)
	throws DiffException;

} // end of class Diff
