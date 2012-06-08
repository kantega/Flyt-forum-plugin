package no.kantega.forum.control.ajax;


import no.kantega.forum.dao.ForumDao;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class NumberOfNewThreadsInForumController implements Controller {

    private ForumDao forumDao;
    private View jsonView;

    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {


        long forumId = ServletRequestUtils.getLongParameter(httpServletRequest, "forumId", -1);
        long forumCategoryId = ServletRequestUtils.getLongParameter(httpServletRequest, "forumCategoryId", -1);

        long timeStamp = ServletRequestUtils.getRequiredLongParameter(httpServletRequest, "timeStamp");
        String username = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "username");

        Timestamp lastRefresh = new Timestamp(timeStamp);
        int numberOfNewThreads;

        if (forumId != -1) {
            numberOfNewThreads = forumDao.getNumberOfThreadsAfterDateInForumNotByUser(forumId, lastRefresh, username);
        } else {
            numberOfNewThreads = forumDao.getNumberOfThreadsAfterDateInForumCategoryNotByUser(forumCategoryId, lastRefresh, username);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("numberOfNewThreads", numberOfNewThreads);

        return new ModelAndView(jsonView, map);
    }
    public void setForumDao(ForumDao forumDao) {
        this.forumDao = forumDao;
    }

    public void setJsonView(View jsonView) {
        this.jsonView = jsonView;
    }
}
