package no.kantega.forum.listeners;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;

/**
 *
 */
public interface ForumListener {
    void afterPostSavedOrUpdated(Post post);
    void beforePostDelete(Post post);
    void afterPostDelete(Post post);
    void beforeThreadDelete(ForumThread t);
}
