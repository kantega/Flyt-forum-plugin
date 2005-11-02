package no.kantega.projectweb.user;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:16:26
 * To change this template use File | Settings | File Templates.
 */
public interface UserProfileManager {

    public UserProfile getUserProfile(String user);

    List getUserProfileDtos(List activityPriorities);
}
