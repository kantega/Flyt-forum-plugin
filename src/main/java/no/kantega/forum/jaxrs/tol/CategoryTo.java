package no.kantega.forum.jaxrs.tol;

import no.kantega.forum.jaxrs.jaxb.InstantXmlAdapter;
import no.kantega.forum.model.ForumCategory;
import org.joda.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "Category")
@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.NONE)
public class CategoryTo {

    @XmlElement(name = "id")
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "numForums")
    private Integer numForums;
    @XmlElement(name = "createdDate")
    @XmlJavaTypeAdapter(InstantXmlAdapter.class)
    private Instant createdDate;
    @XmlElement(name = "owner")
    private String owner;
    @XmlElementWrapper(name = "forumReferences")
    @XmlElement(name = "reference")
    private List<ForumReferenceTo> forumReferences;
    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;

    public CategoryTo() {
    }

    public CategoryTo(ForumCategory forumCategoryBo, List<ForumReferenceTo> forums, List<ResourceReferenceTo> actions) {
        this.id = forumCategoryBo.getId();
        this.name = forumCategoryBo.getName();
        this.description = forumCategoryBo.getDescription();
        this.numForums = forumCategoryBo.getNumForums();
        this.createdDate = forumCategoryBo.getCreatedDate() != null ? new Instant(forumCategoryBo.getCreatedDate().getTime()) : null;
        this.owner = forumCategoryBo.getOwner();
        this.forumReferences = forums;
        this.actions = actions;
    }

    public CategoryTo(Long id, String name, String description, Integer numForums, Instant createdDate, String owner, List<ForumReferenceTo> forums, List<ResourceReferenceTo> actions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numForums = numForums;
        this.createdDate = createdDate;
        this.owner = owner;
        this.forumReferences = forums;
        this.actions = actions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getNumForums() {
        return numForums;
    }

    public void setNumForums(Integer numForums) {
        this.numForums = numForums;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ForumReferenceTo> getForumReferences() {
        return forumReferences;
    }

    public void setForumReferences(List<ForumReferenceTo> forumReferences) {
        this.forumReferences = forumReferences;
    }

    public List<ResourceReferenceTo> getActions() {
        return actions;
    }

    public void setActions(List<ResourceReferenceTo> actions) {
        this.actions = actions;
    }
}
