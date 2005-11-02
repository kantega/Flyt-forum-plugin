package no.kantega.projectweb.control.activity;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.ActivityType;
import no.kantega.projectweb.model.ActivityPriority;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.projectweb.propertyeditors.ActivityTypeEditor;
import no.kantega.projectweb.propertyeditors.ActivityPriorityEditor;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.osworkflow.BasicWorkflowFactory;
import com.opensymphony.workflow.Workflow;


public class AddActivityController extends FormControllerSupport {

    private BasicWorkflowFactory workflowFactory;
    private UserProfileManager userProfileManager;

    private UserResolver userResolver;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Activity a = new Activity();
        long projectId = Long.parseLong(request.getParameter("projectId"));
        a.setProject(dao.getProject(projectId));
        a.setStartDate(new Date());
        a.setReporter(userResolver.resolveUser(request).getUsername());
        return a;
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {

        Activity activity = (Activity) o;
        Map args = new HashMap();
        args.put("activity", activity);
        Workflow workflow = workflowFactory.createBasicWorkflow(userResolver.resolveUser(request).getUsername());
        workflow.initialize("activity", 1, args);
        long projectId = Long.parseLong(request.getParameter("projectId"));
        return new ModelAndView(new RedirectView("activitylist"), "projectId", Long.toString(projectId));
    }

    public void setWorkflowFactory(BasicWorkflowFactory workflowFactory) {
        this.workflowFactory = workflowFactory;
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Map map = new HashMap();
        map.put("types", dao.getActivityTypes());
        map.put("priorities", dao.getActivityPriorities());
        Activity activity = (Activity) object;
        map.put("project", activity.getProject());
        map.put("phases", dao.getProjectPhases());
        List projectParticipants = dao.getProjectParticipants(activity.getProject().getId());
        map.put("profiles", userProfileManager.getUserProfileDtos(projectParticipants));

        return map;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
