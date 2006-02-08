package no.kantega.projectweb.model;

import java.util.Date;


public class Document implements WorkflowParticipator {
    private long id;
    private String title;
    private String fileName;
    private byte[] content;
    private ActivityStatus status;
    private Date editDate;
    private DocumentFolder documentFolder;
    private long workflowId;
    private Project project;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
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
}
