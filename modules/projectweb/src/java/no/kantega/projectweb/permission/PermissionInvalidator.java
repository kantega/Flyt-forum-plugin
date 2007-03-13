package no.kantega.projectweb.permission;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 26, 2005
 * Time: 4:28:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PermissionInvalidator {
    private List listeners;

    public void invalidate() {
        if(listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                 if(listeners.get(i) instanceof PermissionInvalidationListener) {
                   ((PermissionInvalidationListener) listeners.get(i)).cacheInvalid();
                 }
            }
        }
    }

    public void setListeners(List listeners) {
        this.listeners = listeners;
    }
}
