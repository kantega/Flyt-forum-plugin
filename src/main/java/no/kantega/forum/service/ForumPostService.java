package no.kantega.forum.service;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.listeners.ForumListener;
import no.kantega.forum.model.Attachment;
import no.kantega.forum.model.Post;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * User: sjukva
 * Date: 5/27/11
 */
public class ForumPostService implements ApplicationContextAware{
    private ForumDao dao;
    private ApplicationContext applicationContext;

    public ForumPostService(ForumDao dao) {
        this.dao = dao;
    }

    public void saveOrUpdate(Post post){
        dao.saveOrUpdate(post);
        runAfterSaveOrUpdateNotification(post);
        saveAttachments(post);
    }

    public void deletePost(Post post, String deletedText) {

        boolean postGotChildren = dao.postGotChildren(post);
        if(postGotChildren){
            post.setBody(deletedText);
            dao.saveOrUpdate(post);
            deleteAttachments(post);
        }else{
            runBeforePostDeletedNotification(post);
            dao.delete(post);
            runAfterPostDeletedNotification(post);
            deleteThreadIfPostIsOnlyPostInThread(post);
        }
    }

    private void deleteThreadIfPostIsOnlyPostInThread(Post post){
        if (post.getThread().getNumPosts() == 1) {
            dao.delete(post.getThread());
        }
    }

    private void runAfterSaveOrUpdateNotification(Post post) {
        ForumListenerCallback callback = new ForumListenerCallback() {
            public void doWithForumListener(ForumListener listener, Post post) {
                listener.afterPostSavedOrUpdated(post);
            }
        };
        runOnForumListeners(callback, post);
    }


    private void runAfterPostDeletedNotification(Post post) {
        ForumListenerCallback callback = new ForumListenerCallback() {
            public void doWithForumListener(ForumListener listener, Post post) {
                listener.afterPostDelete(post);
            }
        };
        runOnForumListeners(callback, post);
    }

    private void runBeforePostDeletedNotification(Post post) {
        ForumListenerCallback callback = new ForumListenerCallback() {
            public void doWithForumListener(ForumListener listener, Post post) {
                listener.beforePostDelete(post);
            }
        };
        runOnForumListeners(callback, post);
    }

    private void deleteAttachments(Post post) {
        AttachmentCallback callback = new AttachmentCallback() {
            public void doWithAttachment(ForumDao dao, Attachment attachment) {
                dao.delete(attachment);
            }
        };
        runOnAllAttachments(post, callback);
    }

    private void saveAttachments(Post post) {
        AttachmentCallback callback = new AttachmentCallback() {
            public void doWithAttachment(ForumDao dao, Attachment attachment) {
                dao.saveOrUpdate(attachment);
            }
        };
        runOnAllAttachments(post, callback);
    }

    private void runOnAllAttachments(Post post, AttachmentCallback callback) {
        Set<Attachment> attachments = post.getAttachments();
        if (attachments != null) {
            for (Attachment attachment : attachments) {
                if (attachment.getId() > 0) {
                    callback.doWithAttachment(dao, attachment);
                }
            }
        }
    }

    private void runOnForumListeners(ForumListenerCallback callback, Post post) {
        Map<String, ForumListener> forumListeners = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ForumListener.class);
        if (forumListeners != null && forumListeners.size() > 0)  {
            for (ForumListener forumListener : forumListeners.values()) {
                callback.doWithForumListener(forumListener, post);
            }
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private interface AttachmentCallback {
        public void doWithAttachment(ForumDao dao, Attachment attachment);
    }

    private interface ForumListenerCallback{
        public void doWithForumListener(ForumListener listener, Post post);
    }
}
