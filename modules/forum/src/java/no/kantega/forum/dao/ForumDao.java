package no.kantega.forum.dao;

import org.hibernate.*;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.apache.log4j.Logger;

import java.util.*;
import java.sql.SQLException;

import no.kantega.forum.model.*;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.topicmaps.data.Topic;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 02.des.2005
 * Time: 13:10:00
 * To change this template use File | Settings | File Templates.
 */
public class ForumDao {
    private HibernateTemplate template;

    private Logger log = Logger.getLogger(ForumDao.class);

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    // saveOrUpdate
    public void saveOrUpdate(ForumCategory forumCategory) {
        template.saveOrUpdate(forumCategory);
    }

    public void saveOrUpdate(Attachment attachment) {
        template.saveOrUpdate(attachment);
    }

    public void saveOrUpdate(ForumThread thread) {
        template.saveOrUpdate(thread);
        updateThreadCount(thread.getForum().getId());
    }

    public void saveOrUpdate(Forum forum) {
        template.saveOrUpdate(forum);
        updateForumCount(forum.getForumCategory().getId());
    }

    public void saveOrUpdate(Post post) {
        template.saveOrUpdate(post);
        updatePostCount(post.getThread().getId());
    }

    public void approve(Post post) {
        post.setApproved(true);
        saveOrUpdate(post);

        ForumThread thread = post.getThread();
        thread.setApproved(true);
        saveOrUpdate(thread);

        updatePostCount(thread.getId());
        updateThreadCount(thread.getForum().getId());
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

    public List getForumCategories() {
        List list = template.find("from ForumCategory c left join fetch c.forums f order by c.name");

        // Remove duplicates since Hibernate duplicates forums
        Set setItems = new LinkedHashSet(list);
        list.clear();
        list.addAll(setItems);

        return list;
    }

    public List getForums() {
        return template.find("from Forum f inner join fetch f.forumCategory c");
    }

    public List getForumsWithUserPostings(final String userId) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
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
        return (List<Post>) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
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


    public List getLastPosts(final int n) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
                query.setString(0, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });
    }

    public List getThreadsWithTopicIds(final int topicMapId, final List<String> topicIds, final int maxResults) {
        if (topicIds.size() == 0) {
            return new ArrayList();
        }
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer q = new StringBuffer();
                q.append("from Thread t where t.forum.topicMapId = ? ");
                for (int i = 0; i < topicIds.size(); i++) {
                    q.append("and ? in elements(topics) ");
                }
                q.append(" order by t.id desc");
                Query query = session.createQuery(q.toString());
                query.setInteger(0, topicMapId);
                if (maxResults != -1) {
                    query.setMaxResults(maxResults);
                }
                for (int i = 0; i < topicIds.size(); i++) {
                    String t = topicIds.get(i);
                    query.setString(i+1, t);
                }
                return query.list();
            }
        });
    }

    public List getPostsWithTopicIds(final int topicMapId, final List<String> topicIds, final int maxResults) {

        if (topicIds == null || topicIds.size() == 0) {
            return (List) template.execute(new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
                    StringBuffer q = new StringBuffer();
                    q.append("from Post p where p.approved = ? and p.thread.forum.topicMapId = ? order by p.id desc");
                    Query query = session.createQuery(q.toString());
                    query.setString(0, "Y");
                    query.setInteger(1, topicMapId);
                    if (maxResults != -1) {
                        query.setMaxResults(maxResults);
                    }
                    return query.list();
                }
            });
        }
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                StringBuffer q = new StringBuffer();
                q.append("from Post p where p.approved = ? and p.thread.forum.topicMapId = ? and p.thread.id in (select id from ForumThread where ");
                for (int i = 0; i < topicIds.size(); i++) {
                    if (i > 0) {
                        q.append(" and ");
                    }
                    q.append(" ? in elements(topics) ");
                }
                q.append(") order by p.id desc");
                Query query = session.createQuery(q.toString());
                query.setString(0, "Y");
                query.setInteger(1, topicMapId);
                if (maxResults != -1) {
                    query.setMaxResults(maxResults);
                }
                for (int i = 0; i < topicIds.size(); i++) {
                    String t = topicIds.get(i);
                    query.setString(i+2, t);
                }
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

    public List getLastPostsInForum(final long forumId, final int n) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.thread.forum.id = ? and p.approved = ? order by p.id desc");
                query.setLong(0, forumId);
                query.setString(1, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });
    }

    public List getLastPostsInForums(final long forumIds[], final int n) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                String whereClause = "";
                for (int i = 0; i < forumIds.length; i++) {
                    if (i > 0) {
                        whereClause += " or ";
                    }
                    whereClause += "p.thread.forum.id = ? ";
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

    public List getPostsAfterDate(final Date lastVisit) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? and p.postDate > ? order by p.id desc");
                query.setString(0, "Y");
                query.setTimestamp(1, lastVisit);
                return query.list();
            }
        });
    }

    public List getLastPostsInThread(final long threadId, final int n) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.thread.id = ? and p.approved = ? order by p.id desc");
                query.setLong(0, threadId);
                query.setString(1, "Y");
                query.setMaxResults(n);
                return query.list();
            }
        });

    }

    public List getUnapprovedPosts() {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from Post p where p.approved = ? order by p.id desc");
                query.setString(0, "N");
                return query.list();
            }
        });
    }

    public ForumCategory getPopulatedForumCategory(final long forumCategoryId) {
        return (ForumCategory) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategoryId));
                fc.getForums().size();
                return fc;
            }
        });
    }

    public Forum getPopulatedForum(final long forumId) {
        return (Forum) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forumId));
                f.getThreads().size();
                f.getGroups().size();
                return f;
            }
        });
    }

    public ForumThread getPopulatedThread(final long threadId) {
        return (ForumThread) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(ForumThread.class, new Long(threadId));
                t.getPosts().size();
                return t;
            }
        });
    }

    public Post getPopulatedPost(final long postId) {
        return (Post) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Post.class, new Long(postId));
                p.getAttachments().size();
                return p;
            }
        });
    }

    // get
    public Forum getForum(final long forumId) {
        return (Forum) template.find("from Forum f inner join fetch f.forumCategory c where f.id=?", new Long(forumId)).get(0);
    }

    public ForumThread getThread(final long threadId) {
        return (ForumThread) template.find("from ForumThread t inner join fetch t.forum f where t.id=?", new Long(threadId)).get(0);
    }

    public Attachment getAttachment(final long attachmentId) {
        return (Attachment) template.get(Attachment.class, new Long(attachmentId));
    }

    public ForumCategory getForumCategory(final long forumCategoryId) {
        return (ForumCategory) template.get(ForumCategory.class, new Long(forumCategoryId));
    }

    public Post getPost(final long postId) {
        return (Post) template.get(Post.class, new Long(postId));
    }

    // add
    public void addForumToCategory(final Forum forum, final ForumCategory forumCategory) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategory.getId()));
                forum.setForumCategory(fc);
                if (fc.getForums() == null) {
                    fc.setForums(new HashSet());
                }
                fc.getForums().add(forum);
                fc.setNumForums(fc.getNumForums() + 1); // increase number of forums
                session.saveOrUpdate(forum);
                session.saveOrUpdate(fc);
                return null;
            }
        });
    }


    public void addAttachmentToPost(final Attachment attachment, final Post post) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Post.class, new Long(post.getId()));
                attachment.setPost(p);
                if (p.getAttachments() == null) {
                    p.setAttachments(new HashSet());
                }
                p.getAttachments().add(attachment);
                session.saveOrUpdate(attachment);
                session.saveOrUpdate(p);
                return null;
            }
        });
    }

    public boolean postGotChildren(final Post p) {
        List children = template.find("from Post p where p.replyToId=?",p.getId());
        return (children.size() > 0);
    }

    public List<ForumThread> getThreadsInForum(final long forumId, final int firstResult, final int maxResult) {
        return (List<ForumThread>) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q  = session.createQuery("from ForumThread t where t.forum.id = ? and t.approved = ? order by t.createdDate desc");
                q.setLong(0, forumId);
                q.setString(1, "Y");        
                q.setFirstResult(firstResult);
                q.setMaxResults(maxResult);

                return q.list();
            }
        });
    }

    public List getPostsInThread(final long id, final int firstResult, final int maxResult) {
        return getPostsInThread(id, firstResult, maxResult, false);
    }

    public List getPostsInThread(final long id, final int firstResult, final int maxResult, final boolean reverse) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
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

                ForumCategory category = (ForumCategory) session.get(ForumCategory.class, new Long(categoryId));

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

                Forum forum = (Forum) session.get(Forum.class, new Long(forumId));

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

                ForumThread thread = (ForumThread) session.get(ForumThread.class, new Long(threadId));

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

                Post p = (Post) session.get(Post.class, new Long(postId));

                Query q = session.createQuery("select count(*) from Post p where p.thread.id=? and p.id < ? and approved = 'Y'");

                q.setLong(0, p.getThread().getId());
                q.setLong(1, p.getId());

                return q.uniqueResult();
            }
        });

        return n.intValue();
    }
}
