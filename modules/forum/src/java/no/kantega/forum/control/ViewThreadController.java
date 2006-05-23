package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

import no.kantega.forum.model.ForumThread;
import no.kantega.forum.dao.ForumDao;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 13:28:18
 * To change this template use File | Settings | File Templates.
 */
public class ViewThreadController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long id = Long.parseLong(request.getParameter("threadId"));
        ForumThread t = dao.getThread(id);

        Map map = new HashMap();
        map.put("thread", t);
        map.put("posts", dao.getPostsInThread(t.getId()));
        return new ModelAndView("viewthread", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
