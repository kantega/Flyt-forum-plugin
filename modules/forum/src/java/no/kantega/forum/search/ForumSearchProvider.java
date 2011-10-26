package no.kantega.forum.search;

import no.kantega.commons.exception.NotAuthorizedException;
import no.kantega.commons.log.Log;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.search.index.Fields;
import no.kantega.search.index.provider.DocumentProvider;
import no.kantega.search.index.provider.DocumentProviderHandler;
import no.kantega.search.index.rebuild.ProgressReporter;
import no.kantega.search.result.SearchHit;
import no.kantega.search.result.SearchHitContext;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import java.util.List;

public class ForumSearchProvider implements DocumentProvider {
    ForumDao forumDao;


    public String getSourceId() {
        return "forumContent";
    }

    public String getDocumentType() {
        return ForumFields.TYPE_FORUM_POST;
    }

    public void provideDocuments(DocumentProviderHandler handler, ProgressReporter reporter) {
        List posts = forumDao.getAllPosts();

        for (int i = 0; i < posts.size(); i++) {
            Document d = getForumDocument((Post)posts.get(i));
            handler.handleDocument(d);
            reporter.reportProgress(i, "forum-post", posts.size());
        }
    }

    public Document provideDocument(String s) {
        try {
            long postId = Long.parseLong(s);

            Post p = forumDao.getPost(postId);
            if (p != null) {
                return getForumDocument(p);
            }
        } catch (Exception e) {
            Log.error(this.getClass().getName(), e);
        }

        return null;
    }

    private Document getForumDocument(Post post)  {

        Document d = null;

        try {
            d = new Document();

            String allText = "";
            allText += post.getSubject() + " ";
            allText += post.getBody();

            d.add(new Field(Fields.DOCTYPE, ForumFields.TYPE_FORUM_POST, Field.Store.YES, Field.Index.NOT_ANALYZED));
            d.add(new Field(ForumFields.POST_ID, Long.toString(post.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));

            Field fTitle = new Field(Fields.TITLE, post.getSubject(), Field.Store.YES, Field.Index.ANALYZED);
            fTitle.setBoost(2);
            d.add(fTitle);

            Field fAltTitle = new Field(ForumFields.POST_BODY, post.getBody(), Field.Store.YES, Field.Index.ANALYZED);
            fAltTitle.setBoost(2);
            d.add(fAltTitle);

            Field fAuthor = new Field(ForumFields.POST_AUTHOR, post.getAuthor(), Field.Store.YES, Field.Index.ANALYZED);
            d.add(fAuthor);

            d.add(new Field(Fields.TITLE_UNANALYZED, post.getSubject(), Field.Store.NO, Field.Index.NOT_ANALYZED));

            d.add(new Field(Fields.CONTENT, allText, Field.Store.NO, Field.Index.ANALYZED));
            d.add(new Field(Fields.CONTENT_UNSTEMMED, allText, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));

            return d;
        } catch(Throwable e) {
            Log.error(getClass().getName(), "Exception creating index document for post id " + post.getId() + ": " + e.getMessage(), null, null);
            Log.error(getClass().getName(), e, null, null);
            e.printStackTrace();
            return null;
        }
    }


    public Term getDeleteTerm(String id) {
        return new Term("forumPostId", id);
    }

    public Term getDeleteAllTerm() {
        return new Term(Fields.DOCTYPE, getDocumentType());
    }

    public SearchHit createSearchHit() {
        return new ForumPostSearchHit();
    }

    public void processSearchHit(SearchHit searchHit, SearchHitContext searchHitContext, Document document) throws NotAuthorizedException {
        ForumPostSearchHit hit = (ForumPostSearchHit) searchHit;
        hit.setBody(document.get(ForumFields.POST_BODY));
        hit.setAuthor(document.get(ForumFields.POST_AUTHOR));
        hit.setTitle(document.get(Fields.TITLE));
    }

    public void setForumDao(ForumDao forumDao) {
        this.forumDao = forumDao;
    }
}
