package no.kantega.projectweb.model;

import java.util.Set;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 7, 2005
 * Time: 11:58:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class Group {
    private long id;
    private String name;
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

}
