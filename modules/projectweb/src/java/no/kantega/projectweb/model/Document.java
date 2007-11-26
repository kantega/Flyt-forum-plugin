package no.kantega.projectweb.model;

import no.kantega.commons.media.MimeType;
import no.kantega.commons.media.MimeTypes;

import java.util.*;


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
    private MimeType mimeType;

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

    public void setActivity(Activity activity) {
        
        List a = new ArrayList(getActivities());

        for (int i = 0; i < a.size(); i++) {
            Activity ac = (Activity) a.get(i);
            getActivities().remove(ac);
            ac.getDocuments().remove(this);


        }
        //getActivities().add(activity);
        //activity.getDocuments().add(this);
    }

    public DocumentContent getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(DocumentContent documentContent) {
        this.documentContent = documentContent;
    }

    public MimeType getMimeType() {
        return MimeTypes.getMimeType(fileName);
    }

    public String getIconUrl(){
        String url = "/aksess/bitmaps/mimetype/default.gif";
        MimeType type  = getMimeType();
        if( type.getType().equals("application/msword") || type.getType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")){
            url = "/aksess/bitmaps/mimetype/application/msword.gif";
        }
        else if(type.getType().equals("application/vnd.ms-excel") || type.getType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
            url = "/aksess/bitmaps/mimetype/application/vnd.ms-excel.gif";
        }
        else if(type.getType().equals("application/vnd.ms-powerpoint") || type.getType().equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
            url = "/aksess/bitmaps/mimetype/application/vnd.ms-powerpoint.gif";
        }
        else if(type.getType().equals("application/pdf")){
            url = "/aksess/bitmaps/mimetype/application/pdf.gif";
        }
        return url;
    }
}
