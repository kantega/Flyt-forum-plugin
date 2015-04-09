package no.kantega.modules.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Combining several UserSearchers
 */
public class CompundUserSearcher implements UserSearcher {
    private List<UserSearcher> userSearchers;

    public UserProfile[] findUsers(String substring) {
        List<UserProfile> profiles = new ArrayList<>();

        for (UserSearcher searcher : userSearchers) {
            UserProfile[] p = searcher.findUsers(substring);
            profiles.addAll(Arrays.asList(p));
        }

        return profiles.toArray(new UserProfile[profiles.size()]);
    }

    public void setUserSearchers(List<UserSearcher> userSearchers) {
        this.userSearchers = userSearchers;
    }
}
