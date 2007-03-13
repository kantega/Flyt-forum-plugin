package no.kantega.projectweb.model;

import java.util.Set;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:37:17
 * To change this template use File | Settings | File Templates.
 */
public class Project {
    private long id;
    private String name;
    private String code;
    private String goal;
    private String status;
    private String owner;
    private String leader;
    private Date startDate;
    private Date endDate;
    private float estimatedHours;
    private float usedHours;
    private float estimatedLeftHours;

    private Set activities;
    private Set documents;
    private Set participants;
    private long permissionSchemeId;
    private boolean publicProject;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public float getUsedHours() {
        return usedHours;
    }

    public void setUsedHours(float usedHours) {
        this.usedHours = usedHours;
    }

    public float getEstimatedLeftHours() {
        return estimatedLeftHours;
    }

    public void setEstimatedLeftHours(float estimatedLeftHours) {
        this.estimatedLeftHours = estimatedLeftHours;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Set getActivities() {
        return activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
    }

    public Set getDocuments() {
        return documents;
    }

    public void setDocuments(Set documents) {
        this.documents = documents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Set getParticipants() {
        return participants;
    }

    public void setParticipants(Set participants) {
        this.participants = participants;
    }


    public void setPermissionSchemeId(long permissionSchemeId) {
        this.permissionSchemeId = permissionSchemeId;
    }

    public long getPermissionSchemeId() {
        return permissionSchemeId;
    }

    public boolean isPublicProject() {
        return publicProject;
    }

    public void setPublicProject(boolean publicProject) {
        this.publicProject = publicProject;
    }
}
