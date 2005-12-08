package no.kantega.forum.model;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 12:05:15
 * To change this template use File | Settings | File Templates.
 */
public class Attachment {
    private int id;
    private Post Post;
    private String FileName;
    private long FileSize;
    private String MimeType;
    private byte[] Data;

    public long getFileSize() {
        return FileSize;
    }

    public void setFileSize(long fileSize) {
        FileSize = fileSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }

    public Post getPost() {
        return Post;
    }

    public void setPost(Post post) {
        Post = post;
    }
}
