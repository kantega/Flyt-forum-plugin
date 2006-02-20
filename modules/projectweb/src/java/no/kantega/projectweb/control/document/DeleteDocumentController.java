package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.viewmodel.WorkflowHistoryLine;
import no.kantega.projectweb.user.UserResolver;
import no.kantega.projectweb.permission.PermissionManager;
import no.kantega.projectweb.permission.Permissions;
import no.kantega.osworkflow.BasicWorkflowFactory;
import no.kantega.publishing.common.data.Attachment;
import no.kantega.publishing.common.util.InputStreamHandler;
import no.kantega.commons.log.Log;
import no.kantega.commons.media.MimeTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.loader.ActionDescriptor;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class DeleteDocumentController implements Controller{
    private ProjectWebDao dao;
    private UserResolver userResolver;
    private PermissionManager permissionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(request.getMethod().equals("POST")) {
            long documentId = Long.parseLong(request.getParameter("documentId"));
            Document document = dao.getPopulatedDocument(documentId);
            String user = userResolver.resolveUser(request).getUsername();
            if (permissionManager.hasPermission(user, Permissions.DELETE_DOCUMENT, document.getProject())){
                dao.deleteDocument(documentId);

                String activityId = request.getParameter("activityId");
                if (activityId!=null && !activityId.equals("")){
                    return new ModelAndView(new RedirectView("activity"), "activityId", activityId);
                }
                else{
                    return new ModelAndView(new RedirectView("documentlist"), "projectId", Long.toString(document.getProject().getId()));
                }
            }
            else{
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

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

    public void setPermissionManager(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }
}

