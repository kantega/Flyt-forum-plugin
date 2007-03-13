package no.kantega.projectweb.permission;

import no.kantega.projectweb.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 23.sep.2005
 * Time: 12:47:25
 * To change this template use File | Settings | File Templates.
 */
public interface PermissionManager {
    public boolean hasPermission(String user, long permission, Project project);
    public boolean hasGlobalPermission(String user, long permission);
}
