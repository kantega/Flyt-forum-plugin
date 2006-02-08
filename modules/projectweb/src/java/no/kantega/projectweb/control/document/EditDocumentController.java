package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.user.UserProfileManager;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.osworkflow.BasicWorkflowFactory;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:56:55
 * To change this template use File | Settings | File Templates.
 */
public class EditDocumentController extends FormControllerSupport {
    private UserProfileManager userProfileManager;
    private BasicWorkflowFactory workflowFactory;
    private UserResolver userResolver;

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Document document = null;
        if (request.getParameter("documentId")!=null){
            long documentId = new Long(request.getParameter("documentId")).longValue();
            document = dao.getPopulatedDocument(documentId);
        }
        else{
            document = new Document();
            long projectId = Long.parseLong(request.getParameter("projectId"));
            document.setProject(dao.getProject(projectId));
        }
        document.setEditDate(new Date());
        return document;
    }

    protected ModelAndView onSubmit(Object o) throws Exception {
        Document document = (Document) o;
        dao.saveOrUpdate(document);
        return new ModelAndView(new RedirectView("document"), "documentId", Long.toString(document.getId()));
    }

    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Map map = new HashMap();
        map.put("statuses", dao.getActivityStatuses());
        return map;
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }


    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }

    public void setWorkflowFactory(BasicWorkflowFactory workflowFactory) {
        this.workflowFactory = workflowFactory;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }
}
