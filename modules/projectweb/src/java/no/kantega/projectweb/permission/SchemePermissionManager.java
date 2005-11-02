package no.kantega.projectweb.permission;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.model.ProjectRole;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.permission.scheme.PermissionScheme;
import no.kantega.projectweb.permission.scheme.PermissionSchemeManager;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:07:45
 * To change this template use File | Settings | File Templates.
 */
public class SchemePermissionManager implements PermissionManager {

    private PermissionSchemeManager permissionSchemeManager;

    public boolean hasPermission(String user, long permission, Project project) {
        return permissionSchemeManager.hasPermission(user, permission, project);
    }

    public boolean hasGlobalPermission(String user, long permission) {
        return permissionSchemeManager.hasGlobalPermission(user, permission);
    }

    public void setPermissionSchemeManager(PermissionSchemeManager permissionSchemeManager) {
        this.permissionSchemeManager = permissionSchemeManager;
    }
}
