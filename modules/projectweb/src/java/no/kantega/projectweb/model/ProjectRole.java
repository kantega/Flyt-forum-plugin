package no.kantega.projectweb.model;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 23.sep.2005
 * Time: 15:50:37
 * To change this template use File | Settings | File Templates.
 */
public class ProjectRole {
    private long id;
    private String code;
    private String name;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
