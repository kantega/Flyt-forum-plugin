package no.kantega.projectweb.user;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 11, 2005
 * Time: 3:17:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompoundUserProfileManager extends AbstractUserProfileManager {
    private List userManagers;
    
    public UserProfile getUserProfile(String user) {
        for (int i = 0; i < userManagers.size(); i++) {
            UserProfileManager manager =  (UserProfileManager) userManagers.get(i);
            UserProfile profile = manager.getUserProfile(user);
            if(profile != null) {
                return profile;
            }
        }
        return null;
    }

    public void setUserManagers(List userManagers) {
        this.userManagers = userManagers;
    }
}
