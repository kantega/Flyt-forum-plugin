package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;

public class ViewCategoryController  implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long id = Long.parseLong(request.getParameter("categoryId"));

        ForumCategory fc = dao.getPopulatedForumCategory(id);
        map.put("forumcategory", fc);

        return new ModelAndView("viewcategory", map);
    }


    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
