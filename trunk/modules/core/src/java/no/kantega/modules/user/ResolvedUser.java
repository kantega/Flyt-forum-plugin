package no.kantega.modules.user;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 7, 2005
 * Time: 11:20:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResolvedUser {
    private String username;
    private String[] roles = new String[0];

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
