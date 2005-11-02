package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.osworkflow.BasicWorkflowFactory;
import com.opensymphony.workflow.Workflow;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:56:55
 * To change this template use File | Settings | File Templates.
 */
public class AddDocumentController implements Controller {
    private ProjectWebDao dao;
    private BasicWorkflowFactory workflowFactory;
    private UserResolver userResolver;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        String title = request.getParameter("title");
        long projectId = Long.parseLong(request.getParameter("projectId"));

        Document doc = new Document();
        doc.setTitle(title);
        doc.setProject(dao.getProject(projectId));

        Map args = new HashMap();
        args.put("document", doc);

        Workflow workflow = workflowFactory.createBasicWorkflow(userResolver.resolveUser(request).getUsername());

        workflow.initialize("document", 1, args);

        return new ModelAndView(new RedirectView("documentlist"), "projectId", Long.toString(projectId));
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
