package no.kantega.forum.tags;

import no.kantega.forum.dao.ForumDao;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.data.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class NewPostLinkTag extends BodyTagSupport {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private String cssStyle = null;
    private String cssClass = null;
    private String onClick  = null;
    private String accessKey = null;
    private String tabIndex  = null;
    private String target = null;

    private ForumDao dao;

    public void setStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public void setCssclass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setAccesskey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setTabindex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int doStartTag()  throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspException {
        try {
            String body = bodyContent.getString();
            JspWriter out = bodyContent.getEnclosingWriter();

            String url = null;

            Content content = (Content)pageContext.getRequest().getAttribute("aksess_this");
            if (content != null && content.getId() > 0) {
                long threadId = dao.getThreadAboutContent(content.getId());
                long forumId = content.getForumId();
                if (threadId > 0) {
                    url = Aksess.getContextPath() + "/forum/editpost?threadId=" + threadId;
                } else if (forumId > 0) {
                    url = Aksess.getContextPath() + "/forum/startthread?forumId=" + forumId + "&contentId=" + content.getId();
                }
            }

            if (url != null) {
                out.print("<a href=\"" + url + "\"");
                if (onClick != null) {
                    out.print(" onClick=\"" + onClick + "\"");
                }
                if (cssStyle != null) {
                    out.print(" style=\"" + cssStyle + "\"");
                }
                if (cssClass != null) {
                    out.print(" class=\"" + cssClass + "\"");
                }
                if (accessKey != null) {
                    out.print(" accesskey=\"" + accessKey + "\"");
                }

                if (target != null) {
                    out.print(" target=\"" + target + "\"");
                }

                if (tabIndex != null) {
                    out.print(" tabindex=\"" + tabIndex + "\"");
                }
                out.print(">");

                if(body != null) {
                    out.print(body);
                }

                out.print("</a>");
            }

        } catch (Exception e) {
            log.error("Error printing bew post", e);
            throw new JspTagException(e);
        } finally {
            bodyContent.clearBody();
        }

        return SKIP_BODY;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        dao = context.getBean(ForumDao.class);
    }
}
