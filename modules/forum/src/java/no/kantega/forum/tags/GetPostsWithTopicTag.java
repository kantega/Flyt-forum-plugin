package no.kantega.forum.tags;

import no.kantega.commons.log.Log;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.spring.RootContext;

import no.kantega.forum.dao.ForumDao;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.List;
import java.io.IOException;

public class GetPostsWithTopicTag extends SimpleTagSupport {

    private int topicMapId = -1;
    private List<String> topicIds = null;
    private int maxResults = -1;
    private String var = "post";

    public void doTag() throws JspException, IOException {
        Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            ForumDao dao = (ForumDao) daos.values().iterator().next();
            List l = dao.getPostsWithTopicIds(topicMapId, topicIds, maxResults);
            getJspContext().setAttribute(var, l);
        }

        topicMapId = -1;
        topicIds = null;
        maxResults = -1;

        var = "post";

    }

    public void setTopicMapId(int topicMapId) {
        this.topicMapId = topicMapId;
    }

    public void setTopicIds(List<String> topicIds) {
        this.topicIds = topicIds;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public void setVar(String var) {
        this.var = var;
    }
}