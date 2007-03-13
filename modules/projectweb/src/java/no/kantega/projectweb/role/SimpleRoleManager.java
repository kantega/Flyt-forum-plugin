package no.kantega.projectweb.role;

import no.kantega.projectweb.model.ProjectRole;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.dao.ProjectWebDao;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 16:18:17
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRoleManager implements RoleManager {

    private ProjectWebDao dao;

    public ProjectRole[] getRolesForProject(Project p) {
        return dao.getAllRoles();
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
