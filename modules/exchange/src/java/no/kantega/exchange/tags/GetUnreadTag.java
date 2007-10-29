package no.kantega.exchange.tags;

import com.intrinsyc.cdo.*;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;

import no.kantega.publishing.security.data.User;
import no.kantega.exchange.util.ExchangeSession;
import no.kantega.commons.log.Log;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:08:03 PM
 */
public class GetUnreadTag extends TagSupport {
    private static final String SOURCE = "exchange.MailUnreadTag";

    private String userid;

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        Session cdosession = null;
        JspWriter out;
        try {
            int unreads = 0;
            User user = null;
            boolean validuser = false;

                // Start connection to cdo & exchange server
                ExchangeSession Xsession = new ExchangeSession();
                cdosession = Xsession.getInstance(userid, request, pageContext);

                // retrieve the inbox folder
                Integer folderType = new Integer(CdoDefaultFolderTypes.CdoDefaultFolderInbox);
                Folder inbox = new FolderProxy(cdosession.getDefaultFolder(folderType));

                // get the message collection from the inbox
                Messages messages = new MessagesProxy(inbox.getMessages());

                // loop through the message collection and count unreads
                int count = ((Integer) messages.getCount()).intValue();
                for (int i = 1; i <= count; i++) {
                    Message message = new MessageProxy(messages.getItem(new Integer(i)));

                    if (message.getUnread().toString().equalsIgnoreCase("true")) {
                        unreads++;
                    }
                }

                out = pageContext.getOut();
                out.write(new Integer(unreads).toString());
          //  }

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        } finally {
            try {
                //cdosession.logoff();
            } catch (Exception e) {}
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
