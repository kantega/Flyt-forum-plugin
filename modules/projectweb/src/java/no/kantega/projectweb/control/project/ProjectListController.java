package no.kantega.projectweb.control.project;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.modules.user.UserResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ProjectListController implements Controller {

    private ProjectWebDao dao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        String user = userResolver.resolveUser(request).getUsername();
        if(permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR)) {
            map.put("projects", dao.getProjectList());
        } else {
            map.put("projects", dao.getProjectListForUser(user));
        }
        return new ModelAndView("projectlist", map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
