package istory;

import java.util.ListIterator;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ServiceLoader;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import istory.spi.DiffManagerSpi;

/**
 * DiffManager factory.
 *
 * @author Cedric Chantepie
 */
public class DiffManagerFactory {
    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    private DiffManagerFactory() {
    } // end of <init>

    /**
     * Returns registry instance
     */
    public static synchronized DiffManagerFactory getInstance() {
	return new DiffManagerFactory();
    } // end of getInstance
    
    // ---

    /**
     * Returns diff managers.
     *
     * @param loader Class loader used for resources
     * @param category Service provider category defined 
     * by a wellknown interface
     * @return Provider iterator
     * @see #getDiffManagers(Class,Filter)
     */
    public <T extends DiffManagerSpi> Iterable<T> getDiffManagers(final Class<T> category) {

	return getDiffManagers(category, null);
    } // end of getDiffManagers

    /**
     * Returns diff managers.
     *
     * @param category Service provider category defined 
     * by a wellknown interface
     * @param filter Apply to each found provider in order to retain 
     * or exclude it
     * @return Providers iterator
     */
    public <T extends DiffManagerSpi> Iterable<T> getDiffManagers(final Class<T> category, final Filter<T> filter) {

        final ServiceLoader<DiffManagerSpi> loader = 
            ServiceLoader.load(DiffManagerSpi.class);

        final ArrayList<T> managers = new ArrayList<T>();

        for (final DiffManagerSpi manager : loader) {
            if (category.isAssignableFrom(manager.getClass())) {
                continue; // skip
            } // end of if

            // ---

            final T t = (T) manager;

            if (filter == null || filter.accepts(t)) {
                managers.add(t);
            } // end of if
        } // end of for
            
        return managers;
    } // end of getDiffManagers

    // --- Inner classes ---

    /**
     * Service filter.
     *
     * @author Cedric Chantepie
     */
    public static interface Filter<T extends DiffManagerSpi> {
        boolean accepts(T manager);
    } // end of interface Filter
} // end of class DiffManagerFactory
