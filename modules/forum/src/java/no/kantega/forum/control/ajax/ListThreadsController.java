package no.kantega.forum.control.ajax;


import no.kantega.commons.client.util.RequestParameters;
import no.kantega.forum.dao.ForumDao;
import no.kantega.forum.model.ForumThread;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListThreadsController implements Controller {


    private ForumDao forumDao;
    private int defaultNumberOfPostsToShow = 20;

    public ListThreadsController(ForumDao forumDao) {
        this.forumDao = forumDao;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        Map model = new HashMap();

        RequestParameters param = new RequestParameters(request);
        int forumId = param.getInt("forumId");
        int offset = param.getInt("offset");
        int numberOfPostsToShow = param.getInt("numberOfPostsToShow");

        if (numberOfPostsToShow == -1) {
            numberOfPostsToShow = defaultNumberOfPostsToShow;
        }
        if (offset == -1) {
            offset = 0;
        }
        List<ForumThread> threads = forumDao.getThreadsInForum(forumId, offset, numberOfPostsToShow);

        model.put("threads", threads);
        return new ModelAndView("threads", model);
    }
}
