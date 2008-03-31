package no.kantega.projectweb.control.activity;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.Step;
import no.kantega.modules.user.UserResolver;
import no.kantega.osworkflow.BasicWorkflowFactory;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.viewmodel.WorkflowHistoryLine;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class ActivityController implements Controller{


    private ProjectWebDao dao;

    private BasicWorkflowFactory workflowFactory;

    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long activityId = Long.parseLong(request.getParameter("activityId"));
        Activity activity = dao.getPopulatedActivity(activityId);
        map.put("activity", activity);
        map.put("project", activity.getProject());
        Workflow workflow = (Workflow) workflowFactory.createBasicWorkflow(userResolver.resolveUser(request).getUsername());

        final WorkflowDescriptor wd = workflow.getWorkflowDescriptor(workflow.getWorkflowName(activity.getWorkflowId()));

        int[] actionIds = workflow.getAvailableActions(activity.getWorkflowId(), null);

        List actions = new ArrayList();
        for (int i = 0; i < actionIds.length; i++) {
            actions.add(wd.getAction(actionIds[i]));
        }
        map.put("actions", actions);

        List historySteps = workflow.getHistorySteps(activity.getWorkflowId());

        List history = new ArrayList();
        for (int i = 0; i < historySteps.size(); i++) {
            final Step step = (Step) historySteps.get(i);
            WorkflowHistoryLine line = new WorkflowHistoryLine();
            line.setStep(step);
            line.setActionDescriptor(wd.getAction(step.getActionId()));
            history.add(line);
        }
        map.put("history", history);

        List currentSteps = workflow.getCurrentSteps(activity.getWorkflowId());

        List current = new ArrayList();
        for (int i = 0; i < currentSteps.size(); i++) {
            final Step step = (Step) currentSteps.get(i);
            WorkflowHistoryLine line = new WorkflowHistoryLine();
            line.setStep(step);
            line.setActionDescriptor(wd.getAction(step.getActionId()));
            current.add(line);
        }
        map.put("current", current);

        final String order = request.getParameter("order");

        List documents = new ArrayList(activity.getDocuments());

        /* eirbjo: Kind of a hack, we should have done this using criteria api, but I don't have
         *         time to figure that out.
         */
        Collections.sort(documents, new Comparator() {
            public int compare(Object o, Object o1) {
                Document d = (Document) o;
                Document d1 = (Document) o1;
                if("editDate".equals(order)) {
                    return d.getEditDate().compareTo(d1.getEditDate());
                } else {
                    return d.getTitle().toLowerCase().compareTo(d1.getTitle().toLowerCase());
                }
            }
        });
        map.put("documents", documents);

        return new ModelAndView("activity", map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }


    public void setWorkflowFactory(BasicWorkflowFactory workflowFactory) {
        this.workflowFactory = workflowFactory;
    }


    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}

