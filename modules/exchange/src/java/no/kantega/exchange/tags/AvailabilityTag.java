package no.kantega.exchange.tags;

import com.intrinsyc.cdo.*;
import com.linar.jintegra.AutomationException;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;

import no.kantega.publishing.security.data.User;
import no.kantega.publishing.security.SecuritySession;
import no.kantega.publishing.security.realm.SecurityRealm;
import no.kantega.publishing.security.realm.SecurityRealmFactory;
import no.kantega.publishing.modules.exchange.ExchangeSession;
import no.kantega.publishing.common.Aksess;
import no.kantega.commons.exception.SystemException;
import no.kantega.commons.log.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.File;

import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:13:25 PM
 */
public class AvailabilityTag extends TagSupport {
    private static final String SOURCE = "exchange.AvailabilityTag";

    private String userid;
    private String starttime;       // Specifies start time for checking availability
    private String endtime;         // Specifies end time for checking availability
    private String timeslot;        // Specificies length of timeslot in minutes(default 30 min)
    private String multislot;       // Use to show multiple timeslots
    private String image;           // If an image should be shown, does not work with multislot
    private String cssclass = null; // cssclass to use, for use with image
    private String alt = "";        // To use with image

    private boolean imageshow = false;

    public final static String DEFAULT_EXCHANGE_DIR = "/aksess/bitmaps/exchange/";

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        JspWriter out;
        Session cdosession = null;
        try {
            User user = null;
            boolean validuser = true;
            SimpleDateFormat format_ex = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss aa");
            SimpleDateFormat time_in = new SimpleDateFormat("HH:mm");

            if (userid != null) {
                userid = ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
                SecurityRealm realm = SecurityRealmFactory.getInstance();
                try {
                    user = realm.lookupUser(userid);
                    validuser = true;
                } catch (SystemException e) {
                }
            }

            if (validuser) {

                Calendar now = Calendar.getInstance();
                // Get starttime from input for "now"
                if (starttime != null && !"".equals(starttime)) {
                    try {
                        starttime = format_ex.format(time_in.parse(starttime));
                    } catch (Exception e) {
                        starttime = format_ex.format(now.getTime());
                    }
                } else {
                    starttime = format_ex.format(now.getTime());
                }

                // Get endtime from input or "now + 30min"
                if (endtime != null && !"".equals(endtime)) {
                    try {
                        endtime = format_ex.format(time_in.parse(endtime));
                    } catch (Exception e) {
                        now.add(Calendar.MINUTE, 30);
                        endtime = format_ex.format(now.getTime());
                    }
                } else {
                    now.add(Calendar.MINUTE, 30);
                    endtime = format_ex.format(now.getTime());
                }

                // Start connection to cdo & exhange server
                ExchangeSession Xsession = new ExchangeSession();
                cdosession = Xsession.getInstance(userid, request);

                // Create a message in Inbox
                Folder inbox = new FolderProxy(cdosession.getInbox());
                Messages messages = new MessagesProxy(inbox.getMessages());
                Message message = new MessageProxy(messages.add("Subject", "text", null, null));

                // Set the recipient(s) of the message
                Recipients recipients = new RecipientsProxy(message.getRecipients());
                Recipient oneRecipient = new RecipientProxy(recipients.add(userid, null, null, null));
                oneRecipient.resolve(null);
                AddressEntry address = new AddressEntryProxy(oneRecipient.getAddressEntry());
                address = new AddressEntryProxy(oneRecipient.getAddressEntry());

                // Specify the length of each time slot in minutes.
                // If Interval parameter is less than 1, GetFreeBusy returns
                // CdoE_INVALID_PARAMETER.
                int interval = 30;
                if (timeslot == null && "".equals(timeslot)) {
                    try {
                        interval = Integer.parseInt(timeslot);
                    } catch (Exception e) {
                    }
                }
                // Get the actual statuses from exchange
                String freeBusy = address.getFreeBusy(starttime, endtime, new Integer(interval)).toString();

                // Output
                out = pageContext.getOut();

                // Show status as an image if
                if (imageshow) {
                    Integer j;
                    String icon = "";
                    for (int i = 0; i == 0; i++) {
                        j = new Integer(String.valueOf(freeBusy.charAt(i)));
                        switch (j.intValue()) {
                            case 0:
                                icon = "status_free.gif";
                                break;
                            case 1:
                                icon = "status_partly.gif";
                                break;
                            case 2:
                                icon = "status_busy.gif";
                                break;
                            default:
                                break;
                        }
                    }
                    // Check if the image file exists
                    File f = new File(pageContext.getServletContext().getRealPath(DEFAULT_EXCHANGE_DIR + icon));
                    if (f.exists()) {
                        out.write("<img src=\"" + Aksess.getContextPath() + DEFAULT_EXCHANGE_DIR + icon + "\" alt=\"" + alt + "\"");
                        if (cssclass != null) {
                            out.write(" class=\"" + cssclass + "\"");
                        }
                        out.write("/>");
                    }
                } else {
                    out.write(freeBusy);
                }
            }
        } catch (AutomationException e) {
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        userid = null;
        return EVAL_PAGE;
    }

    public void setUserid(String userid) {
        try {
            this.userid = ExpressionEvaluationUtils.evaluateString("userid", userid, pageContext);
        } catch (JspException e) {
            Log.error(SOURCE, e, null, null);
        }
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public void setMultislot(String multislot) {
        this.multislot = multislot;
    }

    public void setImage(String image) {
        if (image.equalsIgnoreCase("true")) {
            imageshow = true;
        }
        this.image = image;
    }

    public String getCssclass() {
        return cssclass;
    }

    public void setCssclass(String cssclass) {
        try {
            this.cssclass = ExpressionEvaluationUtils.evaluateString("cssclass", cssclass, pageContext);
        } catch (JspException e) {
            Log.error(SOURCE, e, null, null);
        }
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}

