package no.kantega.projectweb.control.user;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.user.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 23:26:55
 * To change this template use File | Settings | File Templates.
 */
public class UserListController implements Controller {

    private UserManager userManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        map.put("users", userManager.getUsers());
        return new ModelAndView("userlist", map);
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
