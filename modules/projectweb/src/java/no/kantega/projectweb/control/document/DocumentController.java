package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
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
public class DocumentController implements Controller{
    private ProjectWebDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long documentId = Long.parseLong(request.getParameter("documentId"));



        Document document = dao.getPopulatedDocument(documentId);
        if ("download".equals(request.getParameter("action"))){
            //hvis download er valgt sendes dokumentet til brukeren
            byte[] content = document.getContent();
            if (content == null) {
                // Dokumentinnhold er null
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            String filename = document.getFileName();
            String mimetype = document.getContentType();
            ServletOutputStream out = response.getOutputStream();

            response.setContentType(mimetype);
            response.addHeader("Content-Disposition", "attachment; filename=" + filename);
            try {
                out.write(content);
                out.flush();
                out.close();
            } catch (Exception e) {
                // Klient har avbrutt
            }
            return null;
        }
        else{
            Map map = new HashMap();
            map.put("document", document);
            map.put("activityId", request.getParameter("activityId"));
            map.put("project", document.getProject());

            return new ModelAndView("document", map);
        }
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}

