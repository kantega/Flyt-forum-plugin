package no.kantega.forum.listeners;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.publishing.api.comments.CommentNotification;
import no.kantega.publishing.api.comments.CommentNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class CommentNotificationForumListener implements ForumListener{
    private ForumDao dao;

    @Autowired
    private List<CommentNotificationListener> commentNotificationListeners;

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
        if (commentNotificationListeners != null && commentNotificationListeners.size() > 0)  {
            for (no.kantega.publishing.api.comments.CommentNotificationListener notificationListener : commentNotificationListeners) {
                callback.doWithNotification(notificationListener, notification);
            }
        }
    }

    private CommentNotification getCommentNotification(Post post) {
        CommentNotification notification = new CommentNotification();
        notification.setContext("content");
        notification.setObjectId(String.valueOf(post.getThread().getContentId()));
        ForumThread thread = dao.getThread(post.getThread().getId());
        notification.setNumberOfComments(thread.getNumPosts());
        notification.setCommentId(String.valueOf(post.getId()));
        notification.setCommentTitle(post.getSubject());
        notification.setCommentSummary(post.getBody());
        notification.setCommentAuthor(post.getAuthor());
        return notification;
    }

    private interface NotificationCallback {
        void doWithNotification(CommentNotificationListener listener, CommentNotification notification);
    }

    private boolean isAksessContent(Post post) {
        return post.getThread().getContentId() > 0;
    }
}
