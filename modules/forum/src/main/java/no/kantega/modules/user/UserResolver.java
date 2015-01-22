package no.kantega.modules.user;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 14:58:41
 * To change this template use File | Settings | File Templates.
 */
public interface UserResolver {
    public ResolvedUser resolveUser(HttpServletRequest request);
}
