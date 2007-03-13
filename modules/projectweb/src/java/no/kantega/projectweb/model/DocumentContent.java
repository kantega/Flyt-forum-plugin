package no.kantega.projectweb.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;


public class DocumentContent {
    private long id;
    private byte[] content;
    private String contentText;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

}
