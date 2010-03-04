package no.kantega.forum.listeners;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.publishing.api.rating.RatingNotificationListener;
import no.kantega.publishing.api.rating.RatingNotification;
import no.kantega.publishing.api.rating.Rating;

/**
 *
 */
public class PostRatingNotificationListener implements RatingNotificationListener {
    private ForumDao dao;

    public void newRatingNotification(RatingNotification notification) {
        Rating r = notification.getRating();
        if (r.getContext().equalsIgnoreCase("forum")) {
            Post p = dao.getPost(Long.parseLong(r.getObjectId()));
            if (p != null) {
                p.setRatingScore(notification.getScore());
                p.setNumberOfRatings(notification.getNumberOfRatings());
                dao.saveOrUpdate(p);
            }
        }
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}

