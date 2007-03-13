package no.kantega.modules.user;

import no.kantega.security.api.profile.ProfileManager;
import no.kantega.security.api.profile.Profile;
import no.kantega.security.api.identity.Identity;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.publishing.security.data.User;
import org.apache.log4j.Logger;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 10:49:50 AM
 */
public class SecurityApiProfileManager extends AbstractUserProfileManager {
    private Logger log = Logger.getLogger(AksessUserProfileManager.class);

    private String source = "SecurityApiProfileManager";

    private ProfileManager profileManager;

    public UserProfile getUserProfile(final String user) {

        try {
            Identity identity = SecurityApiHelper.createApiIdentity(user);
            Profile p = profileManager.getProfileForUser(identity);
            if (p == null) {
                return null;
            }

            final String username = p.getIdentity().getDomain() + ":" + p.getIdentity().getUserId();
            final String domain = p.getIdentity().getDomain();
            final String name = p.getGivenName() + " " + p.getSurname();
            final String email = p.getEmail();

            return new UserProfile() {
                public String getUser() {
                    return username;
                }

                public String getFullName() {
                    return name;
                }

                public String getEmail() {
                    return email;
                }

                public String getPhone() {
                    return "";
                }

                public String getSource() {
                    return domain;
                }
            };
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
}
