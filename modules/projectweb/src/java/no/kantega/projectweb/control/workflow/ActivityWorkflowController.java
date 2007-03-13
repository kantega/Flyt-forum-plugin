package no.kantega.projectweb.control.workflow;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.osworkflow.BasicWorkflowFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import com.opensymphony.workflow.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 00:28:23
 * To change this template use File | Settings | File Templates.
 */
public class ActivityWorkflowController extends AbstractWorkflowController {

    public String getRedirectUrl(HttpServletRequest request) {
        return "activity?activityId=" + getActivityId(request);
    }

    public Map getArgs(HttpServletRequest request) {
        Map args = new HashMap();
        args.put("activityId", new Long(getActivityId(request)));
        return args;
    }

    private long getActivityId(HttpServletRequest request) {
        return  Long.parseLong(request.getParameter("activityId"));
    }
}
