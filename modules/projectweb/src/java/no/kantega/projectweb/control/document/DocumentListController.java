package no.kantega.projectweb.control.document;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.hibernate.criterion.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Document;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class DocumentListController implements Controller {

    private ProjectWebDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long projectId = Long.parseLong(request.getParameter("projectId"));
        Project project = dao.getProject(projectId);
        map.put("project", project);

        DetachedCriteria criteria = createCriteria(request, project);
        List documents = dao.getDocumentsInProject(criteria);

        map.putAll(referenceData(request, project));
        Map parameters = request.getParameterMap();
        map.putAll(parameters);
        map.put("documents", documents);
        return new ModelAndView("documentlist", map);
    }

    private DetachedCriteria createCriteria(HttpServletRequest request, Project project) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Document.class);
        String text = request.getParameter("text");
        String[] categories  = request.getParameterValues("categories");
        String searchContent = request.getParameter("searchContent");

        String order = request.getParameter("order");
        criteria.add(Property.forName("project").eq(project));
        criteria.createAlias("documentContent", "dc");
        if (text != null && text.length() > 2) {
            Disjunction tx = Restrictions.disjunction();
            tx.add(Property.forName("title").like(text, MatchMode.ANYWHERE));
            tx.add(Property.forName("description").like(text, MatchMode.ANYWHERE));

            if (searchContent!=null){
                tx.add(Restrictions.like("dc.contentText", text, MatchMode.ANYWHERE));
            }
            criteria.add(tx);
        }

        if(categories != null && categories.length > 0) {

            boolean any = false;
            Long[] lcategories = new Long[categories.length];
            for (int i = 0; i < categories.length; i++) {
                lcategories[i] = new Long(categories[i]);
                if(lcategories[i].longValue() == -1) {
                    any = true;
                }
            }
            if(!any) {
                DetachedCriteria sc = criteria.createCriteria("category");
                if(order != null && order.equals("category")) {
                    sc.addOrder(Property.forName("name").asc());
                }
                sc.add(Property.forName("id").in(lcategories));
            }

        }

        if (order != null && order.equals("title")) {
            criteria.addOrder(Order.asc(order));
        }

        return criteria;


    }

    private Map referenceData(HttpServletRequest request, Project project) {
        Map map = new HashMap();
        map.put("allcategories", dao.getDocumentCategories());
        return map;
    }


    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
