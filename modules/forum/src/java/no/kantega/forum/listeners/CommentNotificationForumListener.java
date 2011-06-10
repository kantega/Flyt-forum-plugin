package no.kantega.forum.listeners;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.publishing.api.comments.CommentNotification;
import no.kantega.publishing.api.comments.CommentNotificationListener;
import no.kantega.publishing.spring.RootContext;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: sjukva
 * Date: 6/6/11
 * Time: 8:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommentNotificationForumListener implements ForumListener{
    private ForumDao dao;

    public CommentNotificationForumListener(ForumDao dao) {
        this.dao = dao;
    }

    public void afterPostSavedOrUpdated(Post post) {
        if(isAksessContent(post))  {
            runNewCommentNotification(post);
        }
    }

    public void beforePostDelete(Post post) {
    }

    public void afterPostDelete(Post post) {
        if(isAksessContent(post)) {
            runCommentDeletedNotification(post);
        }
    }

    public void beforeThreadDelete(ForumThread thread) {
    }


    private void runNewCommentNotification(Post post) {
        NotificationCallback callback = new NotificationCallback() {
            public void doWithNotification(no.kantega.publishing.api.comments.CommentNotificationListener listener, CommentNotification notification) {
                listener.newCommentNotification(notification);
            }
        };
        runOnListeners(callback, post);
    }


    private void runCommentDeletedNotification(Post post) {
        NotificationCallback callback = new NotificationCallback() {
            public void doWithNotification(no.kantega.publishing.api.comments.CommentNotificationListener listener, CommentNotification notification) {
                listener.commentDeletedNotification(notification);
            }
        };
        runOnListeners(callback, post);
    }

    private void runOnListeners(NotificationCallback callback, Post post) {
        CommentNotification notification = getCommentNotification(post);
        Map commentNotificationListenerBeans = RootContext.getInstance().getBeansOfType(no.kantega.publishing.api.comments.CommentNotificationListener.class);
        if (commentNotificationListenerBeans != null && commentNotificationListenerBeans.size() > 0)  {
            for (no.kantega.publishing.api.comments.CommentNotificationListener notificationListener : (Iterable<? extends no.kantega.publishing.api.comments.CommentNotificationListener>) commentNotificationListenerBeans.values()) {
                callback.doWithNotification(notificationListener, notification);
            }
        }
    }

    private CommentNotification getCommentNotification(Post post) {
        CommentNotification notification = new CommentNotification();
        notification.setContext("content");
        notification.setObjectId("" + post.getThread().getContentId());
        ForumThread thread = dao.getThread(post.getThread().getId());
        notification.setNumberOfComments(thread.getNumPosts());
        notification.setCommentId(String.valueOf(post.getId()));
        notification.setCommentTitle(post.getSubject());
        notification.setCommentSummary(post.getBody());
        notification.setCommentAuthor(post.getAuthor());
        return notification;
    }

    private interface NotificationCallback {
        public void doWithNotification(CommentNotificationListener listener, CommentNotification notification);
    }

    private boolean isAksessContent(Post post) {
          return post.getThread().getContentId() > 0;
      }
}
