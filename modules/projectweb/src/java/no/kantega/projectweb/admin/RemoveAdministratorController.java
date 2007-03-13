package no.kantega.projectweb.admin;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class RemoveAdministratorController implements Controller {

    private ProjectWebDao dao;

    private String administratorGroup;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String user = request.getParameter("user");
        dao.removeUserFromGroup(user, administratorGroup);
        return new ModelAndView(new RedirectView("administratorlist"));
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAdministratorGroup(String administratorGroup) {
        this.administratorGroup = administratorGroup;
    }
}
