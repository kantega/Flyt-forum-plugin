package no.kantega.projectweb.control.document;

import no.kantega.modules.user.UserProfileManager;
import no.kantega.modules.user.UserResolver;
import no.kantega.osworkflow.BasicWorkflowFactory;
import no.kantega.projectweb.control.FormControllerSupport;
import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.model.DocumentCategory;
import no.kantega.projectweb.model.DocumentContent;
import no.kantega.publishing.search.extraction.TextExtractor;
import no.kantega.publishing.search.extraction.TextExtractorSelector;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            document.setTitle(request.getParameter("doctitle"));
            document.setDescription(request.getParameter("docDescription"));
            String catId = request.getParameter("docCat");
            if( catId != null ){
                DocumentCategory docCat = new DocumentCategory();
                docCat.setId(Long.parseLong(catId));
                document.setCategory(docCat);
            }
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

            String newDocument = httpServletRequest.getParameter("newdocument");
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
                        document.getDocumentContent().setContentText(extractor.extractText(new ByteArrayInputStream(document.getDocumentContent().getContent()), document.getFileName()));
                    }
                } catch (Throwable e) {
                    log.error("Error extracting text from document " + document.getFileName());
                }
            }
            if( file.getSize()<1 && "true".equals(newDocument)){
                // Nytt dokument og blank innhold ikke lov
                // Dette kan sikkert gjøres mer elegant med en eller annen Spring validator.
                Map map = new HashMap();
                map.put("document",document);
                map.put("errormessage","Kan ikke laste opp tomt dokument");
                map.put("doctitle", document.getTitle());
                map.put("docCat", new Long(document.getCategory().getId()));
                map.put("docDescription", document.getDescription());
                map.put("projectId",new Long(document.getProject().getId()));
                return new ModelAndView( new RedirectView("editdocument"),map);
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

        dao.saveDocumentWithActivity(activityId != null && !"".equals(activityId) ? Long.parseLong(activityId) : 0, document);


        String attachedActivityId = httpServletRequest.getParameter("attachedActivityId");
        if(attachedActivityId != null && !"".equals(attachedActivityId)) {
            return new ModelAndView(new RedirectView("activity"), "activityId", attachedActivityId);
        } else {
            return new ModelAndView(new RedirectView("documentlist"), "projectId", Long.toString(document.getProject().getId()));
        }

    }


    protected Map referenceData(HttpServletRequest httpServletRequest, Object object, Errors errors) throws Exception {
        Document document = (Document) object;
        Map map = new HashMap();
        map.put("categories", dao.getDocumentCategories());
        map.put("project", document.getProject());
        map.put("activityId", httpServletRequest.getParameter("activityId"));
        map.put("attachedActivityId", httpServletRequest.getParameter("attachedActivityId"));
        DetachedCriteria c = DetachedCriteria.forClass(Activity.class).add(Property.forName("project").eq(document.getProject()));

        Set activities = document.getActivities();
        map.put("selectedActivity", activities == null || activities.size() == 0 ? null : activities.iterator().next());
        map.put("allActivities", dao.getActivitiesInProject(c));
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
