package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.Errors;
import org.springframework.validation.BindException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.io.ByteArrayInputStream;

import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.model.DocumentContent;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.osworkflow.BasicWorkflowFactory;
import no.kantega.publishing.search.extraction.TextExtractorSelector;
import no.kantega.publishing.search.extraction.TextExtractor;

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
    private TextExtractorSelector textExtractorSelector;
    private Logger log = Logger.getLogger(getClass());


    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Document document = null;
        if (request.getParameter("documentId")!=null){
            long documentId = new Long(request.getParameter("documentId")).longValue();
            document = dao.getPopulatedDocument(documentId, true);
        }
        else{
            document = new Document();
            DocumentContent content = new DocumentContent();
            document.setDocumentContent(content);
            long projectId = Long.parseLong(request.getParameter("projectId"));
            document.setProject(dao.getProject(projectId));
        }

        document.setEditDate(new Date());
        return document;
    }

    protected ModelAndView onSubmit(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object, BindException bindException) throws Exception {
        Document document = (Document) object;

        if(httpServletRequest instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest request = (MultipartHttpServletRequest) httpServletRequest;
            MultipartFile file  = request.getFile("contentFile");


            //slik at dersom man ikke laster opp noe så forkastes ikke det som er
            if (file.getSize()>0){
                document.setFileName(file.getOriginalFilename());
                document.setContentType(file.getContentType());



                String user = userResolver.resolveUser(request).getUsername();
                document.setUploader(user);
                document.getDocumentContent().setContent(file.getBytes());

                try {
                    TextExtractor extractor = textExtractorSelector.select(document.getFileName());
                    if(extractor != null) {
                        document.getDocumentContent().setContentText(extractor.extractText(new ByteArrayInputStream(document.getDocumentContent().getContent())));
                    }
                } catch (Throwable e) {
                    log.error("Error extracting text from document " + document.getFileName());
                }
            }
        }
        if (document.getTitle()==null || "".equals(document.getTitle())){
            String fileName = document.getFileName();
            if (fileName.lastIndexOf(".")>0){
                document.setTitle(fileName.substring(0,fileName.lastIndexOf(".")));
            }
            else document.setTitle(fileName);
        }

        //hvis dokumentet legges til en aktivitet
        String activityId = httpServletRequest.getParameter("activityId");
        if (activityId!=null){

            dao.saveDocumentWithActivity(Long.parseLong(activityId), document);
        }
        else{
            dao.saveOrUpdate(document);
        }

        return new ModelAndView(new RedirectView("document"), "documentId", Long.toString(document.getId()));
    }


    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Document document = (Document) object;
        Map map = new HashMap();
        map.put("categories", dao.getDocumentCategories());
        map.put("project", document.getProject());
        map.put("activityId", httpServletRequest.getParameter("activityId"));
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

    public void setTextExtractorSelector(TextExtractorSelector textExtractorSelector) {
        this.textExtractorSelector = textExtractorSelector;
    }
}
