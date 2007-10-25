package no.kantega.filesearch;

import no.kantega.publishing.search.model.SearchHit;

import java.util.List;

public class FileSearchHit extends SearchHit {
    private List trail;
    private String owner;

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
}
