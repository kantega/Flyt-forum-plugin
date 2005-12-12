package no.kantega.projectweb.admin;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.model.GroupMembership;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.user.UserSearcher;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class AddAdministratorController implements Controller {

    private ProjectWebDao dao;

    private UserSearcher userSearcher;
    private String administratorGroup;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        if(request.getMethod().equals("POST") && request.getParameter("user") != null) {
            String userName = request.getParameter("user");
            dao.addUserToGroup(userName, administratorGroup);
            return new ModelAndView(new RedirectView("administratorlist"));
        } else {
            String query = request.getParameter("query");
            if(query != null && query.length() >  0) {
                map.put("results", userSearcher.findUsers(query));
                Map currentAdmins = new HashMap();
                GroupMembership[] group = dao.getMembersInGroup(administratorGroup);
                for (int i = 0; i < group.length; i++) {
                    GroupMembership groupMembership = group[i];
                    currentAdmins.put(groupMembership.getUser(), groupMembership);
                }
                map.put("currentAdmins", currentAdmins);
                map.put("query", query);
            }
            return new ModelAndView("addadministrator", map);
        }

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setUserSearcher(UserSearcher userSearcher) {
        this.userSearcher = userSearcher;
    }

    public void setAdministratorGroup(String administratorGroup) {
        this.administratorGroup = administratorGroup;
    }
}
