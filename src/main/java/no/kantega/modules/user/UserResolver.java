package no.kantega.modules.user;

import javax.servlet.http.HttpServletRequest;


public interface UserResolver {
    ResolvedUser resolveUser(HttpServletRequest request);
}
