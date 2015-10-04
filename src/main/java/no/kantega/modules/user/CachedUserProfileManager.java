package no.kantega.modules.user;

import org.springframework.cache.annotation.Cacheable;


public class CachedUserProfileManager extends AbstractUserProfileManager {
    private UserProfileManager realManager;

    @Cacheable("ForumUserProfileManager")
    public UserProfile getUserProfile(String user) {
        return realManager.getUserProfile(user);
    }

    public void setRealManager(UserProfileManager realManager) {
        this.realManager = realManager;
    }

}
