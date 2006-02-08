package no.kantega.projectweb.model;

/**
 * Created by IntelliJ IDEA.
 * User: steinarline
 * Date: 08.feb.2006
 * Time: 12:57:36
 * To change this template use File | Settings | File Templates.
 */
public class DocumentFolder {
    private long id;
    private String title;
    private DocumentFolder parent;
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

    public DocumentFolder getParent() {
        return parent;
    }

    public void setParent(DocumentFolder parent) {
        this.parent = parent;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
