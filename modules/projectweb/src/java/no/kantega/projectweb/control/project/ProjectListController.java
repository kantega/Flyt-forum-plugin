package no.kantega.projectweb.control.project;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.ActivityStatus;
import no.kantega.modules.user.UserResolver;
import no.kantega.publishing.common.Aksess;
import no.kantega.commons.configuration.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ProjectListController implements Controller {

    private ProjectWebDao dao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        // Hent prosjekter
        String user = userResolver.resolveUser(request).getUsername();
        if(permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR)) {
            map.put("projects", dao.getProjectList());
        } else {
            map.put("projects", dao.getProjectListForUser(user));
        }

        if (user != null && user.length() > 0) {
            // Hent aktiviteter som bruker er ansvarlig for, skal vises på forsida
            DetachedCriteria criteria = DetachedCriteria.forClass(Activity.class);
            criteria.add(Property.forName("assignee").eq(user));

            DetachedCriteria sc = criteria.createCriteria("status");

            // Vis kun de med status som er bestemt skal vises
            Configuration config = Aksess.getConfiguration();
            String[] statuses = config.getStrings("projectweb.frontpage.showstatuses");
            if (statuses != null) {
                Long[] lStatuses = new Long[statuses.length];
                for (int i = 0; i < statuses.length; i++) {
                    ActivityStatus status = dao.getActivityStatusByCode(statuses[i]);
                    if (status != null) {
                        lStatuses[i] = new Long(status.getId());
                    }
                }
                sc.add(Property.forName("id").in(lStatuses));
            }
            List activities = dao.getActivitiesInProject(criteria);
            map.put("activities", activities);
        }
        return new ModelAndView("projectlist", map);
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
