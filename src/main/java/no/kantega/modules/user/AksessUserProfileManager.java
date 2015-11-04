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

            return new AksessUserProfile(aksessUser.getId(), aksessUser.getName(), aksessUser.getEmail(), "", source);
        } catch (Exception e) {
            log.error("Error getUserProfile", e);
            return null;
        }
    }

}

