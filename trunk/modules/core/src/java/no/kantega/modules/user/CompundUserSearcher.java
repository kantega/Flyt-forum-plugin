package no.kantega.modules.user;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Nov 1, 2005
 * Time: 11:12:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompundUserSearcher implements UserSearcher {
    private List userSearchers;

    public UserProfile[] findUsers(String substring) {
        List profiles = new ArrayList();

        for (int i = 0; i < userSearchers.size(); i++) {
            UserSearcher searcher = (UserSearcher) userSearchers.get(i);
            UserProfile[] p = searcher.findUsers(substring);
            profiles.addAll(Arrays.asList(p));
        }

        return (UserProfile[]) profiles.toArray(new UserProfile[0]);
    }

    public void setUserSearchers(List userSearchers) {
        this.userSearchers = userSearchers;
    }
}
