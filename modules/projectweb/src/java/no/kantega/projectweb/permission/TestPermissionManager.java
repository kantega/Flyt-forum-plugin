package no.kantega.projectweb.permission;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.model.ProjectRole;
import no.kantega.projectweb.dao.ProjectWebDao;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:07:45
 * To change this template use File | Settings | File Templates.
 */
public class TestPermissionManager implements PermissionManager {

    private ProjectWebDao dao;

    public boolean hasPermission(String user, long permission, Project project) {
        if(permission == Permissions.PROJECT_ADMINISTRATION) {
            Participant p = dao.getProjectParticipant(project.getId(), user);
            if(p != null) {
                Iterator rolesIt = p.getRoles().iterator();
                while (rolesIt.hasNext()) {
                    ProjectRole projectRole = (ProjectRole) rolesIt.next();
                    if(projectRole.getCode().equals("prosjektleder")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasGlobalPermission(String user, long permission) {
        return false; 
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
