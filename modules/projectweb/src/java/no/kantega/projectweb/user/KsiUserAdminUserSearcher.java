package no.kantega.projectweb.user;

import no.kantega.publishing.security.realm.ksi.adminservice.UserProfileAdminProxy;
import ksc.vo.userprofile.UserProfileBrief;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 11, 2005
 * Time: 3:06:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class KsiUserAdminUserSearcher implements UserSearcher {
    private UserProfileAdminProxy proxy;
    private Logger log = Logger.getLogger(KsiUserAdminUserSearcher.class);
    private String source;

    public UserProfile[] findUsers(String substring) {
        try {
            UserProfileBrief[] brief = proxy.findUsers(substring);

            List users = new ArrayList();
            for (int i = 0; i < brief.length; i++) {
                final UserProfileBrief userProfileBrief = brief[i];

                UserProfile profile = new UserProfile() {
                    public String getUser() {
                        return userProfileBrief.getUserName();
                    }

                    public String getFullName() {
                        return userProfileBrief.getFirstName() +" "+ userProfileBrief.getLastName();
                    }

                    public String getEmail() {
                        return "";

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
