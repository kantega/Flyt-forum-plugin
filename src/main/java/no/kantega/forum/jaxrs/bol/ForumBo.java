package no.kantega.forum.jaxrs.bol;

import java.time.Instant;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-08
 */
public class ForumBo {

    private Long id;
    private Long categoryId;
    private String name;
    private String description;
    private Integer numThreads;   // num threads in current Forum
    private Instant createdDate; // Forum Created Date
    private String owner;
    private Boolean anonymousPostAllowed = true;
    private Boolean attachmentsAllowed = false;
    private Boolean approvalRequired = false;
    private String moderator;

    public ForumBo() {
    }

    public ForumBo(Long id, Long categoryId, String name, String description, Integer numThreads, Instant createdDate, String owner, Boolean anonymousPostAllowed, Boolean attachmentsAllowed, Boolean approvalRequired, String moderator) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.numThreads = numThreads;
        this.createdDate = createdDate;
        this.owner = owner;
        this.anonymousPostAllowed = anonymousPostAllowed;
        this.attachmentsAllowed = attachmentsAllowed;
        this.approvalRequired = approvalRequired;
        this.moderator = moderator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public Integer getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(Integer numThreads) {
        this.numThreads = numThreads;
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

    public Boolean getAnonymousPostAllowed() {
        return anonymousPostAllowed;
    }

    public void setAnonymousPostAllowed(Boolean anonymousPostAllowed) {
        this.anonymousPostAllowed = anonymousPostAllowed;
    }

    public Boolean getAttachmentsAllowed() {
        return attachmentsAllowed;
    }

    public void setAttachmentsAllowed(Boolean attachmentsAllowed) {
        this.attachmentsAllowed = attachmentsAllowed;
    }

    public Boolean getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(Boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }
}
