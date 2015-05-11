package no.kantega.forum.control;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Post;
import no.kantega.forum.permission.Permission;
import no.kantega.forum.permission.PermissionManager;
import no.kantega.modules.user.ResolvedUser;
import no.kantega.modules.user.UserResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUnapprovedController implements Controller {
    private ForumDao dao;
    private UserResolver userResolver;
    private PermissionManager permissionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        ResolvedUser user = userResolver.resolveUser(request);
        if (user != null && user.getUsername() != null) {
            String username = user.getUsername();

            // Hent innlegg som brukeren kan godkjenne
            List<Post> myUnapprovedPosts = new ArrayList<>();
            List<Post> allUnapprovedPosts = dao.getUnapprovedPosts();
            for (Post p : allUnapprovedPosts) {
                if (permissionManager.hasPermission(username, Permission.APPROVE_POST, p)) {
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

