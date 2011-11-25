package no.kantega.forum.util;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;

import java.util.Comparator;
import java.util.Iterator;


public class ForumWallThreadListingComparator implements Comparator {

    public int compare(Object o1, Object o2) {

        ForumThread thread1 = (ForumThread)o1;
        ForumThread thread2 = (ForumThread)o2;

        Post thread1LastPost = getLastSubmittedPostInThread(thread1);
        Post thread2LastPost = getLastSubmittedPostInThread(thread2);

        if (thread1LastPost.getPostDate().after(thread2LastPost.getPostDate())) {
            return -1;
        } else if (thread1LastPost.getPostDate().before(thread2LastPost.getPostDate())) {
            return 1;
        } else {
            return 0;
        }
    }

    private Post getLastSubmittedPostInThread(ForumThread thread) {
        Post lastPost = null;
        Iterator posts = thread.getPosts().iterator();
        while (posts.hasNext()) {
            Post post = (Post) posts.next();
            if (lastPost == null) {
                lastPost = post;
            } else {
                if (post.getPostDate().after(lastPost.getPostDate())) {
                    lastPost = post;
                }
            }
        }
        return lastPost;
    }
}