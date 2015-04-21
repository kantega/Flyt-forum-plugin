package no.kantega.forum.control.ajax;


import no.kantega.commons.client.util.RequestParameters;
import no.kantega.commons.util.StringHelper;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.dao.ThreadSortOrder;
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
		Map<String, Object> model = new HashMap<>();

		RequestParameters param = new RequestParameters(request);

        String forumId = param.getString("forumId");
        int[] forumIds;
        if (forumId == null || forumId.equals("-1")) {
            forumIds = new int[] {-1};
        } else {
            forumIds =  StringHelper.getInts(forumId, ",");
        }


		int hiddenForumId = param.getInt("hiddenForumId");
		int forumCategoryId = param.getInt("forumCategoryId");
		int offset = param.getInt("offset");
		int numberOfPostsToShow = param.getInt("numberOfPostsToShow");
		int threadId = param.getInt("threadId");
		String userId = param.getString("username");
		boolean expandThreads = param.getBoolean("expandThreads");

		model.put("expandThreads",expandThreads);

		if (numberOfPostsToShow == -1) {
			numberOfPostsToShow = defaultNumberOfPostsToShow;
		}
		if (offset == -1) {
			offset = 0;
		}

		model.put("hiddenForumId", hiddenForumId);

        ThreadSortOrder order = ThreadSortOrder.fromIntOrDefault(param.getInt("sortBy"));

        List<String> objectIds = new ArrayList<>();
		Map<Long, List<Rating>> ratingsForPosts = new HashMap<>();
		List<ForumThread> threads = new ArrayList<>();

		if (threadId != -1) {
			threads.add(forumDao.getPopulatedThread(threadId));
		} else if (userId != null && userId.trim().length() > 0) {
			threads = forumDao.getThreadsWhereUserHasPosted(userId, numberOfPostsToShow + 1, offset, forumIds[0], forumCategoryId, order);
		} else if (forumCategoryId != -1) {
			threads = forumDao.getThreadsInForumCategory(forumCategoryId, offset, numberOfPostsToShow + 1, order);
		} else {
			threads = forumDao.getThreadsInForums(forumIds, offset, numberOfPostsToShow + 1, order);
		}




		if (numberOfPostsToShow > 0 && threads.size() > numberOfPostsToShow) {
			model.put("hasMorePosts", Boolean.TRUE);
			threads = threads.subList(0, threads.size() - 1);
		}

		for (ForumThread thread : threads) {
            for (Post post : thread.getPosts()) {
                objectIds.add(String.valueOf(post.getId()));
            }
		}

		List<Rating> ratings = ratingService.getRatingsForObjects(objectIds, "forum");
		for (Rating rating : ratings) {
			List<Rating> ratingsForSinglePost = ratingsForPosts.get(Long.parseLong(rating.getObjectId()));
			if (ratingsForSinglePost == null) {
				// Finnes ingen ratings for denne post-iden, kan legge til ny nøkkel og opprette ny list
				ratingsForSinglePost = new ArrayList<>();
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