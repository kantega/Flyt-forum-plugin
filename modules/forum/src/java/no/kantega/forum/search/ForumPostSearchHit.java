package no.kantega.forum.search;

import no.kantega.search.result.SearchHit;

public class ForumPostSearchHit implements SearchHit {
    private String title;
    private String body;
    private String author;

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
}
