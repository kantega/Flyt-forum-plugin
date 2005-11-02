package no.kantega.projectweb.user;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 2, 2005
 * Time: 6:34:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface UserSearcher {
    public UserProfile[] findUsers(String substring);
}
