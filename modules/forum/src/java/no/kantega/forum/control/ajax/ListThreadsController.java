package no.kantega.forum.control.ajax;


import no.kantega.commons.client.util.RequestParameters;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.api.rating.RatingService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ListThreadsController implements Controller {


    private ForumDao forumDao;
    private int defaultNumberOfPostsToShow = 20;
    private RatingService ratingService;

    public ListThreadsController(ForumDao forumDao, RatingService ratingService) {
        this.forumDao = forumDao;
        this.ratingService = ratingService;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map model = new HashMap();

        RequestParameters param = new RequestParameters(request);
        int forumId = param.getInt("forumId");
        int offset = param.getInt("offset");
        int numberOfPostsToShow = param.getInt("numberOfPostsToShow");

        if (numberOfPostsToShow == -1) {
            numberOfPostsToShow = defaultNumberOfPostsToShow;
        }
        if (offset == -1) {
            offset = 0;
        }

        /*
        Bygg en list med String id of poster
         */
        List<String> objectIds = new ArrayList<String>();
        Map<Long, List<Rating>> ratingsForPosts = new HashMap<Long, List<Rating>>();

        List<ForumThread> threads = forumDao.getThreadsInForum(forumId, offset, numberOfPostsToShow);
        for (ForumThread thread : threads) {
            Iterator posts = thread.getPosts().iterator();
            while (posts.hasNext()) {
                Post post = (Post) posts.next();
                objectIds.add(""+post.getId());
            }
        }

        List<Rating> ratings = ratingService.getRatingsForObjects(objectIds, "forum");
        for (Rating rating : ratings) {
            List<Rating> ratingsForSinglePost = ratingsForPosts.get(Long.parseLong(rating.getObjectId()));
            if (ratingsForSinglePost == null) {
                // Finnes ingen ratings for denne post-iden, kan legge til ny nøkkel og opprette ny list
                ratingsForSinglePost = new ArrayList<Rating>();
                ratingsForPosts.put(Long.parseLong(rating.getObjectId()), ratingsForSinglePost);
            }
            ratingsForSinglePost.add(rating);
        }
        model.put("threads", threads);
        model.put("ratings", ratingsForPosts);
        /*
        Kan da i jsp-en kjøre ${ratings[post.id]} for å få en liste over alle ratings for en gitt post
         */
        return new ModelAndView("wall/threads", model);
    }
}
