package no.kantega.projectweb.control.customer;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.GroupMembership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AddCustomerUserController implements Controller {

    private ProjectWebDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return new ModelAndView("addcustomeruser");
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

}
