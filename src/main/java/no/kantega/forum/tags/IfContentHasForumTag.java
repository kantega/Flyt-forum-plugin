package no.kantega.forum.tags;

import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.spring.RootContext;
import no.kantega.forum.dao.ForumDao;

import javax.servlet.jsp.jstl.core.ConditionalTagSupport;
import javax.servlet.jsp.JspTagException;
import java.util.Map;

/**
 *
 */
public class IfContentHasForumTag extends ConditionalTagSupport {
    protected boolean condition() throws JspTagException {
        Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
        if(daos.size() > 0) {
            Content content = (Content)pageContext.getRequest().getAttribute("aksess_this");
            if (content != null && content.getId() > 0) {
                long forumId = content.getForumId();
                if (forumId > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
