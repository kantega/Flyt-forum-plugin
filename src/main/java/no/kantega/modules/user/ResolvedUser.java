package no.kantega.modules.user;

import java.util.Collections;
import java.util.List;

public class ResolvedUser {
    private String username;
    private List<String> roles = Collections.emptyList();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
