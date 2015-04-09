package no.kantega.modules.user;

import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AksessUserSearcher  implements UserSearcher {
    private Logger log = Logger.getLogger(AksessUserSearcher.class);

    private String source = "Aksess";

    public UserProfile[] findUsers(String substring) {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List<User> aksessUsers = realm.searchUsers(substring);

            List<UserProfile> users = new ArrayList<>();
            for (User aksessUser : aksessUsers) {

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
            return users.toArray(new UserProfile[users.size()]);
        } catch (Exception e) {
            log.error(e);
            return new UserProfile[0];
        }
    }

    public void setSource(String source) {
        this.source = source;
    }
}
