package no.kantega.exchange.tags;

import com.intrinsyc.cdo.*;
import no.kantega.commons.log.Log;
import no.kantega.exchange.model.CalendarItem;
import no.kantega.exchange.util.CdoSessionWrapper;
import no.kantega.exchange.util.ExchangeManager;
import no.kantega.publishing.security.SecuritySession;
import org.springframework.web.util.ExpressionEvaluationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:14:36 PM
 */
public class CalendarTag extends LoopTagSupport {

    private static final String SOURCE = "exchange.CalendarTag";
    private Iterator i;
    private String userid = null;
    private String fromdate = ""; // format: "1/9/07"
    private String todate = "";   // format: "2/9/07"
    //private String dateformat = "";  // Thu Sep 20 13:00:00 CEST 2007  // EEE MMM MM HH:mm:ss z yyyy
    private List items = null;

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

//        Session cdosession = null;
        CdoSessionWrapper session = null;
        try {
            boolean notdone = true;
            items = new ArrayList();

            // Declear some datetime formats
            SimpleDateFormat format_ex = new SimpleDateFormat("EEE MMM MM HH:mm:ss zzz yyyy");
            SimpleDateFormat format_in = new SimpleDateFormat("d/M/yy");

            // Start connection to cdo & exhange server
//            ExchangeSession Xsession = new ExchangeSession();
//            cdosession = Xsession.getCdoSession(userid, request, pageContext);
//            cdosession = ExchangeSession.getSession(userid, pageContext);
            session = ExchangeManager.getSession(userid, pageContext);

            // retrieve appointments collection from the CalendarItem
//            Integer defaultCalendar = new Integer();
//            Folder calendar = new FolderProxy(cdosession.getDefaultFolder(defaultCalendar));
            Folder calendar = new FolderProxy(session.getDefaultFolder(CdoDefaultFolderTypes.CdoDefaultFolderCalendar));

            // get the message collection from the calendar
            Messages calColl = new MessagesProxy(calendar.getMessages());
            MessageFilter calFilter = new MessageFilterProxy(calColl.getFilter());

            if (fromdate.equalsIgnoreCase("") || fromdate == null || fromdate.length() == 0) {
                Calendar now = Calendar.getInstance();
                fromdate = format_in.format(now.getTime());
            }

            if (todate.equalsIgnoreCase("") || todate == null || todate.length() == 0) {
                Calendar now = Calendar.getInstance();
                now.add(Calendar.DAY_OF_MONTH, 1);
                todate = format_in.format(now.getTime());
            }

            new FieldsProxy(calFilter.getFields()).add(
                    new Integer(CdoPropTags.CdoPR_START_DATE),
                    new String(todate), // Remember to reverse the date
                    null, null
            );
            new FieldsProxy(calFilter.getFields()).add(
                    new Integer(CdoPropTags.CdoPR_END_DATE),
                    new String(fromdate), // Remember to reverse the date
                    null, null
            );

            SecuritySession securitySession = SecuritySession.getInstance(request);
            String uid = null;
            if (securitySession.getUser() != null) {
                uid = securitySession.getUser().getId();
            }
            boolean showPrivate = false;
            if (userid == null || userid.equalsIgnoreCase(uid)) {
                showPrivate = true;
            }

            int n = 0;
            do {
                CalendarItem ci = new CalendarItem();
                try {
                    AppointmentItem appo = new AppointmentItemProxy(calColl.getNext());
                    ci.setSubject(appo.getSubject().toString());
                    ci.setLocation(appo.getLocation().toString());
                    ci.setDescription(appo.getText().toString());
                    ci.setStarttime((Date)appo.getStartTime());
                    ci.setEndtime((Date) appo.getEndTime());
                    Object sensitivity = appo.getSensitivity();
                    if (sensitivity != null && "2".equals(sensitivity.toString()) && !showPrivate) {
                        // Don't show private appointments for other than current user
                        ci.setSubject("Privat avtale");
                        ci.setLocation("");
                        ci.setDescription("");
                    }

                    // Check if event is all day
                    if (appo.getAllDayEvent() != null && "true".equalsIgnoreCase(appo.getAllDayEvent().toString())) {
                        ci.setAllday(true);
                    }
                    ci.setNoevents(false);
                    items.add(ci);
                    i = items.iterator();
                    n++;

                } catch (IllegalArgumentException e) {
                    notdone = false;
                    if(n==0){
                        items.add(ci);
                        i = items.iterator();
                    }
                }
            }
            while (notdone);

            userid = null;
            fromdate = "";
            todate = "";

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

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public void setDateformat(String dateformat) {

    }
}

