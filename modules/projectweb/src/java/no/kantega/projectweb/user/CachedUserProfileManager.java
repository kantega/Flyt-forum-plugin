package no.kantega.projectweb.user;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 19, 2005
 * Time: 10:55:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class CachedUserProfileManager extends AbstractUserProfileManager {
    private Cache cache = new Cache(true, true, true);
    private int refreshPeriod = 1000;
    private UserProfileManager realManager;

    public UserProfile getUserProfile(String user) {
        UserProfile profile = null;
        try {
            profile = (UserProfile) cache.getFromCache(user, refreshPeriod);
        } catch (NeedsRefreshException e) {

            try {
                profile = realManager.getUserProfile(user);
                cache.putInCache(user, profile);
            } catch (Exception ex) {
                cache.cancelUpdate(user);
            }
        }
        return profile;
    }

    public void setRealManager(UserProfileManager realManager) {
        this.realManager = realManager;
    }
}
