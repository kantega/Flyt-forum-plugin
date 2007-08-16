package no.kantega.forum.util;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.model.Forum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * User: Anders Skar, Kantega AS
 * Date: Aug 15, 2007
 * Time: 2:45:46 PM
 */
public class ForumPostReadStatus {
    HttpServletRequest request;

    public ForumPostReadStatus(HttpServletRequest request) {
        this.request = request;
    }

    public void addPost(Post post) {
        HttpSession session = request.getSession(true);
        Map threads = (Map)session.getAttribute("forumReadPosts");
        if (threads == null) {
            threads = new HashMap();
            session.setAttribute("forumReadPosts", threads);
        }
        ReadStatus st = new ReadStatus();
        st.setPostId(post.getId());
        st.setThreadId(post.getThread().getId());
        st.setForumId(post.getThread().getForum().getId());

        threads.put("post-" + post.getId(), st);
    }

    public List filterReadPosts(List posts) {
        List unreadPosts = new ArrayList();
        HttpSession session = request.getSession(true);

        Map readPosts = (Map)session.getAttribute("forumReadPosts");
        if (readPosts == null) {
            return posts;
        }


        for (int i = 0; i < posts.size(); i++) {
            Post p = (Post) posts.get(i);
            if (readPosts.get("post-" + p.getId()) == null) {
                unreadPosts.add(p);
            }
        }

        return unreadPosts;
    }

    public void updateUnreadPostsInForum(List posts, Forum forum) {
        forum.setNumNewPosts(0);
        List unreadPosts = filterReadPosts(posts);
        for (int i = 0; i < unreadPosts.size(); i++) {
            Post p =  (Post)unreadPosts.get(i);
            if (p.getThread().getForum().getId() == forum.getId()) {
                forum.setNumNewPosts(forum.getNumNewPosts() + 1);
            }
        }
    }

    public void updateUnreadPostsInThread(List posts, ForumThread thread) {
        thread.setNumNewPosts(0);
        List unreadPosts = filterReadPosts(posts);
        for (int i = 0; i < unreadPosts.size(); i++) {
            Post p =  (Post)unreadPosts.get(i);
            if (p.getThread().getId() == thread.getId()) {
                thread.setNumNewPosts(thread.getNumNewPosts() + 1);
            }
        }
    }

    private class ReadStatus {
        private long forumId;
        private long threadId;
        private long postId;

        public long getForumId() {
            return forumId;
        }

        public void setForumId(long forumId) {
            this.forumId = forumId;
        }

        public long getThreadId() {
            return threadId;
        }

        public void setThreadId(long threadId) {
            this.threadId = threadId;
        }

        public long getPostId() {
            return postId;
        }

        public void setPostId(long postId) {
            this.postId = postId;
        }
    }
}

// 