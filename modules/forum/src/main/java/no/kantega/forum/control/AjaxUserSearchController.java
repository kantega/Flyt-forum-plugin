package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import no.kantega.modules.user.UserSearcher;

/**
 * User: Anders Skar, Kantega AS
 * Date: Mar 12, 2007
 * Time: 1:02:31 PM
 */
public class AjaxUserSearchController implements Controller {
    UserSearcher userSearcher;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map model = new HashMap();

        String name = request.getParameter("value");
        if (name != null && name.length() >= 3) {
            model.put("userlist", userSearcher.findUsers(name));
        }

        return new ModelAndView("ajax-searchusers", model);
    }

    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }
}
