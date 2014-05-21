package no.kantega.forum.control.ajax;


import no.kantega.commons.util.StringHelper;
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

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {


        String forumId = request.getParameter("forumId");
        int[] forumIds = null;
        if (forumId != null) {
            forumIds =  StringHelper.getInts(forumId, ",");
        }

        long forumCategoryId = ServletRequestUtils.getLongParameter(request, "forumCategoryId", -1);

        long timeStamp = ServletRequestUtils.getRequiredLongParameter(request, "timeStamp");
        String username = ServletRequestUtils.getRequiredStringParameter(request, "username");

        Timestamp lastRefresh = new Timestamp(timeStamp);
        int numberOfNewThreads;

        if (forumIds != null && forumIds.length > 0) {
            numberOfNewThreads = forumDao.getNumberOfThreadsAfterDateInForumsNotByUser(forumIds, lastRefresh, username);
        } else {
            numberOfNewThreads = forumDao.getNumberOfThreadsAfterDateInForumCategoryNotByUser(forumCategoryId, lastRefresh, username);
        }

        Map<String, Object> map = new HashMap<>();
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
