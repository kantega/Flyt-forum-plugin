package no.kantega.forum.jaxrs.bol;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-07
 */
public class GroupBo {

    private Long forumId;
    private String name;

    public GroupBo(Long forumId, String name) {
        this.forumId = forumId;
        this.name = name;
    }

    public Long getForumId() {
        return forumId;
    }

    public String getName() {
        return name;
    }
}
