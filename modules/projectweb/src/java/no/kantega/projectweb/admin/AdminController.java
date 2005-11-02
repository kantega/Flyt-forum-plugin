package no.kantega.projectweb.admin;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 10, 2005
 * Time: 10:41:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class AdminController implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return new ModelAndView("admin");
    }
}
