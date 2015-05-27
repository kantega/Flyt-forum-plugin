package no.kantega.forum.control;

import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 8, 2007
 * Time: 1:14:49 PM
 */
public class ApprovePostController  implements Controller {
    private ForumDao dao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("postId"));
        Post p = dao.getPopulatedPost(id);

        String userName = getUsername(request);

        if (!permissionManager.hasPermission(userName, Permission.APPROVE_POST, p)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        dao.approve(p);
        return new ModelAndView(new RedirectView("listunapproved"));
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    private String getUsername(HttpServletRequest request) {
        String userName = null;
        ResolvedUser user = userResolver.resolveUser(request);
        if(user != null) {
            userName = user.getUsername();
        }
        return userName;
    }
}
