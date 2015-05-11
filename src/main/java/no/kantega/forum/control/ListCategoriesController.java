package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.forum.util.ForumUtil;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class ListCategoriesController implements Controller {
    private ForumDao dao;
    private UserResolver userResolver;
    private PermissionManager permissionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Date lastVisit = ForumUtil.getLastVisit(request, response, true);
        map.put("lastVisit", lastVisit);

        List<ForumCategory> cats = dao.getForumCategories();

        String username = null;
        ResolvedUser user = userResolver.resolveUser(request);
        if (user != null) {
            username = user.getUsername();
        }

        // Hent nye poster siden siste besøk
        ForumPostReadStatus readStatus = new ForumPostReadStatus(request);
        List unreadPosts = null;

        if (lastVisit != null) {
            unreadPosts = dao.getPostsAfterDate(lastVisit);
        }

	    boolean canCreateForum = permissionManager.hasPermission(username, Permission.EDIT_FORUM, null);


		Iterator<ForumCategory> catsIterator = cats.iterator();

		while (catsIterator.hasNext()){
			ForumCategory cat = catsIterator.next();
			Iterator<Forum> forumIterator = cat.getForums().iterator();
			while (forumIterator.hasNext()){
				Forum forum = forumIterator.next();
				if (!permissionManager.hasPermission(username, Permission.VIEW, forum)) {
					// User does not have access to forum
					forumIterator.remove();
				} else {
					List lastPostsInForum = dao.getLastPostsInForum(forum.getId(), 1);
					if(lastPostsInForum.size() > 0) {
						forum.setLastPost((Post) lastPostsInForum.get(0));
					}
					if (unreadPosts != null) {
						readStatus.updateUnreadPostsInForum(unreadPosts, forum);
					}
				}

			}
			if (cat.getForums().isEmpty() && !canCreateForum) catsIterator.remove();
		}

        if (username != null) {
            // Hent innlegg som brukeren kan godkjenne
            List<Post> myUnapprovedPosts = new ArrayList<>();
            List<Post> allUnapprovedPosts = dao.getUnapprovedPosts();
            for (Post post : allUnapprovedPosts) {
                if (permissionManager.hasPermission(username, Permission.APPROVE_POST, post)) {
                    myUnapprovedPosts.add(post);
                }
            }
            if (myUnapprovedPosts.size() > 0) {
                map.put("unapprovedPostCount", myUnapprovedPosts.size());
            }
        }

        map.put("categories", cats);

        return new ModelAndView("listcategories", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
