package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.util.ForumUtil;
import no.kantega.forum.util.ForumPostReadStatus;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 08.des.2005
 * Time: 15:06:25
 * To change this template use File | Settings | File Templates.
 */
public class ListCategoriesController implements Controller {
    private ForumDao dao;
    private UserResolver userResolver;
    private PermissionManager permissionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        Date lastVisit = ForumUtil.getLastVisit(request, response, true);
        map.put("lastVisit", lastVisit);
        
        List cats = dao.getForumCategories();

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

        for (int i = 0; i < cats.size(); i++) {
            ForumCategory category = (ForumCategory) cats.get(i);
            Iterator forums  = category.getForums().iterator();
            while (forums.hasNext()) {
                Forum forum = (Forum) forums.next();
                if (!permissionManager.hasPermission(username, Permissions.VIEW, forum)) {
                    // User does not have access to forum
                    forums.remove();
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
        }


        if (username != null) {
            // Hent innlegg som brukeren kan godkjenne
            List myUnapprovedPosts = new ArrayList();
            List allUnapprovedPosts = dao.getUnapprovedPosts();
            for (int i = 0; i < allUnapprovedPosts.size(); i++) {
                Post p =  (Post)allUnapprovedPosts.get(i);
                if (permissionManager.hasPermission(username, Permissions.APPROVE_POST, p)) {
                    myUnapprovedPosts.add(p);
                }
            }
            if (myUnapprovedPosts.size() > 0) {
                map.put("unapprovedPostCount", new Integer(myUnapprovedPosts.size()));
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
