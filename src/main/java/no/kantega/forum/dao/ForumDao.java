package no.kantega.forum.dao;

import no.kantega.embed.Embedly;
import no.kantega.embed.Oembed;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.publishing.common.Aksess;
import no.kantega.utilities.Http;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static no.kantega.utilities.Objects.nonNull;

public class ForumDao {
    private HibernateTemplate template;

    private Logger log = Logger.getLogger(ForumDao.class);

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    public ForumCategory saveOrUpdate(ForumCategory forumCategory) {
        template.saveOrUpdate(forumCategory);
        return forumCategory;
    }

    public Attachment saveOrUpdate(Attachment attachment) {
        template.saveOrUpdate(attachment);
        return attachment;
    }

    public ForumThread saveOrUpdate(ForumThread thread) {
        thread.setLastPostDate(new Date());
        template.saveOrUpdate(thread);
        updateThreadCount(thread.getForum().getId());
        return thread;
    }

    public Forum saveOrUpdate(Forum forum) {
        template.saveOrUpdate(forum);
        updateForumCount(forum.getForumCategory().getId());
        return forum;
    }

    public Post saveOrUpdate(Post post) {
        return saveOrUpdate(post, true);
    }

    public Post saveOrUpdate(Post post, boolean updateLastPostDateOnThread) {
        URL link = getLink(post);
        if (nonNull(link)) {
            Embedly embedly = getEmbedly();
            if (nonNull(embedly)) {
                Oembed oembed = embedly.oembed()
                        .withUrl(link)
                        .withNostyle(true)
                        .build();
                try {
                    try (Http.HttpRequest request = oembed.getHttpRequest()) {
                        try (Http.HttpResponse response = request.getResponse()) {
                            if (200 == response.getResponseCode()) {
                                try (InputStream inputStream = response.getInputStream()) {
                                    post.setEmbed(StreamUtils.copyToString(inputStream, Charset.forName("UTF-8")));
                                }
                            }
                        }
                    }
                } catch (Exception cause) {
                    log.error("Could not perform embed", cause);
                    post.setEmbed(null);
                }

            }
        } else {
            post.setEmbed(null);
        }
        template.saveOrUpdate(post);
        updatePostCount(post.getThread().getId());



        if (updateLastPostDateOnThread) {
            ForumThread t = getThread(post.getThread().getId());
            t.setLastPostDate(new Date());
            saveOrUpdate(t);
        }
        return post;
    }

    private URL getLink(Post post) {
        URL link = null;
        if (nonNull(post)) {
            String body = post.getBody();
            String[] split = body.split("\\s");
            if (split.length == 1) {
                if (nonNull(body)) {
                    try {
                        link = new URL(body.trim());
                    } catch (Exception cause) {
                    }

                }
            }
        }
        return link;
    }

    private Embedly getEmbedly() {
        Embedly embedly = null;
        try{
            URL apiUrl = new URL(Aksess.getConfiguration().getString("embed.ly.api.url"));
            String apiUrlEncoding = Aksess.getConfiguration().getString("embed.ly.api.url.encoding");
            String apiKey = Aksess.getConfiguration().getString("embed.ly.api.key");
            apiUrlEncoding = nonNull(apiUrlEncoding) ? apiUrlEncoding : "UTF-8";
            if (nonNull(apiUrl) && nonNull(apiKey)) {
                embedly = new Embedly(apiUrl, apiKey, apiUrlEncoding);
            }
        } catch (Exception cause) {

        }
        return embedly;
    }

    public Post approve(Post post) {
        post.setApproved(true);
        saveOrUpdate(post);

        ForumThread thread = post.getThread();
        thread.setApproved(true);
        saveOrUpdate(thread);

        updatePostCount(thread.getId());
        updateThreadCount(thread.getForum().getId());
        return post;
    }

    // delete
    public void delete(Post post) {
        template.delete(post);
        updatePostCount(post.getThread().getId());
    }

    public void delete(ForumThread thread) {
        template.delete(thread);
        updateThreadCount(thread.getForum().getId());
    }

    public void delete(Attachment attachment) {
        template.delete(attachment);
    }

    public void delete(Forum forum) {
        template.delete(forum);
        updateForumCount(forum.getForumCategory().getId());
    }

    public void delete(ForumCategory forumCategory) {
        template.delete(forumCategory);
    }

    public List<ForumCategory> getForumCategories() {
        List<ForumCategory> list = template.find("from ForumCategory c left join fetch c.forums f order by c.name");

        // Remove duplicates since Hibernate duplicates forums
        Set<ForumCategory> setItems = new LinkedHashSet<>(list);
        return new ArrayList<>(setItems);
    }

    public List<Forum> getForums() {
        return template.find("from Forum f inner join fetch f.forumCategory c");
    }

    public List<Forum> getForumsWithUserPostings(final String userId) {
        return template.execute(new HibernateCallback<List<Forum>>() {
            public List<Forum> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Forum f inner join fetch f.forumCategory c where f.id in (select t.forum.id from ForumThread t where t.id in (select p.thread.id from Post p where p.owner = ?))");
                query.setString(0, userId);
                return query.list();
            }
        });
    }

    /**
     *
     * @param userId
     * @param max - max number of posts to get. Specify -1 for all.
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Post> getUserPostings(final String userId, final int max) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                StringBuilder where = new StringBuilder();
                String[] users = userId.split(",");
                for (int i = 0, usersLength = users.length; i < usersLength; i++) {
                    if (i > 0) {
                        where.append(",");
                    }
                    where.append("?");
                }
                Query query = session.createQuery("from Post p where p.owner IN (" + where.toString() + ") order by p.postDate desc");
                if (max != -1) {
                    query.setMaxResults(max);
                }
                for (int i = 0, usersLength = users.length; i < usersLength; i++) {
                    String user = users[i];
                    query.setString(i, user.trim());
                }

                return query.list();
            }
        });
    }

    public List<ForumThread> getThreadsWhereUserHasPosted(final String userId, final int maxResults, final int firstResult, final int forumId, final int forumCategoryId, ThreadSortOrder order) {
         List<ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {

                Query queryThreadIds = session.createSQLQuery("select distinct(threadId) from forum_post where owner=?");

                queryThreadIds.setString(0, userId);

                List<Number> threadIds = queryThreadIds.list();
                if (threadIds.size() == 0) {
                    return new ArrayList<>();
                }

                StringBuilder q = new StringBuilder();
                q.append("from ForumThread t where t.id IN (");
                for (int i = 0; i < threadIds.size(); i++) {
                    if (i > 0) {
                        q.append(",");
                    }
                    q.append("?");
                }
                q.append(")");
                if (forumId != -1) {
                    q.append(" and t.forum.id = ").append(forumId);
                }
                if (forumCategoryId != -1) {
                    q.append(" and t.forum.forumCategory.id = ").append(forumCategoryId);
                }

                q.append(" order by t.lastPostDate desc");
                Query query = session.createQuery(q.toString());
                if (maxResults != -1) {
                    query.setMaxResults(maxResults);
                }

                if (firstResult > -1) {
                    query.setFirstResult(firstResult);
                }

                for (int i = 0; i < threadIds.size(); i++) {
                    Number tId = threadIds.get(i);
                    query.setLong(i, tId.longValue());
                }

                List<ForumThread> threads = query.list();
                for (ForumThread thread : threads) {
                    Hibernate.initialize(thread.getPosts());
                }
                return threads;
            }
        });


        return ret;
    }

    public List<Post> getLastPosts(final int n) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
                query.setString(0, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });
    }

    public int getNewPostCountInForum(final long forumId, final Date lastVisit) {

        Number n = (Number) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {

                Query q = session.createQuery("select count(*) from Post p where p.thread.forum.id=? and p.approved = 'Y' and p.postDate > ?");

                q.setLong(0, forumId);
                q.setTimestamp(1, lastVisit);

                return q.uniqueResult();
            }
        });

        return n.intValue();
    }

    public List<Post> getLastPostsInForum(final long forumId, final int n) {
        return  template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.thread.forum.id = ? and p.approved = ? order by p.id desc");
                query.setLong(0, forumId);
                query.setString(1, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });
    }

    public List<Post> getLastPostsInForums(final long forumIds[], final int n) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                StringBuilder whereClause = new StringBuilder();
                for (int i = 0; i < forumIds.length; i++) {
                    if (i > 0) {
                        whereClause.append(" or ");
                    }
                    whereClause.append("p.thread.forum.id = ? ");
                }
                Query query = session.createQuery("from Post p where (" + whereClause + ") and p.approved = ? order by p.id desc");
                for (int i = 0; i < forumIds.length; i++) {
                    query.setLong(i, forumIds[i]);

                }
                query.setString(forumIds.length, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });
    }

    public List<Post> getPostsAfterDate(final Date lastVisit) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? and p.postDate > ? order by p.id desc");
                query.setString(0, "Y");
                query.setTimestamp(1, lastVisit);
                return query.list();
            }
        });
    }

    public int getNumberOfThreadsAfterDateInForumsNotByUser(final int[] forumIds, final Timestamp lastRefresh, final String username){
        Number n = (Number) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuilder query = new StringBuilder();

                //A single forumId of -1 means "show all threads from all forumIds"
                //In that case, ignore t.forum.id in the match
                boolean shouldShowAll = forumIds.length == 1 && forumIds[0] == -1;

                if(shouldShowAll){
                    query.append("select count(*) from ForumThread t where t.createdDate > ? and t.owner <> ?");
                } else {
                    query.append("select count(*) from ForumThread t where t.createdDate > ? and t.owner <> ? and t.forum.id in (");
                    for (int i = 0; i < forumIds.length; i++) {
                        if (i > 0) query.append(",");
                        query.append("?");
                    }
                    query.append(")");
                }
                Query q = session.createQuery(query.toString());

                q.setTimestamp(0, lastRefresh);
                q.setString(1, username);

                if(!shouldShowAll){
                    for (int i = 0; i < forumIds.length; i++) {
                        int forumId = forumIds[i];
                        q.setLong(i+2, (long)forumId);
                    }
                }

                return q.uniqueResult();
            }
        });

        return n.intValue();
    }

    public int getNumberOfThreadsAfterDateInForumCategoryNotByUser(final long forumCategoryId, final Timestamp lastRefresh, final String username){
        Number n = (Number) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("select count(*) from ForumThread t where t.forum.forumCategory.id = ? and t.createdDate > ? and t.owner <> ?");
                q.setLong(0, forumCategoryId);
                q.setTimestamp(1, lastRefresh);
                q.setString(2, username);
                return q.uniqueResult();
            }
        });

        return n.intValue();
    }


    public List<Post> getAllPosts() {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
                query.setString(0, "Y");
                return query.list();
            }
        });
    }


    public List<Post> getLastPostsInThread(final long threadId, final int n) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.thread.id = ? and p.approved = ? order by p.id desc");
                query.setLong(0, threadId);
                query.setString(1, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });

    }

    public List<Post> getUnapprovedPosts() {
        return  template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
                query.setString(0, "N");
                return query.list();
            }
        });
    }

    public ForumCategory getPopulatedForumCategory(final long forumCategoryId) {
        return template.execute(new HibernateCallback<ForumCategory>() {
            public ForumCategory doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, forumCategoryId);
                fc.getForums().size();
                return fc;
            }
        });
    }

    public Forum getPopulatedForum(final long forumId) {
        return  template.execute(new HibernateCallback<Forum>() {
            public Forum doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, forumId);
                f.getThreads().size();
                f.getGroups().size();
                return f;
            }
        });
    }

    public ForumThread getPopulatedThread(final long threadId) {
        return template.execute(new HibernateCallback<ForumThread>() {
            public ForumThread doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(ForumThread.class, threadId);
                t.getPosts().size();
                t.getForum().getId();
                return t;
            }
        });
    }

    public Post getPopulatedPost(final long postId) {
        return template.execute(new HibernateCallback<Post>() {
            public Post doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Post.class, postId);
                p.getAttachments().size();
                return p;
            }
        });
    }

    public Forum getForum(final long forumId) {
        List forums = template.find("from Forum f inner join fetch f.forumCategory c where f.id=?", forumId);
        return forums.isEmpty() ? null : (Forum) forums.get(0);
    }

    public ForumThread getThread(final long threadId, final boolean includePosts) {
        ForumThread thread = template.execute(new HibernateCallback<ForumThread>() {
            @Override
            public ForumThread doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from ForumThread t inner join fetch t.forum f where t.id=?");
                query.setLong(0, threadId);
                List<ForumThread> threads = query.list();
                if (threads.isEmpty()) {
                    return null;
                } else if (includePosts) {
                    Hibernate.initialize(threads.get(0).getPosts());
                }
                return threads.get(0);
            }
        });
        return thread;
    }
    public ForumThread getThread(final long threadId) {
        return getThread(threadId, false);
    }

    public Attachment getAttachment(final long attachmentId) {
        return template.get(Attachment.class, attachmentId);
    }

    public Forum getForumForAttachment(final Attachment attachment){
        return template.execute(new HibernateCallback<Forum>() {
            @Override
            public Forum doInHibernate(Session session) throws HibernateException, SQLException {
                Query attachmentQuery = session.createQuery("from Attachment a where a.id = ?");
                attachmentQuery.setLong(0, attachment.getId());
                Attachment attachement = (Attachment) attachmentQuery.uniqueResult();
                return attachement.getPost().getThread().getForum();
            }
        });
    }

    public ForumCategory getForumCategory(final long forumCategoryId) {
        return template.execute(new HibernateCallback<ForumCategory>() {
            public ForumCategory doInHibernate(Session session) throws HibernateException {
                Query q  = session.createQuery("from ForumCategory c where c.id = ?");
                q.setLong(0, forumCategoryId);

                List categories = q.list();
                ForumCategory category = null;
                if (!categories.isEmpty()) {
                    category = (ForumCategory) categories.get(0);
                    for (Object forum : category.getForums()) {
                        Hibernate.initialize(forum);
                    }
                }
                return category;
            }
        });
    }

    public Post getPost(final long postId) {
        return template.get(Post.class, postId);
    }

    public boolean postGotChildren(final Post p) {
        List children = template.find("from Post p where p.replyToId=?",p.getId());
        return (children.size() > 0);
    }

    public List<ForumThread> getThreadsInForums(final int forumIds[], final int firstResult, final int maxResult, final ThreadSortOrder order) {

         List <ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {
                StringBuilder query = new StringBuilder();
                query.append("from ForumThread t where t.approved = ? and t.forum.id in (");
                for (int i = 0; i < forumIds.length; i++) {
                    if (i > 0) query.append(",");
                    query.append("?");
                }
                if (order.equals(ThreadSortOrder.SORT_BY_DEFAULT)){
                    query.append(") order by t.lastPostDate desc");
                }
                else if(order.equals(ThreadSortOrder.SORT_BY_DATE_CREATED)){
                    query.append(") order by t.createdDate desc");
                }


                Query q  = session.createQuery(query.toString());
                q.setString(0, "Y");

                for (int i = 0; i < forumIds.length; i++) {
                    int forumId = forumIds[i];
                    q.setLong(i+1, (long)forumId);
                }

                q.setFirstResult(firstResult);
                q.setMaxResults(maxResult);

                List <ForumThread> threads = (List<ForumThread>)q.list();
                for (ForumThread thread : threads) {
                    Hibernate.initialize(thread.getPosts());
                }
                return threads;
            }
        });

        return ret;

    }

    public List<ForumThread> getThreadsInForum(final long forumId, final int firstResult, final int maxResult, ThreadSortOrder order) {
        return getThreadsInForums(new int[] {(int) forumId}, firstResult, maxResult, order);
    }

    public List<ForumThread> getThreadsInForumCategory(final long forumCategoryId, final int firstResult, final int maxResult, final ThreadSortOrder order) {

        List <ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {

                StringBuilder query = new StringBuilder();
                query.append("from ForumThread t where t.forum.forumCategory.id = ? and t.approved = ? ");
                if(order.equals(ThreadSortOrder.SORT_BY_DEFAULT)){
                    query.append("order by t.lastPostDate desc");
                }
                else if(order.equals(ThreadSortOrder.SORT_BY_DATE_CREATED)){
                    query.append("order by t.createdDate desc");
                }

                Query q  = session.createQuery(query.toString());
                q.setLong(0, forumCategoryId);
                q.setString(1, "Y");
                q.setFirstResult(firstResult);
                q.setMaxResults(maxResult);

                List <ForumThread> threads = (List<ForumThread>)q.list();
                for (ForumThread thread : threads) {
                    Hibernate.initialize(thread.getPosts());
                }
                return threads;
            }
        });



        return ret;

    }


    public List<Post> getPostsInThread(final long id, final int firstResult, final int maxResult) {
        return getPostsInThread(id, firstResult, maxResult, false);
    }

    public List<Post> getPostsInThread(final long id, final int firstResult, final int maxResult, final boolean reverse) {
        return template.execute(new HibernateCallback<List<Post>>() {
            public List<Post> doInHibernate(Session session) throws HibernateException {
                Query q  = session.createQuery("from Post p where p.thread.id = ? and approved = ? order by p.id " +(reverse ? "desc" : "asc"));
                q.setLong(0, id);
                q.setString(1, "Y");

                q.setFirstResult(firstResult);
                if(maxResult > 0) {
                    q.setMaxResults(maxResult);
                }

                return q.list();
            }
        });
    }

    public void updateForumCount(final long categoryId) {

        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                ForumCategory category = (ForumCategory) session.get(ForumCategory.class, categoryId);

                Query q = session.createQuery("select count(*) from Forum f where f.forumCategory.id=?");
                q.setLong(0, categoryId);

                Number num = (Number) q.uniqueResult();
                category.setNumForums(num.intValue());
                session.saveOrUpdate(category);
                return null;

            }
        });
    }

    public void updateThreadCount(final long forumId) {

        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {

                Forum forum = (Forum) session.get(Forum.class, forumId);

                Query q = session.createQuery("select count(*) from ForumThread t where t.forum.id = ? and approved = ?");
                q.setLong(0, forumId);
                q.setString(1, "Y");
                Number num = (Number) q.uniqueResult();
                forum.setNumThreads(num.intValue());
                session.saveOrUpdate(forum);
                return null;

            }
        });
    }

    public void updatePostCount(final long threadId) {

        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {

                ForumThread thread = (ForumThread) session.get(ForumThread.class, threadId);

                Query q = session.createQuery("select count(*) from Post p where p.thread.id = ? and approved = ?");
                q.setLong(0, threadId);
                q.setString(1, "Y");

                Number num = (Number) q.uniqueResult();
                thread.setNumPosts(num.intValue());
                session.saveOrUpdate(thread);
                return null;

            }
        });
    }


    public long getThreadAboutContent(final int contentId) {

        List l = (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("select t.id from ForumThread t where t.contentId=?");
                q.setInteger(0, contentId);
                return q.list();
            }
        });


        if(l.size() == 0) {
            return -1;
        } else {
            return ((Number)l.get(0)).longValue();
        }
    }


    public int getPostCountBefore(final long postId) {

        Number n = (Number) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {

                Post p = (Post) session.get(Post.class, postId);

                Query q = session.createQuery("select count(*) from Post p where p.thread.id=? and p.id < ? and approved = 'Y'");

                q.setLong(0, p.getThread().getId());
                q.setLong(1, p.getId());

                return q.uniqueResult();
            }
        });

        return n.intValue();
    }

    /**
     * Get posts count in threads where participant has posted.
     * Posts count does not include the participant's own posts
     * @param participant The participant (userId)
     * @param period The period in time
     * @return The thread identifiers
     */
    public Long getPostCountInThreadsWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        Long count = (Long) template.execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT COUNT(*) AS result FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?)");

                query.setString(0, participant);
                query.setString(1, participant);

                Timestamp start = toTimestamp(period.getStart());
                query.setTimestamp(2, start);
                query.setTimestamp(3, start);

                Timestamp end = toTimestamp(period.getEnd());
                query.setTimestamp(4, end);
                query.setTimestamp(5, end);

                query.addScalar("result", Hibernate.LONG);

                return query.uniqueResult();
            }
        });
        return count;
    }

    public List<Post> getPostsInThreadsWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        List<Post> posts = (List<Post>) template.execute(new HibernateCallback<List<Post>>() {
            @Override
            public List<Post> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT op.* FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?)");

                query.setString(0, participant);
                query.setString(1, participant);

                Timestamp start = toTimestamp(period.getStart());
                query.setTimestamp(2, start);
                query.setTimestamp(3, start);

                Timestamp end = toTimestamp(period.getEnd());
                query.setTimestamp(4, end);
                query.setTimestamp(5, end);

                query.addEntity(Post.class);

                return query.list();
            }
        });
        return posts;
    }

    public Long getThreadCountWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        Long count = (Long) template.execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT COUNT(DISTINCT op.threadId) AS result FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?)");

                query.setString(0, participant);
                query.setString(1, participant);

                Timestamp start = toTimestamp(period.getStart());
                query.setTimestamp(2, start);
                query.setTimestamp(3, start);

                Timestamp end = toTimestamp(period.getEnd());
                query.setTimestamp(4, end);
                query.setTimestamp(5, end);

                query.addScalar("result", Hibernate.LONG);

                return query.uniqueResult();
            }
        });
        return count;
    }

    public List<Long> getThreadsWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        List<Long> count = (List<Long>) template.execute(new HibernateCallback<List<Long>>() {
            @Override
            public List<Long> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT t.threadId as threadId FROM forum_thread t WHERE t.threadId IN (SELECT DISTINCT op.threadId FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?))");

                query.setString(0, participant);
                query.setString(1, participant);

                Timestamp start = toTimestamp(period.getStart());
                query.setTimestamp(2, start);
                query.setTimestamp(3, start);

                Timestamp end = toTimestamp(period.getEnd());
                query.setTimestamp(4, end);
                query.setTimestamp(5, end);

                query.addScalar("threadId", Hibernate.LONG);

                return query.list();
            }
        });
        return count;
    }

    public Instant getLatestPostDateOfPosts(final String username) {
        return template.execute(new HibernateCallback<Instant>() {
            @Override
            public Instant doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT MAX(p.postDate) as postDate FROM forum_post p WHERE p.owner = ?");
                query.setString(0, username);
                query.addScalar("postDate", Hibernate.TIMESTAMP);
                return toInstant((Timestamp) query.uniqueResult());
            }
        });
    }

    public Instant getLatestModifiedDateTimeOfPosts(final String username) {
        return template.execute(new HibernateCallback<Instant>() {
            @Override
            public Instant doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT MAX(p.modifiedDate) as modifiedDate FROM forum_post p WHERE p.owner = ?");
                query.setString(0, username);
                query.addScalar("modifiedDate", Hibernate.TIMESTAMP);
                return toInstant((Timestamp) query.uniqueResult());
            }
        });
    }

    public Post getFirstPostInThread(final long threadId) {
        return template.execute(new HibernateCallback<Post>() {
            @Override
            public Post doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT TOP 1 * FROM forum_post p WHERE p.threadId = ? ORDER BY p.postId ASC");
                query.setLong(0, threadId);
                query.addEntity(Post.class);
                return (Post) query.uniqueResult();
            }
        });
    }

    private Timestamp toTimestamp(DateTime dateTime) {
        return dateTime != null ? new Timestamp(dateTime.getMillis()) : null;
    }

    private Instant toInstant(Timestamp timestamp) {
        return timestamp != null ? new Instant(timestamp.getTime()) : null;
    }

    public List<ForumThread> getThreads(final String participant, final int offset, final int limit) {
        List<ForumThread> count = (List<ForumThread>) template.execute(new HibernateCallback<List<ForumThread>>() {
            @Override
            public List<ForumThread> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery("SELECT\n" +
                        "  t.threadId, t.name, t.description,\n" +
                        "  t.forumId, t.createdDate, t.numPosts, t.owner, t.contentId,\n" +
                        "  t.approved, MAX(p.postDate) AS lastPostDate, MAX(p.modifiedDate) AS modifiedDate\n" +
                        "FROM\n" +
                        "  forum_post p\n" +
                        "  JOIN\n" +
                        "    forum_thread t\n" +
                        "  ON p.threadId = t.threadId\n" +
                        "WHERE\n" +
                        "  p.threadId IN (SELECT DISTINCT a.threadId FROM forum_post a WHERE a.owner = ?)\n" +
                        "GROUP BY \n" +
                        "  t.threadId, t.name, t.description, t.forumId, t.createdDate, \n" +
                        "  t.numPosts, t.owner, t.contentId, t.approved, t.lastPostDate, t.modifiedDate\n" +
                        "ORDER BY modifiedDate DESC, lastPostDate DESC");

                query.setString(0, participant);
                query.setFetchSize(limit);

                query.addEntity(ForumThread.class);

                Iterator<ForumThread> forumThreads = query.list().iterator();
                List<ForumThread> page = new ArrayList<ForumThread>();
                int start = offset;
                int end = offset + limit;
                int at  = 0;
                while (forumThreads.hasNext() && at < end) {
                    if (at >= start) {
                        page.add(forumThreads.next());
                    } else {
                        forumThreads.next();
                    }
                    at++;
                }
                return page;
            }
        });
        return count;
    }

    private void setString(PreparedStatement statement, int index, String string) throws SQLException {
        statement.setString(index, string);
    }

    private BigDecimal toBigDecimal(Integer integer) {
        return integer != null ? BigDecimal.valueOf(integer.longValue()) : null;
    }

    public List<ForumThread> getThreadsStartingAt(final String username, final Long startAtThreadId, final Integer numberOfThreads, final Boolean includePosts) {
        List<ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {

                Query queryThreadIds = session.createSQLQuery("select distinct(threadId) from forum_post where owner=?");

                queryThreadIds.setString(0, username);

                List<String> threadIds = toListOfStrings(queryThreadIds.list());
                if (threadIds.isEmpty()) {
                    return new ArrayList<>();
                }

                StringBuilder q = new StringBuilder();
                q.append("from ForumThread t where t.id IN (")
                        .append(StringUtils.join(threadIds, ", ")) // Safe because it is always longs
                        .append(")");

                q.append(" order by t.lastPostDate desc");
                Query query = session.createQuery(q.toString());

                query.setFetchSize(numberOfThreads);

                List<ForumThread> selected = new ArrayList<ForumThread>();
                List<ForumThread> candidates = query.list();
                int count = -1;
                for (ForumThread thread : candidates) {
                    if (count >= numberOfThreads) {
                        break;
                    }
                    if (count > -1) {
                        selected.add(thread);
                        count++;
                    }
                    if (thread.getId() == startAtThreadId) {
                        count++;
                    }
                }
                if (includePosts) {
                    for (ForumThread thread : selected) {
                        Hibernate.initialize(thread.getPosts());
                    }
                }
                return selected;
            }
        });
        return ret;
    }

    public List<ForumThread> getThreadsEndingAt(final String username, final Long endAtThreadId, final Integer numberOfThreads, final Boolean includePosts) {
        List<ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {

                Query queryThreadIds = session.createSQLQuery("select distinct(threadId) from forum_post where owner=?");

                queryThreadIds.setString(0, username);

                List<String> threadIds = toListOfStrings(queryThreadIds.list());
                if (threadIds.isEmpty()) {
                    return new ArrayList<>();
                }

                StringBuilder q = new StringBuilder();
                q.append("from ForumThread t where t.id IN (")
                        .append(StringUtils.join(threadIds, ", ")) // Safe because it is always longs
                        .append(")");

                q.append(" order by t.lastPostDate asc");
                Query query = session.createQuery(q.toString());

                query.setFetchSize(numberOfThreads);

                List<ForumThread> selected = new ArrayList<ForumThread>();
                List<ForumThread> candidates = query.list();
                int count = -1;
                for (ForumThread thread : candidates) {
                    if (count >= numberOfThreads) {
                        break;
                    }
                    if (count > -1) {
                        selected.add(thread);
                        count++;
                    }
                    if (thread.getId() == endAtThreadId) {
                        count++;
                    }
                }
                if (includePosts) {
                    for (ForumThread thread : selected) {
                        Hibernate.initialize(thread.getPosts());
                    }
                }
                return selected;
            }
        });
        return ret;
    }

    public List<ForumThread> getThreads(final String username, final Integer numberOfThreads, final boolean includePosts) {
        List<ForumThread> ret = template.execute(new HibernateCallback<List<ForumThread>>() {
            public List<ForumThread> doInHibernate(Session session) throws HibernateException {

                Query queryThreadIds = session.createSQLQuery("select distinct(threadId) from forum_post where owner=?");

                queryThreadIds.setString(0, username);

                List<String> threadIds = toListOfStrings(queryThreadIds.list());
                if (threadIds.isEmpty()) {
                    return new ArrayList<>();
                }

                StringBuilder q = new StringBuilder();
                q.append("from ForumThread t where t.id IN (")
                        .append(StringUtils.join(threadIds, ", ")) // Safe because it is always longs
                        .append(")");

                q.append(" order by t.lastPostDate desc");
                Query query = session.createQuery(q.toString());

                if (numberOfThreads != -1) {
                    query.setMaxResults(numberOfThreads);
                }

                List<ForumThread> threads = query.list();
                if (includePosts) {
                    for (ForumThread thread : threads) {
                        Hibernate.initialize(thread.getPosts());
                    }
                }
                return threads;
            }
        });
        return ret;
    }

    public List<Long> getCategoryByForums(List<Long> forumIds) {
        String in = forumIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return template.execute(new HibernateCallback<List<Long>>() {
            @Override
            public List<Long> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT DISTINCT forumCategoryId FROM forum_forum WHERE forumId IN (" + in + ")");
                sqlQuery.addScalar("forumCategoryId", Hibernate.LONG);
                return sqlQuery.list();
            }
        });
    }

    private List<String> toListOfStrings(List<Number> listOfNumbers) {
        if (listOfNumbers == null) return null;
        List<String> listOfStrings = new ArrayList<>(listOfNumbers.size());
        for (Number number : listOfNumbers) {
            if (number != null) {
                listOfStrings.add(number.toString());
            }
        }
        return listOfStrings;
    }
}
