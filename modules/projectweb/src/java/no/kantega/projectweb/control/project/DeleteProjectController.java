package no.kantega.projectweb.control.project;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.projectweb.permission.GlobalPermissions;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.ActivityStatus;
import no.kantega.modules.user.UserResolver;
import no.kantega.commons.configuration.Configuration;
import no.kantega.publishing.common.Aksess;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: kriove
 * Date: 27.sep.2007
 * Time: 12:15:29
 * To change this template use File | Settings | File Templates.
 */
public class DeleteProjectController implements Controller {

    private ProjectWebDao dao;
    private PermissionManager permissionManager;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
         if(request.getMethod().equals("POST")) {
            long projectId = Long.parseLong(request.getParameter("projectid"));
            Project project = dao.getProject(projectId);
            String user = userResolver.resolveUser(request).getUsername();
            if (permissionManager.hasPermission(user, Permissions.PROJECT_ADMINISTRATION, project)){
                dao.deleteProject(projectId);
                if(permissionManager.hasGlobalPermission(user, GlobalPermissions.ADMINISTRATOR)) {
                    map.put("projects", dao.getProjectList());
                } else {
                    map.put("projects", dao.getProjectListForUser(user));
                }
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
                return new ModelAndView("projectlist", map);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            }
         } else {
            // GET should never be used here
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

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
