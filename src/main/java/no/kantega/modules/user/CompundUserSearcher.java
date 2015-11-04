package no.kantega.modules.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Combining several UserSearchers
 */
public class CompundUserSearcher implements UserSearcher {
    private List<UserSearcher> userSearchers;

    public List<UserProfile> findUsers(String substring) {
        List<UserProfile> profiles = new ArrayList<>();

        for (UserSearcher searcher : userSearchers) {
            List<UserProfile> p = searcher.findUsers(substring);
            profiles.addAll(p);
        }

        return profiles;
    }

    public void setUserSearchers(List<UserSearcher> userSearchers) {
        this.userSearchers = userSearchers;
    }
}
