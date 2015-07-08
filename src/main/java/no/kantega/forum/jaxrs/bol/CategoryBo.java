package no.kantega.forum.jaxrs.bol;

import java.time.Instant;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-08
 */
public class CategoryBo {

    private Long id;
    private String name;
    private String description;
    private Integer numForums;
    private Instant createdDate;
    private String owner;

    public CategoryBo() {
    }

    public CategoryBo(Long id, String name, String description, Integer numForums, Instant createdDate, String owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numForums = numForums;
        this.createdDate = createdDate;
        this.owner = owner;
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
}
