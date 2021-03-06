package no.kantega.forum.dao;

import no.kantega.embed.Embedder;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.LongType;
import org.hibernate.type.TimestampType;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static no.kantega.utilities.Objects.nonNull;

public class ForumDao {
    private HibernateTemplate template;

    @Autowired
    private Embedder embedder;

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    private <T> void doSaveOrUpdate(T object){
        template.executeWithNativeSession(session -> {
            FlushMode flushMode = session.getFlushMode();
            session.setFlushMode(FlushMode.COMMIT);
            session.saveOrUpdate(object);
            session.flush();
            session.setFlushMode(flushMode);
            return null;
        });
    }

    public ForumCategory saveOrUpdate(ForumCategory forumCategory) {
        doSaveOrUpdate(forumCategory);
        return forumCategory;
    }

    public Attachment saveOrUpdate(Attachment attachment) {
        doSaveOrUpdate(attachment);
        return attachment;
    }

    public ForumThread saveOrUpdate(ForumThread thread) {
        thread.setLastPostDate(new Date());
        doSaveOrUpdate(thread);
        updateThreadCount(thread.getForum().getId());
        return thread;
    }

    public Forum saveOrUpdate(Forum forum) {
        doSaveOrUpdate(forum);
        updateForumCount(forum.getForumCategory().getId());
        return forum;
    }

    public Post saveOrUpdate(Post post) {
        return saveOrUpdate(post, true);
    }

    public Post saveOrUpdate(Post post, boolean updateLastPostDateOnThread) {
        List<URL> links = getLinks(post);
        if (!links.isEmpty()) {
            post.setEmbed(embedder.getEmbeddedContent(links));
        } else {
            post.setEmbed(null);
        }
        doSaveOrUpdate(post);
        updatePostCount(post.getThread().getId());



        if (updateLastPostDateOnThread) {
            ForumThread t = getThread(post.getThread().getId());
            t.setLastPostDate(new Date());
            doSaveOrUpdate(t);
        }
        return post;
    }

    //URLs starting with http://, https://, or ftp://
    private static final Pattern pattern1 = Pattern.compile("(\\b(https?|ftp)://[-\\p{L}0-9+&@#/%?=~_|!:,.;]*[-\\p{L}0-9+&@#/%=~_|])", Pattern.CASE_INSENSITIVE);
    //URLs starting with "www." (without // before it, or it'd re-link the ones done above).
    private static final Pattern pattern2 = Pattern.compile("(^|[^/])(www\\.[\\S]+)(\\b)", Pattern.CASE_INSENSITIVE);

    private List<URL> getLinks(Post post) {
        List<URL> links = new LinkedList<>();
        if (nonNull(post)) {
            String body = post.getBody();
            if (nonNull(body)) {
                Matcher matcher = pattern1.matcher(body);
                while (matcher.find()) {
                    try {
                        String string = matcher.group(1);
                        links.add(new URL(string));
                    } catch (Exception cause) {
                    }
                }
                matcher = pattern2.matcher(body);
                while (matcher.find()) {
                    try {
                        String string = matcher.group(2);
                        links.add(new URL("http://" + string));
                    } catch (Exception cause) {
                    }
                }
            }
        }
        return links;
    }



    public Post approve(Post post) {
        post.setApproved(true);
        doSaveOrUpdate(post);

        ForumThread thread = post.getThread();
        thread.setApproved(true);
        doSaveOrUpdate(thread);

        updatePostCount(thread.getId());
        updateThreadCount(thread.getForum().getId());
        return post;
    }

    // delete

    private <T> void doDelete(T object){
        template.executeWithNativeSession(session -> {
            FlushMode flushMode = session.getFlushMode();
            session.setFlushMode(FlushMode.COMMIT);
            session.delete(object);
            session.flush();
            session.setFlushMode(flushMode);
            return null;
        });
    }

    public void delete(Post post) {
        doDelete(post);
        updatePostCount(post.getThread().getId());
    }

    public void delete(ForumThread thread) {
        doDelete(thread);
        updateThreadCount(thread.getForum().getId());
    }

    public void delete(Attachment attachment) {
        doDelete(attachment);
    }

    public void delete(Forum forum) {
        doDelete(forum);
        updateForumCount(forum.getForumCategory().getId());
    }

    public void delete(ForumCategory forumCategory) {
        doDelete(forumCategory);
    }

    public List<ForumCategory> getForumCategories() {
        List<ForumCategory> list = (List<ForumCategory>) template.find("from ForumCategory c left join fetch c.forums f order by c.name");

        // Remove duplicates since Hibernate duplicates forums
        Set<ForumCategory> setItems = new LinkedHashSet<>(list);
        return new ArrayList<>(setItems);
    }

    public List<Forum> getForums() {
        return (List<Forum>) template.find("from Forum f inner join fetch f.forumCategory c");
    }

    public List<Forum> getForumsWithUserPostings(final String userId) {
        return template.execute(session -> {
            Query query = session.createQuery("from Forum f inner join fetch f.forumCategory c where f.id in (select t.forum.id from ForumThread t where t.id in (select p.thread.id from Post p where p.owner = ?))");
            query.setString(0, userId);
            return (List<Forum>) query.list();
        });
    }

    /**
     *
     * @param userId - owner of posts
     * @param max - max number of posts to get. Specify -1 for all.
     * @return posts with userId as owner
     */
    public List<Post> getUserPostings(final String userId, final int max) {
        return template.execute(session -> {
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

            return (List<Post>) query.list();
        });
    }

    public List<ForumThread> getThreadsWhereUserHasPosted(final String userId, final int maxResults, final int firstResult, final int forumId, final int forumCategoryId, ThreadSortOrder order) {
        return template.execute(session -> {

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
        });
    }

    public List<Post> getLastPosts(final int n) {
        return template.execute(session -> {
            Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
            query.setString(0, "Y");
            query.setMaxResults(n);
            return (List<Post>) query.list();
        });
    }

    public int getNewPostCountInForum(final long forumId, final Date lastVisit) {

        Number n = (Number) template.execute(session -> {

            Query q = session.createQuery("select count(*) from Post p where p.thread.forum.id=? and p.approved = 'Y' and p.postDate > ?");

            q.setLong(0, forumId);
            q.setTimestamp(1, lastVisit);

            return q.uniqueResult();
        });

        return n.intValue();
    }

    public List<Post> getLastPostsInForum(final long forumId, final int n) {
        return template.execute(session -> {
            Query query = session.createQuery("from Post p where p.thread.forum.id = ? and p.approved = ? order by p.id desc");
            query.setLong(0, forumId);
            query.setString(1, "Y");
            query.setMaxResults(n);
            return (List<Post>) query.list();
        });
    }

    public List<Post> getLastPostsInForums(final long forumIds[], final int n) {
        return template.execute(session -> {
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
            return (List<Post>) query.list();
        });
    }

    public List<Post> getPostsAfterDate(final Date lastVisit) {
        return template.execute(session -> {
            Query query = session.createQuery("from Post p where p.approved = ? and p.postDate > ? order by p.id desc");
            query.setString(0, "Y");
            query.setTimestamp(1, lastVisit);
            return (List<Post>) query.list();
        });
    }

    public int getNumberOfThreadsAfterDateInForumsNotByUser(final int[] forumIds, final Timestamp lastRefresh, final String username){
        Number n = (Number) template.execute(session -> {
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
        });

        return n.intValue();
    }

    public int getNumberOfThreadsAfterDateInForumCategoryNotByUser(final long forumCategoryId, final Timestamp lastRefresh, final String username){
        Number n = (Number) template.execute(session -> {
            Query q = session.createQuery("select count(*) from ForumThread t where t.forum.forumCategory.id = ? and t.createdDate > ? and t.owner <> ?");
            q.setLong(0, forumCategoryId);
            q.setTimestamp(1, lastRefresh);
            q.setString(2, username);
            return q.uniqueResult();
        });

        return n.intValue();
    }


    public List<Post> getAllPosts() {
        return template.execute(session -> {
            Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
            query.setString(0, "Y");
            return (List<Post>) query.list();
        });
    }


    public List<Post> getLastPostsInThread(final long threadId, final int n) {
        return template.execute(session -> {
            Query query = session.createQuery("from Post p where p.thread.id = ? and p.approved = ? order by p.id desc");
            query.setLong(0, threadId);
            query.setString(1, "Y");
            query.setMaxResults(n);
            return (List<Post>) query.list();
        });

    }

    public List<Post> getUnapprovedPosts() {
        return  template.execute(session -> {
            Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
            query.setString(0, "N");
            return (List<Post>) query.list();
        });
    }

    public ForumCategory getPopulatedForumCategory(final long forumCategoryId) {
        return template.execute(session -> {
            ForumCategory fc = session.get(ForumCategory.class, forumCategoryId);
            fc.getForums().size();
            return fc;
        });
    }

    public Forum getPopulatedForum(final long forumId) {
        return  template.execute(session -> {
            Forum f = session.get(Forum.class, forumId);
            f.getThreads().size();
            f.getGroups().size();
            return f;
        });
    }

    public ForumThread getPopulatedThread(final long threadId) {
        return template.execute(session -> {
            ForumThread t = session.get(ForumThread.class, threadId);
            t.getPosts().size();
            t.getForum().getId();
            return t;
        });
    }

    public Post getPopulatedPost(final long postId) {
        return template.execute(session -> {
            Post p = session.get(Post.class, postId);
            p.getAttachments().size();
            return p;
        });
    }

    public Forum getForum(final long forumId) {
        List forums = template.find("from Forum f inner join fetch f.forumCategory c where f.id=?", forumId);
        return forums.isEmpty() ? null : (Forum) forums.get(0);
    }

    public ForumThread getThread(final long threadId, final boolean includePosts) {
        ForumThread thread = template.execute(session -> {
            Query query = session.createQuery("from ForumThread t inner join fetch t.forum f where t.id=?");
            query.setLong(0, threadId);
            List<ForumThread> threads = query.list();
            if (threads.isEmpty()) {
                return null;
            } else if (includePosts) {
                Hibernate.initialize(threads.get(0).getPosts());
            }
            return threads.get(0);
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
        return template.execute(session -> {
            Query attachmentQuery = session.createQuery("from Attachment a where a.id = ?");
            attachmentQuery.setLong(0, attachment.getId());
            Attachment attachement = (Attachment) attachmentQuery.uniqueResult();
            return attachement.getPost().getThread().getForum();
        });
    }

    public ForumCategory getForumCategory(final long forumCategoryId) {
        return template.execute(session -> {
            Query q  = session.createQuery("from ForumCategory c where c.id = ?");
            q.setLong(0, forumCategoryId);

            List<ForumCategory> categories = q.list();
            ForumCategory category = null;
            if (!categories.isEmpty()) {
                category = categories.get(0);
                for (Object forum : category.getForums()) {
                    Hibernate.initialize(forum);
                }
            }
            return category;
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

         List <ForumThread> ret = template.execute(session -> {
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
         });

        return ret;

    }

    public List<ForumThread> getThreadsInForum(final long forumId, final int firstResult, final int maxResult, ThreadSortOrder order) {
        return getThreadsInForums(new int[] {(int) forumId}, firstResult, maxResult, order);
    }

    public List<ForumThread> getThreadsInForumCategory(final long forumCategoryId, final int firstResult, final int maxResult, final ThreadSortOrder order) {

        List <ForumThread> ret = template.execute(session -> {

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
        });



        return ret;

    }


    public List<Post> getPostsInThread(final long id, final int firstResult, final int maxResult) {
        return getPostsInThread(id, firstResult, maxResult, false);
    }

    public List<Post> getPostsInThread(final long id, final int firstResult, final int maxResult, final boolean reverse) {
        return template.execute(session -> {
            Query q  = session.createQuery("from Post p where p.thread.id = ? and approved = ? order by p.id " +(reverse ? "desc" : "asc"));
            q.setLong(0, id);
            q.setString(1, "Y");

            q.setFirstResult(firstResult);
            if(maxResult > 0) {
                q.setMaxResults(maxResult);
            }

            return (List<Post>) q.list();
        });
    }

    public void updateForumCount(final long categoryId) {

        template.execute(session -> {

            ForumCategory category = session.get(ForumCategory.class, categoryId);

            Query q = session.createQuery("select count(*) from Forum f where f.forumCategory.id=?");
            q.setLong(0, categoryId);

            Number num = (Number) q.uniqueResult();
            category.setNumForums(num.intValue());
            session.saveOrUpdate(category);
            return null;

        });
    }

    public void updateThreadCount(final long forumId) {

        template.execute(session -> {

            Forum forum = session.get(Forum.class, forumId);

            Query q = session.createQuery("select count(*) from ForumThread t where t.forum.id = ? and approved = ?");
            q.setLong(0, forumId);
            q.setString(1, "Y");
            Number num = (Number) q.uniqueResult();
            forum.setNumThreads(num.intValue());
            session.saveOrUpdate(forum);
            return null;

        });
    }

    public void updatePostCount(final long threadId) {

        template.execute(session -> {

            ForumThread thread = session.get(ForumThread.class, threadId);

            Query q = session.createQuery("select count(*) from Post p where p.thread.id = ? and approved = ?");
            q.setLong(0, threadId);
            q.setString(1, "Y");

            Number num = (Number) q.uniqueResult();
            thread.setNumPosts(num.intValue());
            session.saveOrUpdate(thread);
            return null;

        });
    }


    public long getThreadAboutContent(final int contentId) {

        List<Number> l = template.execute(session -> {
            Query q = session.createQuery("select t.id from ForumThread t where t.contentId=?");
            q.setInteger(0, contentId);
            return (List<Number>) q.list();
        });


        if(l.size() == 0) {
            return -1;
        } else {
            return l.get(0).longValue();
        }
    }


    public int getPostCountBefore(final long postId) {

        Number n = template.execute(session -> {

            Post p = session.get(Post.class, postId);

            Query q = session.createQuery("select count(*) from Post p where p.thread.id=? and p.id < ? and approved = 'Y'");

            q.setLong(0, p.getThread().getId());
            q.setLong(1, p.getId());

            return (Number) q.uniqueResult();
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
        Long count = template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT COUNT(*) AS result FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?)");

            query.setString(0, participant);
            query.setString(1, participant);

            Timestamp start = toTimestamp(period.getStart());
            query.setTimestamp(2, start);
            query.setTimestamp(3, start);

            Timestamp end = toTimestamp(period.getEnd());
            query.setTimestamp(4, end);
            query.setTimestamp(5, end);

            query.addScalar("result", LongType.INSTANCE);

            return (Long) query.uniqueResult();
        });
        return count;
    }

    public List<Post> getPostsInThreadsWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        List<Post> posts = template.execute(session -> {
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

            return (List<Post>) query.list();
        });
        return posts;
    }

    public Long getThreadCountWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        Long count = (Long) template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT COUNT(DISTINCT op.threadId) AS result FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?)");

            query.setString(0, participant);
            query.setString(1, participant);

            Timestamp start = toTimestamp(period.getStart());
            query.setTimestamp(2, start);
            query.setTimestamp(3, start);

            Timestamp end = toTimestamp(period.getEnd());
            query.setTimestamp(4, end);
            query.setTimestamp(5, end);

            query.addScalar("result", LongType.INSTANCE);

            return query.uniqueResult();
        });
        return count;
    }

    public List<Long> getThreadsWithActivityInPeriodWhereParticipantHasPosted(final String participant, final Interval period) {
        List<Long> count = template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT t.threadId as threadId FROM forum_thread t WHERE t.threadId IN (SELECT DISTINCT op.threadId FROM forum_post op WHERE op.threadId IN (SELECT DISTINCT pp.threadId FROM forum_post pp WHERE pp.owner = ?) AND op.owner <> ? AND (op.postDate > ? OR op.modifiedDate > ?) AND (op.postDate < ? OR op.modifiedDate < ?))");

            query.setString(0, participant);
            query.setString(1, participant);

            Timestamp start = toTimestamp(period.getStart());
            query.setTimestamp(2, start);
            query.setTimestamp(3, start);

            Timestamp end = toTimestamp(period.getEnd());
            query.setTimestamp(4, end);
            query.setTimestamp(5, end);

            query.addScalar("threadId", LongType.INSTANCE);

            return (List<Long>) query.list();
        });
        return count;
    }

    public Instant getLatestPostDateOfPosts(final String username) {
        return template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT MAX(p.postDate) as postDate FROM forum_post p WHERE p.owner = ?");
            query.setString(0, username);
            query.addScalar("postDate", TimestampType.INSTANCE);
            return toInstant((Timestamp) query.uniqueResult());
        });
    }

    public Instant getLatestModifiedDateTimeOfPosts(final String username) {
        return template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT MAX(p.modifiedDate) as modifiedDate FROM forum_post p WHERE p.owner = ?");
            query.setString(0, username);
            query.addScalar("modifiedDate", TimestampType.INSTANCE);
            return toInstant((Timestamp) query.uniqueResult());
        });
    }

    public Post getFirstPostInThread(final long threadId) {
        return template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT TOP 1 * FROM forum_post p WHERE p.threadId = ? ORDER BY p.postId ASC");
            query.setLong(0, threadId);
            query.addEntity(Post.class);
            return (Post) query.uniqueResult();
        });
    }

    private Timestamp toTimestamp(DateTime dateTime) {
        return dateTime != null ? new Timestamp(dateTime.getMillis()) : null;
    }

    private Instant toInstant(Timestamp timestamp) {
        return timestamp != null ? new Instant(timestamp.getTime()) : null;
    }

    public List<ForumThread> getThreads(final String participant, final int offset, final int limit) {
        List<ForumThread> count = template.execute(session -> {
            SQLQuery query = session.createSQLQuery("SELECT" +
                    "  t.threadId, t.name, t.description," +
                    "  t.forumId, t.createdDate, t.numPosts, t.owner, t.contentId," +
                    "  t.approved, MAX(p.postDate) AS lastPostDate, MAX(p.modifiedDate) AS modifiedDate " +
                    "FROM" +
                    "  forum_post p" +
                    "  JOIN" +
                    "    forum_thread t" +
                    "  ON p.threadId = t.threadId" +
                    " WHERE" +
                    "  p.threadId IN (SELECT DISTINCT a.threadId FROM forum_post a WHERE a.owner = ?) " +
                    "GROUP BY" +
                    "  t.threadId, t.name, t.description, t.forumId, t.createdDate," +
                    "  t.numPosts, t.owner, t.contentId, t.approved, t.lastPostDate, t.modifiedDate " +
                    "ORDER BY modifiedDate DESC, lastPostDate DESC");

            query.setString(0, participant);
            query.setFetchSize(limit);

            query.addEntity(ForumThread.class);

            Iterator<ForumThread> forumThreads = query.list().iterator();
            List<ForumThread> page = new ArrayList<>();
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
        });
        return count;
    }

    public List<ForumThread> getThreadsStartingAt(final String username, final Long startAtThreadId, final Integer numberOfThreads, final Boolean includePosts) {
        List<ForumThread> ret = template.execute(session -> {

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

            List<ForumThread> selected = new ArrayList<>();
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
        });
        return ret;
    }

    public List<ForumThread> getThreadsEndingAt(final String username, final Long endAtThreadId, final Integer numberOfThreads, final Boolean includePosts) {
        List<ForumThread> ret = template.execute(session -> {

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
        });
        return ret;
    }

    public List<ForumThread> getThreads(final String username, final Integer numberOfThreads, final boolean includePosts) {
        List<ForumThread> ret = template.execute(session -> {

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
        });
        return ret;
    }

    public List<Long> getCategoryByForums(List<Long> forumIds) {
        String in = forumIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        return template.execute(session -> {
            SQLQuery sqlQuery = session.createSQLQuery("SELECT DISTINCT forumCategoryId FROM forum_forum WHERE forumId IN (" + in + ")");
            sqlQuery.addScalar("forumCategoryId", LongType.INSTANCE);
            return (List<Long>)sqlQuery.list();
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
