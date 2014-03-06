package no.kantega.forum.search;

import no.kantega.forum.model.Post;
import no.kantega.search.api.search.SearchResult;

import java.util.Date;

public class ForumPostSearchHit extends SearchResult {
    private String title;
    private String body;
    private String author;
    private String owner;
    private String postId;
    private String postThreadId;
    private Date postDate;


    public ForumPostSearchHit(Post post, String url, String highlightedText) {
        super((int)post.getId(), (int)post.getId(), ForumpostTransformer.HANDLED_DOCUMENT_TYPE,
                post.getSubject(), highlightedText, url, (int)post.getThread().getId());
    }

    public String getTitle() {
        if (title == null || title.length() == 0) {
            return body;
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostThreadId() {
        return postThreadId;
    }

    public void setPostThreadId(String postThreadId) {
        this.postThreadId = postThreadId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
