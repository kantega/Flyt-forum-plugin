package no.kantega.projectweb.control.project;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.control.participant.dto.ParticipantDto;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.permission.GlobalPermissions;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:56:55
 * To change this template use File | Settings | File Templates.
 */
public class ProjectController implements Controller {
    private ProjectWebDao dao;
    private UserProfileManager userProfileManager;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long projectId = Long.parseLong(request.getParameter("projectId"));

        Project project = dao.getPopulatedProject(projectId);
        
        map.put("project", project);
        String user = userResolver.resolveUser(request).getUsername();
        boolean mayEdit = permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR) ||
                permissionManager.hasPermission(user, Permissions.PROJECT_ADMINISTRATION, project);
        map.put("mayEdit", new Boolean(mayEdit));
        return new ModelAndView("project", map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
