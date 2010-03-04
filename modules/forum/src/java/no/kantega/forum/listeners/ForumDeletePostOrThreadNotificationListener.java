package no.kantega.forum.listeners;

import no.kantega.forum.model.Post;
import no.kantega.forum.model.ForumThread;
import no.kantega.publishing.api.rating.RatingService;

import java.util.Set;

/**
 *
 */
public class ForumDeletePostOrThreadNotificationListener extends ForumListenerAdapter {
    private RatingService ratingService;

    public void beforePostDelete(Post p) {
        ratingService.deleteRatingsForObject("" + p.getId(), "forum");
    }

    public void beforeThreadDelete(ForumThread t) {
        Set<Post> posts = t.getPosts();        
        for (Post p : posts) {
            ratingService.deleteRatingsForObject("" + p.getId(), "forum");
        }
    }

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }
}
