package no.kantega.forum.control.ajax;


import no.kantega.commons.client.util.RequestParameters;
import no.kantega.commons.util.StringHelper;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.dao.ThreadSortOrder;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.api.rating.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ListThreadsController implements Controller {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private PermissionManager permissionManager;
    private UserResolver userResolver;
	private ForumDao forumDao;
	private int defaultNumberOfPostsToShow = 5;
	private RatingService ratingService;

	public ListThreadsController(ForumDao forumDao, RatingService ratingService, PermissionManager permissionManager, UserResolver userResolver) {
		this.forumDao = forumDao;
		this.ratingService = ratingService;
        this.permissionManager = permissionManager;
        this.userResolver = userResolver;
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
		List<ForumThread> threads;

		if (threadId != -1) {
			threads = singletonList(forumDao.getPopulatedThread(threadId));
		} else if (isNotBlank(userId)) {
			threads = forumDao.getThreadsWhereUserHasPosted(userId, numberOfPostsToShow + 1, offset, forumIds[0], forumCategoryId, order);
		} else if (forumCategoryId != -1) {
			threads = forumDao.getThreadsInForumCategory(forumCategoryId, offset, numberOfPostsToShow + 1, order);
		} else {
			threads = forumDao.getThreadsInForums(forumIds, offset, numberOfPostsToShow + 1, order);
		}

        ResolvedUser user = userResolver.resolveUser(request);
        threads = filterByPermission(user.getUsername(), threads);

		if (numberOfPostsToShow > 0 && threads.size() > numberOfPostsToShow) {
			model.put("hasMorePosts", Boolean.TRUE);
			threads = threads.subList(0, threads.size() - 1);
		}

		for (ForumThread thread : threads) {
            for (Post post : thread.getPosts()) {
                objectIds.add(String.valueOf(post.getId()));
            }
		}
		List<Rating> ratings = new ArrayList<>(ratingService.getRatingsForObjects(objectIds, "forum"));
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

    private List<ForumThread> filterByPermission(String userId, List<ForumThread> threads) {
        List<ForumThread> filteredThreads = new ArrayList<>(threads.size());
        for (ForumThread thread : threads) {
            if(permissionManager.hasPermission(userId, Permission.VIEW, thread)){
                filteredThreads.add(thread);
            }
        }
        return filteredThreads;
    }

	public class ExtendedRating extends Rating {

		private String userFullName;

		public ExtendedRating(Rating rating) {
			this.setUserid(rating.getUserid());
			this.setContext(rating.getContext());
			this.setRating(rating.getRating());
			this.setObjectId(rating.getObjectId());
			this.setComment(rating.getComment());
			this.setDate(rating.getDate());
		}

		public String getUserFullName() {
			return userFullName;
		}

		public void setUserFullName(String userFullName) {
			this.userFullName = userFullName;
		}
	}
}
