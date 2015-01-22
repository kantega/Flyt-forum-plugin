package no.kantega.modules.user;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:17:10
 * To change this template use File | Settings | File Templates.
 */
public class TestUserProfileManager extends AbstractUserProfileManager {
    public UserProfile getUserProfile(final String user) {
        return new UserProfile() {
            public String getUser() {
                return user;
            }

            public String getFullName() {
                if(user == null) {
                    return "null";
                }
                if(user.length() < 2) {
                    return user.toUpperCase();
                }

                return user.substring(0,1).toUpperCase()
                        + user.substring(1);
            }

            public String getEmail() {
                return "eirik.bjorsnos@kantega.no";
            }

            public String getPhone() {
                return "41473765";
            }

            public String getSource() {
                return "Test";
            }


        };
    }
}
