package no.kantega.projectweb.admin;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.GroupMembership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AdministratorListController implements Controller {

    private ProjectWebDao dao;
    private String administratorGroup;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map map  = new HashMap();

        GroupMembership[] group = dao.getMembersInGroup(administratorGroup);
        map.put("administrators", group);
        return new ModelAndView("administratorlist", map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAdministratorGroup(String administratorGroup) {
        this.administratorGroup = administratorGroup;
    }
}
