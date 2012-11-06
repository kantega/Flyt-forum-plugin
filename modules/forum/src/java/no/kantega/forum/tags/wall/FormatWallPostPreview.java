package no.kantega.forum.tags.wall;

import no.kantega.commons.exception.ConfigurationException;
import no.kantega.commons.log.Log;
import no.kantega.commons.util.LocaleLabels;
import no.kantega.publishing.common.Aksess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class FormatWallPostPreview extends SimpleTagSupport {
    private static final String SOURCE = "FormatWallPostPreview";

    private String postbody;
    private int charsInBodyPreview = 200;
    private int charsPerLine = 1;

    public void doTag() throws JspException, IOException {
        PageContext pageContext = ((PageContext)getJspContext());
        JspWriter out = pageContext.getOut();
        StringBuffer formattedPostBody = new StringBuffer();

        // Dont want to split up html anchor in preview text. So must first check if a link is present in the preview text
        String linkStartTag = "<a";
        String linkEndTag = "</a>";
        try {
            charsPerLine = Aksess.getConfiguration().getInt("forum.post.charsperline", 1);
        } catch (ConfigurationException e) {
            Log.debug(SOURCE, "Fetching configuration");
        }
        int indexOfFirstLink = postbody.indexOf(linkStartTag);
        if( indexOfFirstLink < (charsInBodyPreview - charsPerLine) ) {
            int indexOfFirstLinkEnd = postbody.indexOf(linkEndTag);
            if (indexOfFirstLinkEnd > 0) {
                charsInBodyPreview = charsInBodyPreview + ( (indexOfFirstLinkEnd + linkEndTag.length()) - indexOfFirstLink);
            }
        }
        // Creating a preview if neccessary, if the post does not fit within the limit, one line is removed in order to
        // show "show more" tag so it will not use more space
        if (postbody.length() > charsInBodyPreview) {
            String linkText = LocaleLabels.getLabel("forum.wall.post.previewlink","forum", Aksess.getDefaultAdminLocale());
            formattedPostBody.append(postbody.substring(0, charsInBodyPreview - charsPerLine));
            formattedPostBody.append("<span class=\"oa-forum-post-preview-more-indicator\">...</span>");
            formattedPostBody.append("<br><a href=\"#\" class=\"oa-forum-post-preview-show-full-body-link\">" + linkText + "</a><br>");
            formattedPostBody.append("<span class=\"oa-forum-post-preview-hidden-post-body\">" + postbody.substring(charsInBodyPreview - charsPerLine) + "</span>");
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
