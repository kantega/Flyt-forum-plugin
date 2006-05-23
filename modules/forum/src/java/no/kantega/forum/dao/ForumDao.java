package no.kantega.forum.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.sql.SQLException;

import no.kantega.forum.model.*;
import no.kantega.forum.permission.Roles;

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

    public void saveOrUpdate(Post post) {
        template.saveOrUpdate(post);
    }

    public void saveOrUpdate(User user) {
        template.saveOrUpdate(user);
    }

    public void saveOrUpdate(UserProfile userProfile) {
        template.saveOrUpdate(userProfile);
    }

    public void saveOrUpdate(Group group) {
        template.saveOrUpdate(group);
    }

    public void saveOrUpdate(Role role) {
        template.saveOrUpdate(role);
    }

    // delete
    public void delete(Post post) {
        template.delete(post);
    }

    public void delete(ForumThread thread) {
        template.delete(thread);
    }

    public void delete(Attachment attachment) {
        template.delete(attachment);
    }

    public void delete(Forum forum) {
        template.delete(forum);
    }

    public void delete(ForumCategory forumCategory) {
        template.delete(forumCategory);
    }

    //
    public List getForumCategories() {
        return template.find("from ForumCategory order by Name");
    }

    //
    public Role getRole(Roles r) {
        return (Role) template.find("from Roles where roleType=?", r).get(0);
    }

    public ForumCategory getPopulatedForumCategory(final long forumCategoryId) {
        return (ForumCategory) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                ForumCategory fc = (ForumCategory) session.get(ForumCategory.class, new Long(forumCategoryId));
                fc.getForums().size();
                fc.getGroups().size();
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
                t.getGroups().size();
                if(t.getOwner() != null) {
                    t.getOwner().getName();
                }
                return t;
            }
        });
    }

    public Post getPopulatedPost(final long postId) {
        return (Post) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Post p = (Post) session.get(Post.class, new Long(postId));
                p.getAttachments().size();
                p.getThread().getGroups().size();
                return p;
            }
        });
    }

    // get
    public Forum getForum(final long forumId) {
        return (Forum) template.get(Forum.class, new Long(forumId));
    }

    public ForumThread getThread(final long threadId) {
        return (ForumThread) template.get(ForumThread.class, new Long(threadId));
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

    public User getUser(final long userId) {
        return (User) template.get(User.class, new Long(userId));
    }

    public User getUser(final String username) {
        return (User) template.find("from User u where u.name=?", username).get(0);
    }

    public Group getGroup(final long groupId) {
        return (Group) template.get(Group.class, new Long(groupId));
    }

    public Group getGroup(final String groupname) {
        return (Group) template.find("from Group g where g.name=?", groupname).get(0);
    }

    public List getUserGroups(final User user) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query q = session.createQuery("from forum_groups_users where userId=:userId order by userId");
                q.setLong("userId", user.getId());
                List groups = q.list();
                return groups;
            }
        });
    }

    public List getUserRoles(final User user) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query q = session.createQuery("from forum_roles_users where userId=:userId order by userId");
                q.setLong("userId", user.getId());
                List groups = q.list();
                return groups;
            }
        });
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

    public void addUserProfileToUser(final UserProfile userProfile, final User user) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                UserProfile up = (UserProfile) session.get(UserProfile.class, new Long(user.getId()));
                up.setId(user.getId());
                session.saveOrUpdate(up);
                return null;
            }
        });
    }

    public boolean isInGroup(Set groups, Group g) {
        Object[] tmpgroups = groups.toArray();
        for (int i = 0; i < tmpgroups.length; i++) {
            Group tmpgroup = (Group) tmpgroups[i];
            if (tmpgroup.getId() == g.getId()) {
                return true;
            }
        }

        return false;
    }

    public boolean postGotChildren(final Post p) {
        List children = template.find("from Post p where p.replyToId=?",String.valueOf(p.getId()));
        return (children.size() > 0) ? true : false;
    }

    public List getThreadsInForum(long forumId) {
        return template.find("from ForumThread t where t.forum.id=?",new Long(forumId));
    }

    public List getPostsInThread(long id) {
        return template.find("from Post p where p.thread.id=?", new Long(id));
    }
}
