package no.kantega.projectweb.model;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 26.sep.2005
 * Time: 16:25:32
 * To change this template use File | Settings | File Templates.
 */
public class ActivityStatus {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
