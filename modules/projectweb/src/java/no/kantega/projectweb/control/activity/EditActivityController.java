package no.kantega.projectweb.control.activity;

import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Activity;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.projectweb.util.ProjectWebUtil;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivityController extends FormControllerSupport {

    private UserProfileManager userProfileManager;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        long activityId = new Long(request.getParameter("activityId")).longValue();
        Activity activity = dao.getPopulatedActivity(activityId);
        return activity;

    }

    protected ModelAndView onSubmit(Object o) throws Exception {
        Activity activity = (Activity) o;
        dao.saveOrUpdate(activity);
        return new ModelAndView(new RedirectView("activity"), "activityId", Long.toString(activity.getId()));
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Map map = new HashMap();
        map.put("types", dao.getActivityTypes());
        map.put("priorities", dao.getActivityPriorities());
        map.put("phases", dao.getProjectPhases());
        Activity activity = (Activity) object;
        map.put("project",activity.getProject());
        List projectParticipants = dao.getProjectParticipants(activity.getProject().getId());
        map.put("profiles", ProjectWebUtil.getUserProfileDtos(userProfileManager, projectParticipants));
        return map;
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }


    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }
}
