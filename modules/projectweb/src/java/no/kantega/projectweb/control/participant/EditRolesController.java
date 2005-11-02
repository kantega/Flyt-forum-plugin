package no.kantega.projectweb.control.participant;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.model.ProjectRole;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.role.RoleManager;
import no.kantega.projectweb.viewmodel.HasRole;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class EditRolesController implements Controller {

    private ProjectWebDao dao;

    private RoleManager roleManager;
    private UserProfileManager userProfileManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long participantId = Long.parseLong(request.getParameter("participantId"));
        Participant p = dao.getPopulatedParticipant(participantId);

        if(request.getMethod().equals("POST")) {
            //Clear roles
            p.setRoles(new HashSet());

            ProjectRole[] allRoles = roleManager.getRolesForProject(p.getProject());

            for (int i = 0; i < allRoles.length; i++) {
                ProjectRole role = allRoles[i];
                String hasRole = request.getParameter("has_" + role.getId());
                System.out.println("hasrole is :" + hasRole);
                if(hasRole != null && hasRole.equals("on")) {
                    p.getRoles().add(role);
                }
            }
            dao.saveOrUpdate(p);
            return new ModelAndView(new RedirectView("participantlist"), "projectId", new Long(p.getProject().getId()));

        } else {
            map.put("profile", userProfileManager.getUserProfile(p.getUser()));
            map.put("participant", p);
            map.put("project", p.getProject());

            Set currentRoles = p.getRoles();

            ProjectRole[] allRoles = roleManager.getRolesForProject(p.getProject());

            List hasRoles = new ArrayList();

            for (int i = 0; i < allRoles.length; i++) {

                ProjectRole role = allRoles[i];
                HasRole has = new HasRole();
                has.setRole(role);
                has.setHasRole(false);

                Iterator it = currentRoles.iterator();
                boolean found = false;
                while (it.hasNext()) {
                    ProjectRole projectRole = (ProjectRole) it.next();
                    if(projectRole.getId() == role.getId()) {
                       found = true;
                        has.setHasRole(true);
                        break;
                    }
                }
                hasRoles.add(has);

            }
            map.put("hasRoles", hasRoles);

            return new ModelAndView("editparticipantroles", map);
        }
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }


    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }
}
