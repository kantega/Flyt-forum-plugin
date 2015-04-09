package no.kantega.modules.user;

import java.util.List;

/**
 * Search several UserProfileManagers
 */
public class CompoundUserProfileManager extends AbstractUserProfileManager {
    private List<UserProfileManager> userManagers;

    public UserProfile getUserProfile(String user) {
        for (UserProfileManager manager : userManagers) {
            UserProfile profile = manager.getUserProfile(user);
            if (profile != null) {
                return profile;
            }
        }
        return null;
    }

    public void setUserManagers(List<UserProfileManager> userManagers) {
        this.userManagers = userManagers;
    }
}
