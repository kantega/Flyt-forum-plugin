package no.kantega.forum.tags.wall;

import no.kantega.commons.exception.ConfigurationException;
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
    private int charsPerLine = -1;



    public void doTag() throws JspException, IOException {
        PageContext pageContext = ((PageContext)getJspContext());
        JspWriter out = pageContext.getOut();
        StringBuilder formattedPostBody = new StringBuilder();

        // Dont want to split up html anchor in preview text. So must first check if a link is present in the preview text
        String linkStartTag = "<a ";
        String linkEndTag = "</a>";
        charsPerLine = Aksess.getConfiguration().getInt("forum.post.charsperline", -1);

        int indexOfFirstLink = postbody.indexOf(linkStartTag);
        // Link is in the preview part and the post is large enough for a preview to be made
        if( indexOfFirstLink < (charsInBodyPreview - charsPerLine) && postbody.length() > charsInBodyPreview) {
            int indexOfFirstLinkEnd = postbody.indexOf(linkEndTag);
            // End of link is on "the other side" of the preview meaning it will split the link
            if (indexOfFirstLinkEnd > (charsInBodyPreview - charsPerLine)) {
                // Therefore the preview is increased by the length of the link
                charsInBodyPreview = charsInBodyPreview + ( (indexOfFirstLinkEnd + linkEndTag.length()) - indexOfFirstLink);
            }
            // No matter where the link is in the preview, its length in chars will be about double of what is shown
            // Preview is therefore increased
            charsInBodyPreview += (indexOfFirstLinkEnd - indexOfFirstLink)/2;
        }
        // Creating a preview if necessary, if the post does not fit within the limit, one line is removed in order to
        // show "show more" tag so it does not use more space
        formattedPostBody.append(postbody);
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
    public void setCharsperline(int charsPerLine) {
        this.charsPerLine = charsPerLine;
    }
}
