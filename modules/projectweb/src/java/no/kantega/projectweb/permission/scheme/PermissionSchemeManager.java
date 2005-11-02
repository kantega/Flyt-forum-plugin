package no.kantega.projectweb.permission.scheme;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.model.ProjectRole;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.permission.GlobalPermissions;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 23:47:32
 * To change this template use File | Settings | File Templates.
 */
public class PermissionSchemeManager {

    private ProjectWebDao dao;
    private Logger log = Logger.getLogger(PermissionSchemeManager.class);
    private String[] administratorRoles = new String[0];

    public boolean hasPermission(String user, long permission, Project project) {
        PermissionScheme scheme = dao.getPopulatedPermissionScheme(project.getPermissionSchemeId());

        log.info("Permission scheme '" + scheme.getName() +"' for project " + project.getName());

        Participant participant = dao.getProjectParticipant(project.getId(), user);
        if(participant == null) {
            log.info("User " + user + " is not participant in project");
            return false;
        }

        Set entries = scheme.getPermissionEntries();
        if(entries != null) {
            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                PermissionEntry permissionEntry = (PermissionEntry) iterator.next();
                log.info("Checking entry with permission " + permissionEntry.getPermission());
                if(permissionEntry.getPermission() == permission) {
                    if(hasRole(participant.getRoles(), permissionEntry.getRoles())) {
                        return true;
                    }
                }
            }
        } else {
            log.warn("Permission scheme " + scheme.getId() +" contains no entries");
        }
        return false;
    }

    private boolean hasRole(Set userRoles, Set requiredRoles) {
        Iterator ri = requiredRoles.iterator();
        while (ri.hasNext()) {
            ProjectRole projectRole = (ProjectRole) ri.next();
            log.info("Testing role " + projectRole.getCode());
            Iterator ui = userRoles.iterator();
            while (ui.hasNext()) {
                ProjectRole role = (ProjectRole) ui.next();
                if(projectRole.getId() == role.getId()) {
                    log.info("  matched against " + role.getCode());
                    return true;
                }
                log.info("  no match against " + role.getCode());
            }

        }
        log.info("User does not have one of the required role");
        return false;
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public boolean hasGlobalPermission(String user, long permission) {

        if(permission == GlobalPermissions.ADMINISTRATOR) {
            for (int i = 0; i < administratorRoles.length; i++) {
                String role = (String) administratorRoles[i];
                if(dao.isUserInGroup(user, role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setAdministratorRoles(String administratorRoles) {
        if(administratorRoles != null) {
            this.administratorRoles = administratorRoles.split(",");
        }
    }
}
