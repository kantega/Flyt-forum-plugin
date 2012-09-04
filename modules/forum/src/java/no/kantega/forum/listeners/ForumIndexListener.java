package no.kantega.forum.listeners;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.publishing.search.index.jobs.RemoveContentJob;
import no.kantega.publishing.search.index.jobs.UpdateContentJob;
import no.kantega.search.index.IndexManager;

import java.util.Iterator;
import java.util.Set;

public class ForumIndexListener extends ForumListenerAdapter {
    private IndexManager indexManager;

    @Override
    public void afterPostSavedOrUpdated(Post post) {
        indexManager.addIndexJob(new RemoveContentJob(Long.toString(post.getId()), "forumContent"));
        indexManager.addIndexJob(new UpdateContentJob(Long.toString(post.getId()), "forumContent"));
    }

    @Override
    public void afterPostDelete(Post post) {
        indexManager.addIndexJob(new RemoveContentJob(Long.toString(post.getId()), "forumContent"));
    }

    public void setIndexManager(IndexManager indexManager) {
        this.indexManager = indexManager;
    }

    @Override
    public void beforeThreadDelete(ForumThread t) {
        Set posts = t.getPosts();
        Iterator iterator = posts.iterator();
        while(iterator.hasNext()){
            Post post = (Post)iterator.next();
            indexManager.addIndexJob(new RemoveContentJob(Long.toString(post.getId()), "forumContent"));
        }
    }
}
