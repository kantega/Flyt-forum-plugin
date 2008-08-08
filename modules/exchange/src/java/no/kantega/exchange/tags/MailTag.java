package no.kantega.exchange.tags;

import com.intrinsyc.cdo.*;
import no.kantega.commons.log.Log;
import no.kantega.exchange.model.MailItem;
import no.kantega.exchange.model.MailTagModel;
import no.kantega.exchange.util.CdoSessionWrapper;
import no.kantega.exchange.util.ExchangeManager;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.io.IOException;
import java.util.*;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:16:04 PM
 */
public class MailTag extends LoopTagSupport {

    private static final String SOURCE = MailTag.class.toString();
    private static final int FIELD_ID_RELATIVE_URL = 1728512031;

    private Iterator mailIterator;
    private String userid;
    private String mailbox = "";
    private boolean unreadonly = false;
    private int max = -1;
    private List fieldList = new ArrayList();


    protected Object next() throws JspTagException {
        if (mailIterator != null) {
            return mailIterator.next();
        }
        return null;
    }

    protected boolean hasNext() throws JspTagException {
        return mailIterator != null && mailIterator.hasNext();
    }

    protected void prepare() throws JspTagException {

        try {
            List items = new ArrayList();
            if (userid == null || userid.equals("")) {
                userid = ExchangeManager.lookupUserId(null, pageContext);
            }
            mailbox = userid;

            // Start connection to cdo & exchange server
            CdoSessionWrapper session = ExchangeManager.getSession(userid, pageContext);

            Integer folderType = new Integer(CdoDefaultFolderTypes.CdoDefaultFolderInbox);
            Folder box = new FolderProxy(session.getDefaultFolder(folderType));

            // get the message collection from the inbox
            Messages msgs = new MessagesProxy(box.getMessages());

            // Har ved prøving og feiling funnet ut at denne gjør at
            // meldinger blir sortert slik at de nyeste får lavest indeks.
            msgs.sort(new Integer(CdoSortOrder.CdoDescending), null);

            int nofMsgs = ((Integer)msgs.getCount()).intValue();

            for (int k = 1; k <= nofMsgs && (max == -1 || k <= max); k++) {
                MailItem ei = new MailItem();
                Message message = new MessageProxy(msgs.getItem(new Integer(k)));

                boolean unread = message.getUnread().toString().equalsIgnoreCase("true");

                if (!unreadonly || unread) {
                    if (fieldList.size() == 0) {
                        AddressEntry sender = new AddressEntryProxy(message.getSender());
                        ei.setUnread(unread);
                        ei.setSender(sender.getName().toString());
                        ei.setSubject(message.getSubject().toString());
                        ei.setReceived((Date) message.getTimeReceived());        // Might not work
                        ei.setSensitivity(message.getSensitivity().toString());
//                          ei.setImportance(message.getImportance().toString());
//                          ei.setBody(message.getText().toString());
                        ei.setRecipients(message.getRecipients().toString());
                        ei.setUrl(getUrl(message));
                    } else {
                        for (int i = 0; i < fieldList.size(); i++) {
                            String field = fieldList.get(i).toString();
                            if (field.equalsIgnoreCase("sender")) {
                                AddressEntry sender = new AddressEntryProxy(message.getSender());
                                ei.setSender(sender.getName().toString());
                            }
                            if (field.equalsIgnoreCase("unread")) {
                                ei.setUnread(unread);
                            }
                            if (field.equalsIgnoreCase("subject")) {
                                ei.setSubject(message.getSubject().toString());
                            }
                            if (field.equalsIgnoreCase("received")) {
                                ei.setReceived((Date) message.getTimeReceived());
                            }
                            if (field.equalsIgnoreCase("sensitivity")) {
                                ei.setSensitivity(message.getSensitivity().toString());
                            }
                            if (field.equalsIgnoreCase("recipients")) {
                                ei.setRecipients(message.getRecipients().toString());
                            }
                            if (field.equalsIgnoreCase("url")) {
                                ei.setUrl(getUrl(message));
                            }
                        }
                    }
                    items.add(ei);
                }
            }
            mailIterator = items.iterator();
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
    }

    public void setUserid(String userid) {
        if (userid != null && userid.startsWith("$")){
            try {
                userid = ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
            } catch (JspException e) {
                Log.error(SOURCE, e, null, null);
            }
        }
        this.userid = userid;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public void setUnread(String unread) {
        if (unread.equalsIgnoreCase("true")) {
            unreadonly = true;
        }
    }

    public void setMax(String max) {
        if (max != null && max.startsWith("$")){
            try {
                this.max = ExpressionEvaluationUtils.evaluateInteger("max", max, pageContext);
            } catch (JspException e) {
                Log.error(SOURCE, e, null, null);
            }
        } else {
            try {
                this.max = Integer.parseInt(max);
            } catch (NumberFormatException e) {
                this.max = 0;
            }
        }
    }

    public void setFields(String fields) {
        fieldList = new ArrayList();
        String[] fieldArray = fields.split(",");
        for (int i = 0; i < fieldArray.length; i++) {
            fieldList.add(fieldArray[i].trim());
        }
    }


    /**
     * Henter verdien til feltet med ID: FIELD_ID_RELATIVE_URL fra meldingen gitt som parameter.
     * Denne verdien ser ut til å være "/folder/subject.EML" og kan brukes som en relativ
     * URL for OWA.
     * @param msg meldingen verdien skal hentes fra.
     * @return verdien til feltet med ID: FIELD_ID_RELATIVE_URL.
     */
    private String getUrl(Message msg) {
        String url = "";
        try {
            Fields fields = new FieldsProxy(msg.getFields());
            int nofFields = ((Integer)fields.getCount()).intValue();
            for (int i = 1; i <= nofFields; i++){
                Field field = new FieldProxy(fields.getItem(new Integer(i), null));
                if (((Integer)field.getID()).intValue() == FIELD_ID_RELATIVE_URL) {
                    url = field.getValue().toString();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

}
