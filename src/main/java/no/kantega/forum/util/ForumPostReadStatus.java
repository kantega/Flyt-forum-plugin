package no.kantega.forum.util;

import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForumPostReadStatus {
    HttpServletRequest request;

    public ForumPostReadStatus(HttpServletRequest request) {
        this.request = request;
    }

    @SuppressWarnings("unchecked")
    public void addPost(Post post) {
        HttpSession session = request.getSession(true);
        Map<String, ReadStatus> threads = (Map<String, ReadStatus>)session.getAttribute("forumReadPosts");
        if (threads == null) {
            threads = new HashMap<>();
            session.setAttribute("forumReadPosts", threads);
        }
        ReadStatus st = new ReadStatus();
        st.setPostId(post.getId());
        st.setThreadId(post.getThread().getId());
        st.setForumId(post.getThread().getForum().getId());

        threads.put("post-" + post.getId(), st);
    }

    @SuppressWarnings("unchecked")
    public List<Post> filterReadPosts(List<Post> posts) {
        List<Post> unreadPosts = new ArrayList<>();
        HttpSession session = request.getSession(true);

        Map<String, ReadStatus> readPosts = (Map<String, ReadStatus>)session.getAttribute("forumReadPosts");
        if (readPosts == null) {
            return posts;
        }


        for (Post p : posts) {
            if (readPosts.get("post-" + p.getId()) == null) {
                unreadPosts.add(p);
            }
        }

        return unreadPosts;
    }

    public void updateUnreadPostsInForum(List<Post> posts, Forum forum) {
        forum.setNumNewPosts(0);
        List<Post> unreadPosts = filterReadPosts(posts);
        for (Post p : unreadPosts) {
            if (p.getThread().getForum().getId() == forum.getId()) {
                forum.setNumNewPosts(forum.getNumNewPosts() + 1);
            }
        }
    }

    public void updateUnreadPostsInThread(List<Post> posts, ForumThread thread) {
        thread.setNumNewPosts(0);
        List<Post> unreadPosts = filterReadPosts(posts);
        for (Post p : unreadPosts) {
            if (p.getThread().getId() == thread.getId()) {
                thread.setNumNewPosts(thread.getNumNewPosts() + 1);
            }
        }
    }

    private static class ReadStatus {
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
