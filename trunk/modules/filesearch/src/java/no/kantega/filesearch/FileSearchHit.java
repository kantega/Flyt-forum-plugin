package no.kantega.filesearch;

import no.kantega.search.result.SearchHit;

import java.util.List;


public class FileSearchHit implements SearchHit {
    private List trail;
    private String owner;
    private String url;
    private String title;

    public List getTrail() {
        return trail;
    }

    public void setTrail(List trail) {
        this.trail = trail;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
