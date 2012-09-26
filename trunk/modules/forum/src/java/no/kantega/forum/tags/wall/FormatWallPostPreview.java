package no.kantega.forum.tags.wall;

import no.kantega.commons.util.LocaleLabels;
import no.kantega.publishing.common.Aksess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class FormatWallPostPreview extends SimpleTagSupport {
    
    private String postbody;
    private int charsInBodyPreview = 200;

    public void doTag() throws JspException, IOException {
        PageContext pageContext = ((PageContext)getJspContext());
        JspWriter out = pageContext.getOut();
        StringBuffer formattedPostBody = new StringBuffer();

        // Dont want to split up html anchor in preview text. So must first check if a link is present in the preview text
        String linkStartTag = "<a";
        String linkEndTag = "</a>";
        int indexOfFirstLink = postbody.indexOf(linkStartTag);
        if( indexOfFirstLink < (charsInBodyPreview + 1) ) {
            int indexOfFirstLinkEnd = postbody.indexOf(linkEndTag);
            if (indexOfFirstLinkEnd > 0) {
                charsInBodyPreview = charsInBodyPreview + ( (indexOfFirstLinkEnd + linkEndTag.length()) - indexOfFirstLink);
            }
        }
        // Creating a preview if neccessary
        if (postbody.length() > charsInBodyPreview) {
            String linkText = LocaleLabels.getLabel("forum.wall.post.previewlink","forum", Aksess.getDefaultAdminLocale());
            formattedPostBody.append(postbody.substring(0, charsInBodyPreview +1));
            formattedPostBody.append("<span class=\"oa-forum-post-preview-more-indicator\">...</span>");
            formattedPostBody.append("<br><a href=\"#\" class=\"oa-forum-post-preview-show-full-body-link\">" + linkText + "</a><br>");
            formattedPostBody.append("<span class=\"oa-forum-post-preview-hidden-post-body\">" + postbody.substring(charsInBodyPreview + 1) + "</span>");
        } else {
            formattedPostBody.append(postbody);
        }
        out.write(formattedPostBody.toString());

        postbody = "";
        charsInBodyPreview = 200;
    }

    public void setPostbody(String postbody) {
        this.postbody = postbody;
    }

    public void setCharsinbodypreview(int charsInBodyPreview) {
        this.charsInBodyPreview = charsInBodyPreview;
    }
}
