package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Groups")
@XmlRootElement(name = "groups")
@XmlAccessorType(XmlAccessType.NONE)
public class GroupsTo {

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<GroupTo> groups;

    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;

    public GroupsTo() {}

    public GroupsTo(List<GroupTo> groups, List<ResourceReferenceTo> actions) {
        this.groups = groups;
        this.actions = actions;
    }

    public List<GroupTo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupTo> groups) {
        this.groups = groups;
    }

    public List<ResourceReferenceTo> getActions() {
        return actions;
    }

    public void setActions(List<ResourceReferenceTo> actions) {
        this.actions = actions;
    }
}

