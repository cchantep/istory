package istory;

import java.io.Serializable;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Arrays;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.time.DateFormatUtils;

/** 
 * Version state extracted from an history.
 *
 * @author Cedric Chantepie ()
 * @see istory.VersionHistory
 */
public class Version implements Serializable {
    // --- Properties ---

    /**
     * Entity identifier
     */
    protected String id = null;

    /**
     * Version name
     */
    protected String name = null;

    /**
     * Identifier of containing history.
     */
    protected String historyId = null;

    /**
     * Creation time
     */
    protected Calendar created = null;

    /**
     * Names of predecessors
     */
    protected HashSet predecessorNames = null;

    /**
     * Names of successors
     */
    protected HashSet successorNames = null;

    // --- Constructors ---

    /**
     * No-arg constructor.
     */
    private Version() {
    } // end of <init>

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
    public Version(String id,
		   String historyId,
		   Calendar created,
		   String name,
		   String[] predecessorNames,
		   String[] successorNames) {

	this.id = id;
	this.historyId = historyId;
	this.created = created;
	this.name = name;

	if (predecessorNames != null) {
	    this.predecessorNames = 
		new HashSet(Arrays.
			    asList(predecessorNames));

	} // end of if

	if (successorNames != null) {
	    this.successorNames = 
		new HashSet(Arrays.
			    asList(successorNames));

	} // end of if
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
     * @see #getSuccessorNames()
     * @see #getName()
     */
    public String[] getPredecessorNames() {
	if (predecessorNames == null) {
	    return new String[0];
	} // end of if

	return (String[]) predecessorNames.
	    toArray(new String[predecessorNames.size()]);

    } // end of getPredecessorNames

    /** 
     * Returns names of successor states.
     * @see #getPredecessorNames()
     * @see #getName()
     */
    public String[] getSuccessorNames() {
	if (successorNames == null) {
	    return new String[0];
	} // end of if

	return (String[]) successorNames.
	    toArray(new String[successorNames.size()]);

    } // end of getSuccessorNames

    /** 
     * Returns time when this version was created.
     */
    public Calendar getCreated() {
	return this.created;
    } // end of getCreated
  
    /** 
     * Returns unique identifier for history containing this version.
     * @see istory.VersionHistory
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

	Version other = (Version) o;

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
	String createdStr = null;

	if (this.created != null) {
	    createdStr = 
		DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.
		format(this.created);

	} // end of if

	return new ToStringBuilder(this).
	    append("id", this.id).
	    append("historyId", this.historyId).
	    append("created", createdStr).
	    append("name", this.name).
	    toString();

    } // end of toString

} // end of class Version
