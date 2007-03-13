package no.kantega.forum.tags;

import no.kantega.commons.log.Log;
import no.kantega.publishing.common.Aksess;
import no.kantega.publishing.common.data.Content;
import no.kantega.publishing.spring.RootContext;

import no.kantega.forum.dao.ForumDao;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * User: ANDSKA
 * Date: 11.jul.2006
 * Time: 13:12:30
 * Copyright: Kantega
 */
public class NewPostLinkTag extends BodyTagSupport {
    private static final String SOURCE = "aksess.forum.NewPostLinkTag";

    private String cssStyle = null;
    private String cssClass = null;
    private String onClick  = null;
    private String accessKey = null;
    private String tabIndex  = null;
    private String target = null;

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
        return EVAL_BODY_TAG;
    }

    public int doAfterBody() throws JspException {
        try {
            String body = bodyContent.getString();
            JspWriter out = bodyContent.getEnclosingWriter();

            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

            String url = null;

            Map daos = RootContext.getInstance().getBeansOfType(ForumDao.class);
            if(daos.size() > 0) {
                ForumDao dao = (ForumDao) daos.values().iterator().next();
                Content content = (Content)request.getAttribute("aksess_this");
                if (content != null && content.getId() > 0) {
                    long threadId = dao.getThreadAboutContent(content);
                    long forumId = content.getForumId();
                    if (threadId > 0) {
                        url = Aksess.getContextPath() + "/forum/editpost?threadId=" + threadId;
                    } else if (forumId > 0) {
                        url = Aksess.getContextPath() + "/forum/startthread?forumId=" + forumId + "&contentId=" + content.getId();
                    }
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

                out.print("</a>\n");
            }

        } catch (Exception e) {
            System.err.println(e);
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        } finally {
            bodyContent.clearBody();
        }

        return SKIP_BODY;
     }


}
