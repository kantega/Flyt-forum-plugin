package no.kantega.projectweb.control.activity;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.ActivityComment;
import no.kantega.modules.user.UserResolver;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:53:29
 * To change this template use File | Settings | File Templates.
 */
public class AddCommentController implements Controller{


    private ProjectWebDao dao;

    private UserResolver userResolver;


    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = new HashMap();
        long activityId = Long.parseLong(request.getParameter("activityId"));

        String text = request.getParameter("text");
        String user = userResolver.resolveUser(request).getUsername();
        ActivityComment comment = new ActivityComment();
        comment.setUser(user);
        comment.setDate(new Date());
        comment.setText(text);
        if(request.getMethod().equals("POST")) {
            dao.addActivityComment(comment, activityId);
        }
        map.put("activityId", new Long(activityId));
        return new ModelAndView(new RedirectView("activity"), map);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setUserResolver(UserResolver userResolver) {
        this.userResolver = userResolver;
    }

}

