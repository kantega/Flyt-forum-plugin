package no.kantega.projectweb.control.workflow;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.modules.user.UserResolver;
import no.kantega.osworkflow.BasicWorkflowFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import com.opensymphony.workflow.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 00:28:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractWorkflowController implements Controller {

    private ProjectWebDao dao;

    private BasicWorkflowFactory workflowFactory;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long workflowId = Long.parseLong(request.getParameter("workflowId"));
        int action = Integer.parseInt(request.getParameter("action"));
        Workflow workflow = workflowFactory.createBasicWorkflow(userResolver.resolveUser(request).getUsername());

        workflow.doAction(workflowId, action, getArgs(request));
        response.sendRedirect(getRedirectUrl(request));
        return null;
    }

    public abstract String getRedirectUrl(HttpServletRequest request);


    public abstract Map getArgs(HttpServletRequest request);


    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setWorkflowFactory(BasicWorkflowFactory workflowFactory) {
        this.workflowFactory = workflowFactory;
    }

    public ProjectWebDao getDao() {
        return dao;
    }

    public BasicWorkflowFactory getWorkflowFactory() {
        return workflowFactory;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
