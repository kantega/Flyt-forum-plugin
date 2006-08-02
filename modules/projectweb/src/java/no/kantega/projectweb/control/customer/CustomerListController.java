package no.kantega.projectweb.control.customer;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.GroupMembership;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import no.kantega.projectweb.model.Customer;
/**
 * Created by IntelliJ IDEA.
 * User: runmoe
 * Date: 28.jul.2005
 * Time: 08:34:36
 * To change this template use File | Settings | File Templates.
 */

public class CustomerListController implements Controller {

    private ProjectWebDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        String user = userResolver.resolveUser(request).getUsername();
        if(permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR)) {
            map.put("customers", dao.getCustomerList());
        }

        return new ModelAndView("customerlist", map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

}
