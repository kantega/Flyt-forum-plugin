package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 10:01:58
 * To change this template use File | Settings | File Templates.
 */
public class ViewForumController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long id = Long.parseLong(request.getParameter("forumId"));
        Forum f = dao.getPopulatedForum(id);
        map.put("forum", f);

        return new ModelAndView("viewforum", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
