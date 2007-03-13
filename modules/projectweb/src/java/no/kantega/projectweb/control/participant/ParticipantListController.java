package no.kantega.projectweb.control.participant;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.modules.user.UserResolver;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.projectweb.control.participant.dto.ParticipantDto;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantListController implements Controller {

    private ProjectWebDao dao;

    private PermissionManager permissionManager;
    private UserResolver userResolver;
    private UserProfileManager userProfileManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long projectId = Long.parseLong(request.getParameter("projectId"));
        Project project = dao.getProject(projectId);
        map.put("project", project);
        
        List participants = dao.getProjectParticipants(project.getId());
        List dtos = new ArrayList();
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = (Participant) participants.get(i);
            ParticipantDto dto = new ParticipantDto();
            dto.setParticipant(participant);
            dto.setProfile(userProfileManager.getUserProfile(participant.getUser()));
            dtos.add(dto);
        }
        map.put("participants", dtos);
        String user = userResolver.resolveUser(request).getUsername();
        boolean allowAdd = permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR) || permissionManager.hasPermission(user, Permissions.PROJECT_ADMINISTRATION, project);
        map.put("allowAdd", new Boolean(allowAdd));
        return new ModelAndView("participantlist", map);
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

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }


}
