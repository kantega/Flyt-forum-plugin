package no.kantega.projectweb.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

public class Activity implements WorkflowParticipator {
    private long id;
    private String title;
    private String description;
    private ActivityStatus status;
    private Project project;
    private ActivityType type;
    private ActivityPriority priority;
    private ProjectPhase projectPhase;
    private String assignee;
    private String reporter;

    private Date startDate;
    private Date endDate;
    private float estimatedHours;
    private float usedHours;
    private float estimatedLeftHours;
    private Set comments;
    private Set documents = new HashSet();


    private long workflowId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(long workflowId) {
        this.workflowId = workflowId;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public ActivityType getType() {
        return type;
    }

    public void setPriority(ActivityPriority priority) {
        this.priority = priority;
    }

    public ActivityPriority getPriority() {
        return priority;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public Set getComments() {
        return comments;
    }

    public void setComments(Set comments) {
        this.comments = comments;
    }

    public ProjectPhase getProjectPhase() {
        return projectPhase;
    }

    public void setProjectPhase(ProjectPhase projectPhase) {
        this.projectPhase = projectPhase;
    }

    public Set getDocuments() {
        return documents;
    }

    protected void setDocuments(Set documents) {
        this.documents = documents;
    }

    public void addToDocuments(Document document){
        getDocuments().add(document);
        document.getActivities().add(this);
    }

    public void removeFromDocuments(Document document){
        getDocuments().remove(document);
        document.getActivities().remove(this);
    }
}
