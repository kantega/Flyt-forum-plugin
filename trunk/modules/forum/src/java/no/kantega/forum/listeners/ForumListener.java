package no.kantega.forum.listeners;

import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;

/**
 *
 */
public interface ForumListener {
    public void afterPostSavedOrUpdated(Post post);
    public void beforePostDelete(Post post);
    public void afterPostDelete(Post post);
    public void beforeThreadDelete(ForumThread t);
}
