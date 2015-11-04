package no.kantega.modules.user;

import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AksessUserSearcher  implements UserSearcher {
    private Logger log = LoggerFactory.getLogger(AksessUserSearcher.class);

    private String source = "Aksess";

    public List<UserProfile> findUsers(String substring) {
        try {
            SecurityRealm realm = SecurityRealmFactory.getInstance();

            List<User> aksessUsers = realm.searchUsers(substring);

            List<UserProfile> users = new ArrayList<>();
            for (User aksessUser : aksessUsers) {

                final String username = aksessUser.getId();
                final String name = aksessUser.getName();
                final String email = aksessUser.getEmail();
                users.add(new AksessUserProfile(username, name, email, "", source));
            }
            return users;
        } catch (Exception e) {
            log.error("Error findUsers", e);
            return Collections.emptyList();
        }
    }

    public void setSource(String source) {
        this.source = source;
    }
}
