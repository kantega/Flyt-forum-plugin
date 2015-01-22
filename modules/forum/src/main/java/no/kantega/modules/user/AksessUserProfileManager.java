package no.kantega.modules.user;

import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.apache.log4j.Logger;

/**
 * User: ANDSKA
 * Date: 24.mai.2006
 * Time: 13:40:09
 * Copyright: Kantega
 */
public class AksessUserProfileManager extends AbstractUserProfileManager {
    private Logger log = Logger.getLogger(AksessUserProfileManager.class);

    private String source = "Aksess";

    public UserProfile getUserProfile(final String user) {

        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            User aksessUser = realm.lookupUser(user);
            if (aksessUser == null) {
                return null;
            }

            final String username = aksessUser.getId();
            final String name = aksessUser.getName();
            final String email = aksessUser.getEmail();

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
                    return source;
                }
            };
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public void setSource(String source) {
        this.source = source;
    }
}

