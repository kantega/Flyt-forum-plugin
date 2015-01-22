package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumCategory;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 10:13:02
 * To change this template use File | Settings | File Templates.
 */
public class ListForumsController implements Controller {
    private ForumDao dao;
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("id"));
        ForumCategory fc = dao.getPopulatedForumCategory(id);
        Map map = new HashMap();
        map.put("forumcategory", fc);

        return new ModelAndView("listforums", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
