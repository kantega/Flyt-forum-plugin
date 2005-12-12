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

    public void saveOrUpdate(User user) {
        template.saveOrUpdate(user);
    }

    public void saveOrUpdate(Group group) {
        template.saveOrUpdate(group);
    }

    public void saveOrUpdate(Role role) {
        template.saveOrUpdate(role);
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
                fc.getGroups().size();
                return fc;
            }
        });
    }

    public Forum getPopulatedForum(final int forumId) {
        return (Forum) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forumId));
                f.getThreads().size();
                f.getGroups().size();
                return f;
            }
        });
    }

    public ForumThread getPopulatedThread(final int threadId) {
        return (ForumThread) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(Forum.class, new Long(threadId));
                t.getPosts().size();
                t.getGroups().size();
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

    public void addThreadToForum(final ForumThread thread, final Forum forum) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forum.getId()));
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
                ForumThread t = (ForumThread) session.get(ForumThread.class, new Long(thread.getId()));
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

    // permissions
    public void addGroupToForumCategory(final Group group, final ForumCategory forumCategory) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategory.getId()));
                if (fc.getGroups() == null) {
                    fc.setGroups(new HashSet());
                }
                fc.getGroups().add(group);
                session.saveOrUpdate(fc);
                return null;
            }
        });
    }

    public void removeGroupFromForumCategory(final Group group, final ForumCategory forumCategory) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategory.getId()));
                if (fc.getGroups() == null) {
                    fc.setGroups(new HashSet());
                }
                fc.getGroups().remove(group);
                session.saveOrUpdate(fc);
                return null;
            }
        });
    }

    public void addGroupToForum(final Group group, final Forum forum) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forum.getId()));
                if (f.getGroups() == null) {
                    f.setGroups(new HashSet());
                }
                f.getGroups().add(group);
                session.saveOrUpdate(f);
                return null;
            }
        });
    }

    public void removeGroupFromForum(final Group group, final Forum forum) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Forum f = (Forum) session.get(Forum.class, new Long(forum.getId()));
                if (f.getGroups() == null) {
                    f.setGroups(new HashSet());
                }
                f.getGroups().remove(group);
                session.saveOrUpdate(f);
                return null;
            }
        });
    }

    public void addGroupToThread(final Group group, final ForumThread thread) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(ForumThread.class, new Long(thread.getId()));
                if (t.getGroups() == null) {
                    t.setGroups(new HashSet());
                }
                t.getGroups().add(group);
                session.saveOrUpdate(t);
                return null;
            }
        });
    }

    public void removeGroupFromThread(final Group group, final ForumThread thread) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumThread t = (ForumThread) session.get(ForumThread.class, new Long(thread.getId()));
                if (t.getGroups() == null) {
                    t.setGroups(new HashSet());
                }
                t.getGroups().remove(group);
                session.saveOrUpdate(t);
                return null;
            }
        });
    }

    public void addUserToGroup(final User user, final Group group) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Group g = (Group) session.get(Group.class, new Long(group.getId()));
                if (g.getUsers() == null) {
                    g.setUsers(new HashSet());
                }
                g.getUsers().add(user);
                session.saveOrUpdate(g);
                return null;
            }
        });
    }

    public void removeUserFromGroup(final User user, final Group group) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Group g = (Group) session.get(Group.class, new Long(group.getId()));
                if (g.getUsers() == null) {
                    g.setUsers(new HashSet());
                }
                g.getUsers().remove(user);
                session.saveOrUpdate(g);
                return null;
            }
        });
    }

    public void addUserToRole(final User user, final Role role) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Role r = (Role) session.get(Role.class, new Long(role.getId()));
                if (r.getUsers() == null) {
                    r.setUsers(new HashSet());
                }
                r.getUsers().add(user);
                session.saveOrUpdate(r);
                return null;
            }
        });
    }

    public void removeUserFromRole(final User user, final Role role) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Role r = (Role) session.get(Role.class, new Long(role.getId()));
                if (r.getUsers() == null) {
                    r.setUsers(new HashSet());
                }
                r.getUsers().remove(user);
                session.saveOrUpdate(r);
                return null;
            }
        });
    }
}