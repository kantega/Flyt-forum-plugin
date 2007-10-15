package no.kantega.exchange.tags;

import org.apache.log4j.Logger;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import com.intrinsyc.cdo.*;
import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.SecuritySession;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.exchange.util.ExchangeSession;
import no.kantega.exchange.model.MailItem;
import no.kantega.commons.exception.SystemException;
import no.kantega.commons.log.Log;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:16:04 PM
 */
public class MailTag extends LoopTagSupport {

    private static final String SOURCE = "exchange.MailTag";
    private Iterator i;
    private String userid;
    private String mailbox = "";
    private String unread = "";
    //private String fromdate = ""; // format: "1/9/07"
    //private String todate = "";   // format: "2/9/07"
    private List items = null;

    private boolean unreadonly = false;
    //private boolean filter = false;

    protected Object next() throws JspTagException {
        if (i != null) {
            return i.next();
        }
        return null;
    }

    protected boolean hasNext() throws JspTagException {
        if (i == null) {
            return false;
        }
        return i.hasNext();
    }

    protected void prepare() throws JspTagException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        Session cdosession = null;
        boolean skip = false;
        try {

            boolean notdone = true;
            items = new ArrayList();
            User user = null;
            boolean validuser = false;
            Integer folderType = null;
            Folder box = null;

            SecuritySession session = SecuritySession.getInstance(request);
            if (userid != null) {
                userid = ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                try {
                    user = realm.lookupUser(userid);
                    validuser = true;
                } catch (SystemException e) {
                }
            } else {
                user = session.getUser();
                userid = user.getId().substring(user.getId().indexOf(":") + 1);
                validuser = true;
            }

            if (validuser) {

                // Start connection to cdo & exchange server
                ExchangeSession Xsession = new ExchangeSession();
                cdosession = Xsession.getInstance(userid, request);


                if (mailbox != null && !"".equals(mailbox)) {
                    // TODO: Find out order of inputs and how to get folder by names
                    box = new FolderProxy(cdosession.getFolder(mailbox, ""));
                } else {
                    folderType = new Integer(CdoDefaultFolderTypes.CdoDefaultFolderInbox);
                    box = new FolderProxy(cdosession.getDefaultFolder(folderType));
                }

                // get the message collection from the inbox
                Messages mesColl = new MessagesProxy(box.getMessages());

                int count = ((Integer) mesColl.getCount()).intValue();
                for (int k = 1; k <= count; k++) {

                    MailItem ei = new MailItem();
                    Message message = new MessageProxy(mesColl.getItem(new Integer(k)));

                    if (unreadonly) {
                        if (!message.getUnread().toString().equalsIgnoreCase("true")) {
                            skip = true;
                        }
                    }
                    if (!skip) {
                        AddressEntry sender = new AddressEntryProxy(message.getSender());
                        ei.setSender(sender.getName().toString());
                        ei.setSubject(message.getSubject().toString());
                        ei.setReceived((Date) message.getTimeReceived());        // Might not work
                        ei.setSensitivity(message.getSensitivity().toString());
                        ei.setImportance(message.getImportance().toString());
                        ei.setBody(message.getText().toString());
                        ei.setRecipients(message.getRecipients().toString());
                        items.add(ei);
                        i = items.iterator();
                    }
                    skip = false;
                }
            }
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        }
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public void setUnread(String unread) {
        if (unread.equalsIgnoreCase("true")) {
            unreadonly = true;
        }
        this.unread = unread;
    }

}
