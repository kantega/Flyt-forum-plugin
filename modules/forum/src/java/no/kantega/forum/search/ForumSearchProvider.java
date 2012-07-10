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
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
            try{
                Document d = getForumDocument((Post)posts.get(i));
                handler.handleDocument(d);
                reporter.reportProgress(i, "forum-post", posts.size());
            } catch (Throwable e) {
                Log.error(this.getClass().getName(), "Caught throwable during indexing of document id: " +((Post)posts.get(i)).getId() +"", null, null);
                Log.error(this.getClass().getName(), e, null, null);
            }
        }
    }

    @Override
    public void provideDocuments(DocumentProviderHandler documentProviderHandler, ProgressReporter progressReporter, Map map) {
        provideDocuments(documentProviderHandler, progressReporter);
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

            Field fOwner = new Field(ForumFields.POST_OWNER, post.getOwner(), Field.Store.YES, Field.Index.ANALYZED);
            d.add(fOwner);

            Field fPostId = new Field(ForumFields.POST_ID, ""+post.getId(), Field.Store.YES, Field.Index.ANALYZED);
            d.add(fPostId);

            Field fThreadId = new Field(ForumFields.THREAD_ID, ""+post.getThread().getId(), Field.Store.YES, Field.Index.ANALYZED);
            d.add(fThreadId);

            d.add(new Field(Fields.TITLE_UNANALYZED, post.getSubject(), Field.Store.NO, Field.Index.NOT_ANALYZED));

            d.add(new Field(Fields.CONTENT, allText, Field.Store.NO, Field.Index.ANALYZED));
            d.add(new Field(Fields.CONTENT_UNSTEMMED, allText, Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.YES));

            d.add(new Field(Fields.LAST_MODIFIED, DateTools.dateToString(post.getPostDate(), DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.NOT_ANALYZED));

            return d;
        } catch(Throwable e) {
            Log.error(getClass().getName(), "Exception creating index document for post id " + post.getId() + ": " + e.getMessage(), null, null);
            Log.error(getClass().getName(), e, null, null);
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
        hit.setOwner(document.get(ForumFields.POST_OWNER));
        hit.setPostId(document.get(ForumFields.POST_ID));
        hit.setPostThreadId(document.get(ForumFields.THREAD_ID));
        try {
            hit.setPostDate(DateTools.stringToDate(document.get(Fields.LAST_MODIFIED)));
        } catch (ParseException e) {
            Log.error(getClass().getName(), e, null, null);
        }
    }

    public void setForumDao(ForumDao forumDao) {
        this.forumDao = forumDao;
    }
}
