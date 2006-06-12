package no.kantega.forum.control;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumCategory;
import no.kantega.forum.model.Post;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 08.des.2005
 * Time: 15:06:25
 * To change this template use File | Settings | File Templates.
 */
public class ListCategoriesController implements Controller {
    private ForumDao dao;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();

        List cats = dao.getForumCategories();

        for (int i = 0; i < cats.size(); i++) {
            ForumCategory category = (ForumCategory) cats.get(i);
            Iterator forums  = category.getForums().iterator();

            while (forums.hasNext()) {
                Forum forum = (Forum) forums.next();
                List lastPostsInForum = dao.getLastPostsInForum(forum.getId(), 1);
                if(lastPostsInForum.size() > 0) {
                    forum.setLastPost((Post) lastPostsInForum.get(0));
                }

            }
        }
        map.put("categories", cats);

        return new ModelAndView("listcategories", map);
    }

    public void setDao(ForumDao dao) {
        this.dao = dao;
    }
}
