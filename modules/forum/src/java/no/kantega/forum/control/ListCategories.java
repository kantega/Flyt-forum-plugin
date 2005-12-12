package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 08.des.2005
 * Time: 15:06:25
 * To change this template use File | Settings | File Templates.
 */
public class ListCategories implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        
        List categories = dao.getForumCategories();
        map.put("categories", categories);

        return new ModelAndView("categorylist", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}