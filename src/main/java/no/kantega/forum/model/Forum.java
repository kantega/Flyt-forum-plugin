package no.kantega.forum.model;

import java.util.Date;
import java.util.Set;


public class Forum {
    private long id;
    private ForumCategory forumCategory;
    private String name;
    private String description;
    private int numThreads;   // num threads in current Forum
    private Date createdDate; // Forum Created Date
    private Post lastPost;
    private String owner;
    private Set<ForumThread> threads;
    private boolean anonymousPostAllowed = true;
    private boolean attachmentsAllowed = false;
    private boolean approvalRequired = false;
    private String moderator;
    private Set<String> groups;
    private int numNewPosts = 0;

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Set<ForumThread> getThreads() {
        return threads;
    }

    public void setThreads(Set<ForumThread> threads) {
        this.threads = threads;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public ForumCategory getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(ForumCategory forumCategory) {
        this.forumCategory = forumCategory;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Post getLastPost() {
        return lastPost;
    }

    public void setLastPost(Post lastPost) {
        this.lastPost = lastPost;
    }

    public boolean isAttachmentsAllowed() {
        return attachmentsAllowed;
    }

    public void setAttachmentsAllowed(boolean attachmentsAllowed) {
        this.attachmentsAllowed = attachmentsAllowed;
    }

    public boolean isAnonymousPostAllowed() {
        return anonymousPostAllowed;
    }

    public void setAnonymousPostAllowed(boolean anonymousPostAllowed) {
        this.anonymousPostAllowed = anonymousPostAllowed;
    }

    public boolean isApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(boolean approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getModerator() {
        return moderator;
    }

    public void setModerator(String moderator) {
        this.moderator = moderator;
    }

    public int getNumNewPosts() {
        return numNewPosts;
    }

    public void setNumNewPosts(int newPosts) {
        this.numNewPosts = newPosts;
    }

    public boolean isClosed() {
        return !(groups == null || groups.isEmpty() || groups.contains("everyone"));
    }
}
