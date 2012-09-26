package no.kantega.exchange.tags;

import com.intrinsyc.cdo.*;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;

import no.kantega.publishing.security.data.User;
import no.kantega.exchange.util.CdoSessionWrapper;
import no.kantega.exchange.util.ExchangeManager;
import no.kantega.commons.log.Log;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:08:03 PM
 */
public class GetUnreadTag extends TagSupport {

    private static final String SOURCE = "exchange.GetUnreadTag";

    private String userid;


    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        CdoSessionWrapper session = null;
        JspWriter out;
        try {
            session = ExchangeManager.getSession(userid, pageContext);

            // retrieve the inbox folder
            Folder inbox = new FolderProxy(session.getDefaultFolder(new Integer(CdoDefaultFolderTypes.CdoDefaultFolderInbox)));

            // get the message collection from the inbox
            Messages messages = new MessagesProxy(inbox.getMessages());

            MessageFilter f = new MessageFilterProxy(messages.getFilter());
            f.setUnread(Boolean.TRUE);
            messages.setFilter(f);

            // loop through the message collection and count unreads
            int count = ((Integer)messages.getCount()).intValue();

            out = pageContext.getOut();
            out.write(Integer.toString(count));
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        userid = null;
        return EVAL_PAGE;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
