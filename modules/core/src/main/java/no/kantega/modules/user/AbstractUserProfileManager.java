package no.kantega.modules.user;


/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 27, 2005
 * Time: 12:45:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserProfileManager implements UserProfileManager {

    public abstract UserProfile getUserProfile(String user);


}