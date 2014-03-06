package no.kantega.forum.listeners;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.search.ForumpostTransformer;
import no.kantega.search.api.index.DocumentIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Set;

import static java.util.Arrays.asList;

public class ForumIndexListener extends ForumListenerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DocumentIndexer documentIndexer;

    @Autowired
    private ForumpostTransformer forumpostTransformer;

    @Override
    public void afterPostSavedOrUpdated(Post post) {
        log.info("Indexing post " + post.getSubject());
        documentIndexer.indexDocumentAndCommit(forumpostTransformer.transform(post));

    }

    @Override
    public void afterPostDelete(Post post) {
        log.info("Deleting post " + post.getSubject());
        documentIndexer.deleteByUid(asList(forumpostTransformer.generateUniqueID(post)));
        documentIndexer.commit();
    }

    @Override
    public void beforeThreadDelete(ForumThread t) {
        Set<Post> posts = t.getPosts();
        for (Post post : posts) {
            documentIndexer.deleteByUid(asList(forumpostTransformer.generateUniqueID(post)));
        }
        documentIndexer.commit();
    }
}
