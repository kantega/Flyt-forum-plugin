package no.kantega.forum.jaxrs.bll;

import no.kantega.modules.user.GroupResolver;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-08-04
 */
public class AuthorizationBl {

    private GroupResolver groupResolver;
    private List<String> administratorGroups;

    @Inject
    public AuthorizationBl(@Named("administratorGroups") String administratorGroups, @Named("groupResolver") GroupResolver groupResolver) {
        this.groupResolver = groupResolver;
        this.administratorGroups = administratorGroups != null ? Arrays.asList(administratorGroups.split(",")) : null;
    }

    public boolean isAdministrator(String user) {
        for (String administratorGroup : administratorGroups) {
            if (groupResolver.isInGroup(user, administratorGroup)) {
                return true;
            }
        }
        return false;
    }

    public GroupResolver getGroupResolver() {
        return groupResolver;
    }

    public List<String> getAdministratorGroups() {
        return administratorGroups;
    }
}
