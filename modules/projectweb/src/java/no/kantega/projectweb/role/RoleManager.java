package no.kantega.projectweb.role;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.ProjectRole;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 16:17:25
 * To change this template use File | Settings | File Templates.
 */
public interface RoleManager {

    public ProjectRole[] getRolesForProject(Project p);
}
