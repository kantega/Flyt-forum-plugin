package no.kantega.forum.search;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.search.api.IndexableDocument;
import no.kantega.search.api.index.ProgressReporter;
import no.kantega.search.api.provider.IndexableDocumentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ForumSearchProvider implements IndexableDocumentProvider {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ProgressReporter progressReporter;

    private ForumDao forumDao;

    @Autowired
    private ForumpostTransformer forumpostTransformer;

    public void setForumDao(ForumDao forumDao) {
        this.forumDao = forumDao;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public ProgressReporter getProgressReporter() {
        if(progressReporter == null){
            progressReporter = new ProgressReporter(ForumpostTransformer.HANDLED_DOCUMENT_TYPE, getNumberOfDocuments());
        }
        return progressReporter;
    }

    private long getNumberOfDocuments() {
        return forumDao.getAllPosts().size();
    }

    @Override
    public void provideDocuments(BlockingQueue<IndexableDocument> indexableDocuments) {
        try {
            List<Post> allPosts = forumDao.getAllPosts();
            for (Post post : allPosts) {
                if(progressReporter==null || progressReporter.isFinished()){
                    break;
                }
                try {
                    IndexableDocument indexablePost = forumpostTransformer.transform(post);
                    indexableDocuments.put(indexablePost);
                } catch (InterruptedException e) {
                    log.error("Interruped", e);
                } finally {
                    progressReporter.reportProgress();
                }
            }
        } finally {
            progressReporter = null;
        }

    }
}
