package no.kantega.filesearch;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 8, 2007
 * Time: 12:43:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class Root {
    private String url;
    private String password;
    private String siteAlias;
    private String username;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSiteAlias() {
        return siteAlias;
    }

    public void setSiteAlias(String siteAlias) {
        this.siteAlias = siteAlias;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
