package no.kantega.projectweb.control.user;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.user.UserManager;
import com.opensymphony.user.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 23:26:55
 * To change this template use File | Settings | File Templates.
 */
public class AddUserController implements Controller {

    private UserManager userManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        User user = userManager.createUser(request.getParameter("username"));
        user.setFullName(request.getParameter("fullname"));
        user.setEmail(request.getParameter("email"));
        user.store();
        
        return new ModelAndView(new RedirectView("userlist"));
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
