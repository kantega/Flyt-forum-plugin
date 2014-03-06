package no.kantega.modules.user;

import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.publishing.security.data.User;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * User: ANDSKA
 * Date: 24.mai.2006
 * Time: 13:31:51
 * Copyright: Kantega
 */
public class AksessUserSearcher  implements UserSearcher {
    private Logger log = Logger.getLogger(AksessUserSearcher.class);

    private String source = "Aksess";

    public UserProfile[] findUsers(String substring) {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List aksessUsers = realm.searchUsers(substring);

            List users = new ArrayList();
            for (int i = 0; i < aksessUsers.size(); i++) {
                User aksessUser = (User)aksessUsers.get(i);

                final String username = aksessUser.getId();
                final String name = aksessUser.getName();
                final String email = aksessUser.getEmail();
                UserProfile profile = new UserProfile() {
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
                users.add(profile);
            }
            return (UserProfile[]) users.toArray(new UserProfile[0]);
        } catch (Exception e) {
            log.error(e);
            return new UserProfile[0];
        }
    }

    public void setSource(String source) {
        this.source = source;
    }
}
