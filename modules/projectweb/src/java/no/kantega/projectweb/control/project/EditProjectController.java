package no.kantega.projectweb.control.project;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Errors;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Duration;
import no.kantega.projectweb.model.Hours;
import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.util.ProjectWebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.DateFormat;

public class EditProjectController extends FormControllerSupport implements ApplicationContextAware {

    private UserProfileManager userProfileManager;
    private PermissionManager permissionManager;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long projectId = new Long(request.getParameter("projectId")).longValue();
        return dao.getProject(projectId);

    }

    protected ModelAndView onSubmit(Object o) throws Exception {
        Project project = (Project) o;
        dao.saveOrUpdate(project);
        return new ModelAndView(new RedirectView("project"), "projectId", Long.toString(project.getId()));
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Map map = new HashMap();
        Project project = (Project) object;
        List projectParticipants = dao.getProjectParticipants(project.getId());
        map.put("profiles", ProjectWebUtil.getUserProfileDtos(userProfileManager, projectParticipants));
        return map;
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}
