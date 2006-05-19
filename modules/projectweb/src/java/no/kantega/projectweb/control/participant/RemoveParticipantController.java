package no.kantega.projectweb.control.participant;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.modules.user.UserResolver;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class RemoveParticipantController implements Controller {

    private ProjectWebDao dao;

    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        long projectId = Long.parseLong(request.getParameter("projectId"));
        long participantId = Long.parseLong(request.getParameter("participantId"));
        Project project = dao.getProject(projectId);
        Participant participant = dao.getProjectParticipant(participantId);
        String user = userResolver.resolveUser(request).getUsername();
        boolean allowRemove = permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR) ||
                permissionManager.hasPermission(user, Permissions.PROJECT_ADMINISTRATION, project);
        if(allowRemove && request.getMethod().equals("POST")) {
            dao.removeParticipantFromProject(participantId, projectId);
        }

        return new ModelAndView(new RedirectView("participantlist"), "projectId", new Long(projectId));
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
