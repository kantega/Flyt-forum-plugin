package no.kantega.forum.listeners;

import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;

/**
 *
 */
public interface ForumListener {
    public void beforePostDelete(Post p);
    public void beforeThreadDelete(ForumThread t);
}
