package istory;

import java.io.Serializable;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.time.DateFormatUtils;

/** 
 * Version state extracted from an history.
 *
 * @author Cedric Chantepie
 */
public class Version implements Serializable {
    // --- Properties ---

    /**
     * Entity identifier
     */
    protected final String id;

    /**
     * Version name
     */
    protected final String name;

    /**
     * Identifier of containing history.
     */
    protected final String historyId;

    /**
     * Creation time
     */
    protected final Calendar created;

    /**
     * Names of predecessors
     */
    protected final HashSet<String> predecessorNames;

    /**
     * Names of successors
     */
    protected final HashSet<String> successorNames;

    // --- Constructors ---

    /**
     * Bulk constructor
     *
     * @param id Version synthetic identifier
     * @param historyId Identifier of containing history
     * @param created Creation time
     * @param name Version name
     * @param predecessorNames Names or predecessors in history
     * @param successorNames Names of successors in history
     */
    public Version(final String id,
		   final String historyId,
		   final Calendar created,
		   final String name,
		   final String[] predecessorNames,
		   final String[] successorNames) {

	this.id = id;
	this.historyId = historyId;
	this.created = created;
	this.name = name;

        this.predecessorNames = (predecessorNames != null)
            ? new HashSet<String>(Arrays.asList(predecessorNames))
            : new HashSet<String>();



        this.successorNames = (successorNames != null)
            ? new HashSet<String>(Arrays.asList(successorNames))
            : new HashSet<String>();

    } // end of <init>

    // --- Properties accessors ---

    /**
     * Returns version identifier.
     */
    public String getId() {
	return this.id;
    } // end of getId

    /** 
     * Returns name of successor states.
     * @see #getSuccessorNames
     * @see #getName
     */
    public String[] getPredecessorNames() {
        final int len = predecessorNames.size();

	if (predecessorNames == null || len == 0) {
	    return new String[0];
	} // end of if

        return predecessorNames.toArray(new String[len]);
    } // end of getPredecessorNames

    /** 
     * Returns names of successor states.
     * @see #getPredecessorNames
     * @see #getName
     */
    public String[] getSuccessorNames() {
        final int len = successorNames.size();

	if (successorNames == null) {
	    return new String[0];
	} // end of if

	return successorNames.toArray(new String[len]);
    } // end of getSuccessorNames

    /** 
     * Returns time when this version was created.
     */
    public Calendar getCreated() {
	return this.created;
    } // end of getCreated
  
    /** 
     * Returns unique identifier for history containing this version.
     */
    public String getHistoryId() {
	return this.historyId;
    } // end of getHistoryId

    /** 
     * Returns name of this version, unique in the containing history.
     */
    public String getName() {
	return this.name;
    } // end of getName

    // ---

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
	if (o == null || !(o instanceof Version)) {
	    return false;
	} // end of if

	final Version other = (Version) o;

	return new EqualsBuilder().
	    append(this.id, other.id).
	    append(this.historyId, other.historyId).
	    append(this.created, other.created).
	    append(this.name, other.name).
	    append(this.getPredecessorNames(), 
		   other.getPredecessorNames()).
	    append(this.getSuccessorNames(), 
		   other.getSuccessorNames()).
	    isEquals();

    } // end of equals

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
	return new HashCodeBuilder(1, 3).
	    append(this.id).
	    append(this.historyId).
	    append(this.created).
	    append(this.name).
	    append(this.getPredecessorNames()).
	    append(this.getSuccessorNames()).
	    toHashCode();

    } // end of hashCode

    /**
     * {@inheritDoc}
     */
    public String toString() {
	final String createdStr = (this.created == null) ? null
            : DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.
            format(this.created);

	return new ToStringBuilder(this).
	    append("id", this.id).
	    append("historyId", this.historyId).
	    append("created", createdStr).
	    append("name", this.name).
	    toString();

    } // end of toString
} // end of class Version
