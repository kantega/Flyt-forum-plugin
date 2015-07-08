package no.kantega.forum.jaxrs.bol;

/**
 * @author Kristian Myrhaug
 * @since 2015-07-07
 */
public class GroupDo {

    private String id;
    private Long forumId;

    public GroupDo(String id, Long forumId) {
        this.id = id;
        this.forumId = forumId;
    }

    public String getId() {
        return id;
    }

    public Long getForumId() {
        return forumId;
    }
}
