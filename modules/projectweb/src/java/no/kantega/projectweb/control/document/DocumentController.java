package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Document;

import java.util.HashMap;
import java.util.Map;

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



        if ("download".equals(request.getParameter("action"))){
            Document document = dao.getPopulatedDocument(documentId, true);

            //hvis download er valgt sendes dokumentet til brukeren
            byte[] content = document.getDocumentContent().getContent();
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
            Document document = dao.getPopulatedDocument(documentId, true);

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

