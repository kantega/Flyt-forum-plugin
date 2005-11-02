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
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.user.UserSearcher;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class AddParticipantController implements Controller {

    private ProjectWebDao dao;

    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private UserSearcher userSearcher;
    private String defaultRoles = "";

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long projectId = Long.parseLong(request.getParameter("projectId"));
        Project project = dao.getProject(projectId);
        String user = userResolver.resolveUser(request).getUsername();
        boolean allowAdd = permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR) ||
                permissionManager.hasPermission(user, Permissions.PROJECT_ADMINISTRATION, project);

        if(request.getMethod().equals("POST")) {
            String userName = request.getParameter("user");
            if(allowAdd) {
                Participant participant = new Participant();
                participant.setUser(userName);
                participant.setRoles(new HashSet());
                String[] roles = defaultRoles.split(",");
                for (int i = 0; i < roles.length; i++) {
                    String role = roles[i].trim();
                    participant.getRoles().add(dao.getRoleByCode(role));
                }
                dao.addParticipantToProject(participant, project);
            }
            return new ModelAndView(new RedirectView("participantlist"), "projectId", new Long(projectId));
        } else {
            map.put("project", project);
            String query = request.getParameter("query");
            if(query != null && query.length() >  0) {
                map.put("results", userSearcher.findUsers(query));
                Map currentParticipants = new HashMap();
                Iterator i = dao.getProjectParticipants(project.getId()).iterator();
                while (i.hasNext()) {
                    Participant participant = (Participant) i.next();
                    currentParticipants.put(participant.getUser(), participant);
                }
                map.put("currentParticipants", currentParticipants);
                map.put("query", query);
            }
            return new ModelAndView("addparticipant", map);
        }

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

    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }

    public void setDefaultRoles(String defaultRoles) {
        this.defaultRoles = defaultRoles;
    }
}
