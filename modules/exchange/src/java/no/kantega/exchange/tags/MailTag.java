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
    private static final String KEY_CACHE_MAIL_TAG = "mailTagCache";

    private Iterator mailIterator;
    private String userid;
    private String mailbox = "";
    //private String fromdate = ""; // format: "1/9/07"
    //private String todate = "";   // format: "2/9/07"
    private boolean unreadonly = false;
    //private boolean filter = false;
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
            List items = null;
            if (userid == null || userid.equals("")) {
                userid = ExchangeManager.lookupUserId(null, pageContext);
            }
            mailbox = userid;

            // caching.
            HttpSession httpSession = pageContext.getSession();
            if (httpSession != null) {
                MailTagModel model = (MailTagModel)httpSession.getAttribute(KEY_CACHE_MAIL_TAG);
                if (isValidCache(model)) {
                    Log.debug(SOURCE, "Got list of mails for user: " + userid + " from session.", null, null);
                    items = model.getMailList();
                }
            }

            if (items == null) {
                // listen med mail ble ikke funnet i sesjon.
                // må hente på nytt fra exchange-server
                items = new ArrayList();
                Folder box = null;

                // Start connection to cdo & exchange server
                CdoSessionWrapper session = ExchangeManager.getSession(userid, pageContext);

//                if (mailbox != null && !"".equals(mailbox)) {
//                    // TODO: Find out order of inputs and how to get folder by names
//                    box = new FolderProxy(session.getFolder(mailbox, ""));
//                } else {
                    Integer folderType = Integer.valueOf(CdoDefaultFolderTypes.CdoDefaultFolderInbox);
                    box = new FolderProxy(session.getDefaultFolder(folderType));
//                }

                // get the message collection from the inbox
                Messages msgs = new MessagesProxy(box.getMessages());
                // Har ved prøving og feiling funnet ut at denne gjør at
                // meldinger blir sortert slik at de nyeste får lavest indeks.
                msgs.sort(Integer.valueOf(CdoSortOrder.CdoDescending), null);

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

                if (httpSession != null && ExchangeManager.getCacheTimeLimit() != 0) {
                    MailTagModel model = new MailTagModel();
                    model.setUserid(userid);
                    model.setMailbox(mailbox);
                    model.setUnreadonly(unreadonly);
                    model.setMax(max);
                    model.setMailList(items);
                    model.setTimeRetrieved(new Date());
                    model.setFieldList(fieldList);
                    httpSession.setAttribute(KEY_CACHE_MAIL_TAG, model);
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

    private boolean isValidCache(MailTagModel model) {
        boolean valid = false;

        if (model != null) {
            if (model.getUserid() != null && model.getUserid().equals(userid)
                    && model.getMailbox() != null && model.getMailbox().equals(mailbox)
                    && model.isUnreadonly() == unreadonly
                    && model.getMax() == max
                    && model.getMailList() != null) {

                List cacheFieldList = model.getFieldList();
                if (cacheFieldList != null && cacheFieldList.size() == fieldList.size()) {
                    boolean fieldListsEqual = true;
                    for (int i = 0; i < cacheFieldList.size(); i++) {
                        String field = cacheFieldList.get(i).toString();
                        if (!fieldList.contains(field)) {
                            fieldListsEqual = false;
                            break;
                        }
                    }
                    if (fieldListsEqual) {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.SECOND, -ExchangeManager.getCacheTimeLimit());
                        if (model.getTimeRetrieved().compareTo(cal.getTime()) >= 0) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return valid;
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
