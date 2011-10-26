package no.kantega.forum.listeners;

import no.kantega.forum.model.Post;
import no.kantega.publishing.search.index.jobs.RemoveContentJob;
import no.kantega.publishing.search.index.jobs.UpdateContentJob;
import no.kantega.search.index.IndexManager;

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
}
