package no.kantega.projectweb.user;

import no.kantega.publishing.security.realm.ksi.adminservice.UserProfileAdminProxy;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 11, 2005
 * Time: 3:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class KsiUserProfileManager extends AbstractUserProfileManager {
    private UserProfileAdminProxy proxy;

    private Logger log = Logger.getLogger(KsiUserProfileManager.class);

    private String source;

    public UserProfile getUserProfile(final String user) {

        try {
            final ksc.vo.userprofile.UserProfile profile = proxy.lookup(user);
            return new UserProfile() {
                public String getUser() {
                    return user;
                }

                public String getFullName() {
                    return profile.getFirstName() + " " + profile.getLastName();
                }

                public String getEmail() {
                    return profile.getEmail();
                }

                public String getPhone() {
                    return "";
                }

                public String getSource() {
                    return source;
                }
            };
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public void setProxy(UserProfileAdminProxy proxy) {
        this.proxy = proxy;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
