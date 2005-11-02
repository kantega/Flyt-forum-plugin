package no.kantega.projectweb.permission;

import no.kantega.projectweb.model.Project;
import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 21, 2005
 * Time: 4:08:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CachedPermissionManager implements PermissionManager, PermissionInvalidationListener {
    private Cache cache = new Cache(true, true, true);
    private int refreshPeriod = 60;
    private PermissionManager realPermissionManager;

    public boolean hasPermission(String user, long permission, Project project) {
        String key = user +"-" + +permission + "-" + project.getId();

        Boolean permitted  = null;
        try {
            permitted = (Boolean) cache.getFromCache(key, refreshPeriod);
        } catch (NeedsRefreshException e) {
            try {
                permitted = new Boolean(realPermissionManager.hasPermission(user, permission,  project));
                cache.putInCache(key, permitted);
            } catch (Exception e1) {
                cache.cancelUpdate(key);
            }
        }
        return permitted.booleanValue();
    }

    public boolean hasGlobalPermission(String user, long permission) {
        String key = user +"-" + +permission;

        Boolean permitted  = null;
        try {
            permitted = (Boolean) cache.getFromCache(key, refreshPeriod);
        } catch (NeedsRefreshException e) {
            try {
                permitted = new Boolean(realPermissionManager.hasGlobalPermission(user, permission));
                cache.putInCache(key, permitted);
            } catch (Exception e1) {
                cache.cancelUpdate(key);
            }
        }
        return permitted.booleanValue();

    }

    public void setRefreshPeriod(int refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }

    public void setRealPermissionManager(PermissionManager realPermissionManager) {
        this.realPermissionManager = realPermissionManager;
    }

    public void cacheInvalid() {
        cache.flushAll(new Date());
    }
}
