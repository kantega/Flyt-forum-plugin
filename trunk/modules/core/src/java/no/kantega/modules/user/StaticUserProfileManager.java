package no.kantega.modules.user;

import no.kantega.modules.user.AbstractUserProfileManager;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 11, 2005
 * Time: 3:23:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaticUserProfileManager extends AbstractUserProfileManager {
    public UserProfile getUserProfile(final String user) {
        return new UserProfile() {
            public String getUser() {
                return user;
            }

            public String getFullName() {
                return user.substring(0,1).toUpperCase() + ((user.length() > 1) ? user.substring(1) : "");
            }

            public String getEmail() {
                return "";
            }

            public String getPhone() {
                return "";
            }

            public String getSource() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
    }
}
