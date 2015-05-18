package no.kantega.forum.tags;

import no.kantega.publishing.common.data.Content;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

/**
 *
 */
public class IfContentHasForumTag extends ConditionalTagSupport {

    protected boolean condition() throws JspTagException {
            Content content = (Content)pageContext.getRequest().getAttribute("aksess_this");
            if (content != null && content.getId() > 0) {
                long forumId = content.getForumId();
                if (forumId > 0) {
                    return true;
                }
        }
        return false;
    }
}
