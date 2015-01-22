package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.forum.permission.Permissions;
import no.kantega.forum.model.Post;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.ResolvedUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 9, 2007
 * Time: 3:10:22 PM
 */
public class ListUnapprovedController implements Controller {
    private ForumDao dao;
    private UserResolver userResolver;
    private PermissionManager permissionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        ResolvedUser user = userResolver.resolveUser(request);
        if (user != null && user.getUsername() != null) {
            String username = user.getUsername();

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
                map.put("unapprovedPosts", myUnapprovedPosts);
            }
        }

        return new ModelAndView("listunapproved", map);
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

