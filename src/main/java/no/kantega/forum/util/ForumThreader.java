package no.kantega.forum.util;

import no.kantega.forum.model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ForumThreader {
    /**
     * Organizes posts in children
     * @param posts - List of posts
     * @return
     */
    public List<Post> organizePostsInThread(List<Post> posts) {
        List<Post> rootPosts = new ArrayList<>();

        Map<Long, List<Post>> postCache = new HashMap<>();

        for (Post p : posts) {
            long replyId = p.getReplyToId();
            if (replyId > 0) {
                List<Post> children = postCache.get(replyId);
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(p);
                postCache.put(replyId, children);
            } else {
                rootPosts.add(p);
            }
        }

        for (Post p : rootPosts) {
            addChildren(p, postCache);
        }

        return rootPosts;
    }

    private void addChildren(Post parent, Map<Long, List<Post>> postCache) {
        List<Post> children = postCache.get(parent.getId());
        if (children != null) {
            parent.setReplyPosts(children);
            for (Post child : children) {
                addChildren(child, postCache);
            }
        }
    }

}
