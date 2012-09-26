package no.kantega.modules.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 2, 2005
 * Time: 6:34:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestUserSearcher implements UserSearcher {
    private List usernames;
    public UserProfile[] findUsers(String substring) {
        List found = new ArrayList();
        for (int i = 0; i < usernames.size(); i++) {
            final String user = (String) usernames.get(i);

            UserProfile proile = new UserProfile() {
                public String getUser() {
                    return user;
                }

                public String getFullName() {
                    return user.substring(0,1).toUpperCase() + ((user.length() > 1) ? user.substring(1) : "");
                }

                public String getEmail() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public String getPhone() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                public String getSource() {
                    return "Test";
                }
            };
            if(user.indexOf(substring) != -1) {
                found.add(proile);
            }
        }
        return (UserProfile[]) found.toArray(new UserProfile[0]);
    }

    public void setUsernames(List usernames) {
        this.usernames = usernames;
    }
}
