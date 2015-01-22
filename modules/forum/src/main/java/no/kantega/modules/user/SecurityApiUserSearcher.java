package no.kantega.modules.user;

import no.kantega.security.api.profile.Profile;
import no.kantega.security.api.profile.ProfileManager;
import no.kantega.security.api.search.SearchResult;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jan 27, 2007
 * Time: 11:25:51 AM
 */
public class SecurityApiUserSearcher implements UserSearcher {
    private Logger log = Logger.getLogger(SecurityApiUserSearcher.class);

    private String source = "SecurityApiUserSearcher";

    private ProfileManager profileManager;

    public UserProfile[] findUsers(String substring) {
        try {
            SearchResult result = profileManager.searchProfiles(substring);

            List users = new ArrayList();
            Iterator profiles = result.getAllResults();
            while (profiles.hasNext()) {
                Profile p =  (Profile)profiles.next();
                final String username = p.getIdentity().getDomain() + ":" + p.getIdentity().getUserId();
                final String domain = p.getIdentity().getDomain();
                final String name = p.getGivenName() + " " + p.getSurname();
                final String email = p.getEmail();

                UserProfile userProfile = new UserProfile() {
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
                users.add(userProfile);

            }
            return (UserProfile[]) users.toArray(new UserProfile[0]);
        } catch (Exception e) {
            log.error(e);
            return new UserProfile[0];
        }
    }
}
