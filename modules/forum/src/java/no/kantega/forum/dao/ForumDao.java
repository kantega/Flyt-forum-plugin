package no.kantega.forum.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.HashSet;

import no.kantega.forum.model.*;

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
    }

    public void saveOrUpdate(Forum forum) {
        template.saveOrUpdate(forum);
    }
    //

    public List getForumCategories() {
        return template.find("from ForumCategory order by Name");
    }

    //
    public ForumCategory getPopulatedForumCategory(final int forumCategoryId) {
        return (ForumCategory) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategoryId));
                fc.getForums().size();
                return fc;
            }
        });
    }

    public Forum getPopulatedForum(final int forumId) {
        return (Forum) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forumId));
                f.getThreads().size();
                return f;
            }
        });
    }

    public ForumThread getPopulatedThread(final int threadId) {
        return (ForumThread) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(Forum.class, new Long(threadId));
                t.getPosts().size();
                return t;
            }
        });
    }

    public Post getPopulatedPost(final int postId) {
        return (Post) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Forum.class, new Long(postId));
                p.getAttachments().size();
                return p;
            }
        });
    }

    // get
    public Forum getForum(final int forumId) {
        return (Forum) template.get(Forum.class, new Integer(forumId));
    }

    public ForumThread getThread(final int threadId) {
        return (ForumThread) template.get(ForumThread.class, new Integer(threadId));
    }

    public Attachment getAttachment(final int attachmentId) {
        return (Attachment) template.get(Attachment.class, new Integer(attachmentId));
    }

    public ForumCategory getForumCategory(final int forumCategoryId) {
        return (ForumCategory) template.get(ForumCategory.class, new Integer(forumCategoryId));
    }

    // add
    public void addForumToCategory(final Forum forum, final ForumCategory forumCategory) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Integer(forumCategory.getId()));
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

    public void addThreadToForum(final ForumThread thread, final Forum forum) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Integer(forum.getId()));
                thread.setForum(f);
                if (f.getThreads() == null) {
                    f.setThreads(new HashSet());
                }
                f.getThreads().add(thread);
                f.setNumThreads(f.getNumThreads() + 1); // increase number of threads
                session.saveOrUpdate(thread);
                session.saveOrUpdate(f);
                return null;
            }
        });
    }

    public void addPostToThread(final Post post, final ForumThread thread) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(ForumThread.class, new Integer(thread.getId()));
                post.setThread(t);
                if (t.getPosts() == null) {
                    t.setPosts(new HashSet());
                }
                t.getPosts().add(post);
                t.setNumPosts(t.getNumPosts() + 1);
                session.saveOrUpdate(post);
                session.saveOrUpdate(t);
                return null;
            }
        });
    }

    public void addAttachmentToPost(final Attachment attachment, final Post post) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Post.class, new Integer(post.getId()));
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
}
