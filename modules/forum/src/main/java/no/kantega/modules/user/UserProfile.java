package no.kantega.modules.user;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 15:14:04
 * To change this template use File | Settings | File Templates.
 */
public interface UserProfile {
    public String getUser();
    public String getFullName();
    public String getEmail();
    public String getPhone();
    public String getSource();

}
