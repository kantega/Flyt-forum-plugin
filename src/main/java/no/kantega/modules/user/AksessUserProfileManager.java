package no.kantega.modules.user;

import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AksessUserProfileManager extends AbstractUserProfileManager {
    private Logger log = LoggerFactory.getLogger(AksessUserProfileManager.class);

    private String source = "Aksess";

    public UserProfile getUserProfile(final String user) {

        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            User aksessUser = realm.lookupUser(user);
            if (aksessUser == null) {
                return null;
            }

            return new UserProfile() {
                public String getUser() {
                    return aksessUser.getId();
                }

                public String getFullName() {
                    return aksessUser.getName();
                }

                public String getEmail() {
                    return aksessUser.getEmail();
                }

                public String getPhone() {
                    return "";
                }

                public String getSource() {
                    return source;
                }
            };
        } catch (Exception e) {
            log.error("Error getUserProfile", e);
            return null;
        }
    }
}

