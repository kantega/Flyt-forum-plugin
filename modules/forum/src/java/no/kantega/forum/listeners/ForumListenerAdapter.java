package no.kantega.forum.listeners;

import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;

/**
 *
 */
public class ForumListenerAdapter implements ForumListener {
    public void afterPostSavedOrUpdated(Post post) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforePostDelete(Post p) {


    }

    public void afterPostDelete(Post p) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void beforeThreadDelete(ForumThread t) {

    }
}
