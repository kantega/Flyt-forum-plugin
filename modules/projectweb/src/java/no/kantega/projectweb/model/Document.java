package no.kantega.projectweb.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;


public class Document implements WorkflowParticipator {
    private long id;
    private String title;
    private String description;
    private String fileName;
    private DocumentCategory category;
    private Date editDate;
    private DocumentFolder documentFolder;
    private long workflowId;
    private Project project;
    private String contentType;
    private String uploader;
    private Set activities = new HashSet();
    private DocumentContent documentContent = new DocumentContent();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(long workflowId) {
        this.workflowId = workflowId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DocumentFolder getDocumentFolder() {
        return documentFolder;
    }

    public void setDocumentFolder(DocumentFolder documentFolder) {
        this.documentFolder = documentFolder;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Set getActivities() {
        return activities;
    }

    protected void setActivities(Set activities) {
        this.activities = activities;
    }

    public DocumentContent getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(DocumentContent documentContent) {
        this.documentContent = documentContent;
    }
}
