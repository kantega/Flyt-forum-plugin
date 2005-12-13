package no.kantega.projectweb.control.activity;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.hibernate.criterion.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.activity.ActivityStatusManager;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ActivityListController implements Controller {

    private ProjectWebDao dao;
    private UserProfileManager userProfileManager;
    private ActivityStatusManager statusManager;


    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long projectId = Long.parseLong(request.getParameter("projectId"));
        Project project = dao.getProject(projectId);
        map.put("project", project);
        List activities = new ArrayList();
        DetachedCriteria criteria = createCriteria(request, project);
        List a = dao.getActivitiesInProject(criteria);
        Iterator i = a.iterator();
        Activity first = null;
        Activity last = null;
        while (i.hasNext()) {
            Activity activity = (Activity) i.next();
            ActivityDto dto = new ActivityDto();
            dto.setActivity(activity);
            dto.setAssigneeProfile(userProfileManager.getUserProfile(activity.getAssignee()));
            dto.setReporterProfile(userProfileManager.getUserProfile(activity.getReporter()));
            activities.add(dto);
            if(activity.getStartDate() != null && (first == null || activity.getStartDate().getTime() < first.getStartDate().getTime())) {
                first = activity;
            }
            if(activity.getEndDate() != null && (last == null || activity.getEndDate().getTime() > last.getEndDate().getTime())) {
                last = activity;
            }
        }
        map.put("firstactivity", first);
        map.put("lastactivity", last);

        map.putAll(referenceData(request, project));
        Map parameters = request.getParameterMap();
        map.putAll(parameters);
        map.put("activities", activities);
        return new ModelAndView("activitylist", map);
    }

    private DetachedCriteria createCriteria(HttpServletRequest request, Project project) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Activity.class);
        long projectId = Long.parseLong(request.getParameter("projectId"));
        String[] phases = request.getParameterValues("phases");
        String[] types  = request.getParameterValues("types");
        String[] statuses  = request.getParameterValues("statuses");
        String[] priorities  = request.getParameterValues("priorities");
        String[] assignees = request.getParameterValues("assignees");
        String[] reporters = request.getParameterValues("reporters");
        String text = request.getParameter("text");
        String order = request.getParameter("order");
        criteria.add(Property.forName("project").eq(project));

        if(text != null && text.length() > 2) {

            Disjunction tx = Restrictions.disjunction();
            tx.add(Property.forName("title").like(text, MatchMode.ANYWHERE));
            tx.add(Property.forName("description").like(text, MatchMode.ANYWHERE));
            criteria.add(tx);
        }

        // Phases
        if(phases != null && phases.length > 0) {

            boolean any = false;
            Long[] lphases = new Long[phases.length];
            for (int i = 0; i < phases.length; i++) {
                lphases[i] = new Long(phases[i]);
                if(lphases[i].longValue() == -1) {
                    any = true;
                }
            }
            if(!any) {
                DetachedCriteria sc = criteria.createCriteria("projectPhase");
                if(order != null && order.equals("phase")) {
                    sc.addOrder(Property.forName("name").asc());
                }
                sc.add(Property.forName("id").in(lphases));
            }

        }
        // Activitytype
        if(types != null && types.length > 0) {

            boolean any = false;
            Long[] ltypes = new Long[types.length];
            for (int i = 0; i < types.length; i++) {
                ltypes[i] = new Long(types[i]);
                if(ltypes[i].longValue() == -1) {
                    any = true;
                }
            }
            if(!any) {
                DetachedCriteria sc = criteria.createCriteria("type");
                if(order != null && order.equals("type")) {
                    sc.addOrder(Property.forName("name").asc());
                }
                sc.add(Property.forName("id").in(ltypes));
            }

        }
        // ActivityStatus
        if(statuses != null && statuses.length > 0) {

            boolean any = false;
            Long[] lStatuses = new Long[statuses.length];
            for (int i = 0; i < statuses.length; i++) {
                lStatuses[i] = new Long(statuses[i]);
                if(lStatuses[i].longValue() == -1) {
                    any = true;
                }
            }
            if(!any) {
                DetachedCriteria sc = criteria.createCriteria("status");
                if(order != null && order.equals("status")) {
                    sc.addOrder(Property.forName("name").asc());
                }
                sc.add(Property.forName("id").in(lStatuses));
            }

        }


        // Priorities
        if(priorities != null && priorities.length > 0) {
            boolean any = false;
            Long[] lPriorities = new Long[priorities.length];
            for (int i = 0; i < priorities.length; i++) {
                lPriorities[i] = new Long(priorities[i]);
                if(lPriorities[i].longValue() == -1) {
                    any = true;
                }

            }
            if(!any) {
                DetachedCriteria sc = criteria.createCriteria("priority");
                if(order.equals("priority")) {
                    sc.addOrder(Property.forName("name").asc());
                }
                sc.add(Property.forName("id").in(lPriorities));
            }
        }
        // Assignees
        if(assignees != null && assignees.length > 0) {
            boolean any = false;
            for (int i = 0; i < assignees.length; i++) {
                if(assignees[i].equals("-1")) {
                    any = true;
                }

            }
            if(!any) {
                if(order.equals("assignee")) {
                    criteria.addOrder(Property.forName("assignee").asc());
                }
                criteria.add(Property.forName("assignee").in(assignees));
            }
        }
        // Reporters
        if(reporters != null && reporters.length > 0) {
            boolean any = false;
            for (int i = 0; i < reporters.length; i++) {
                if(reporters[i].equals("-1")) {
                    any = true;
                }

            }
            if(!any) {
                if(order.equals("reporter")) {
                    criteria.addOrder(Property.forName("reporter").asc());
                }
                criteria.add(Property.forName("reporter").in(reporters));
            }
        }
        if(order != null &&
                (order.equals("title") ||
                 order.equals("estimatedLeftHours") ||
                 order.equals("usedHours"))) {
                criteria.addOrder(Order.asc(order));
        }

        return criteria;


    }

    private Map referenceData(HttpServletRequest request, Project project) {
        Map map = new HashMap();
        map.put("allstatuses", statusManager.getActivityStatuses(project));
        map.put("allparticipants", userProfileManager.getUserProfileDtos(dao.getProjectParticipants(project.getId())));
        map.put("allpriorities", dao.getActivityPriorities());
        map.put("alltypes", dao.getActivityTypes());
        map.put("allphases", dao.getProjectPhases());
        return map;
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setStatusManager(ActivityStatusManager statusManager) {
        this.statusManager = statusManager;
    }
}
