package no.kantega.exchange.tags;

import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.text.SimpleDateFormat;

import com.intrinsyc.cdo.*;
import no.kantega.publishing.security.data.User;
import no.kantega.exchange.util.ExchangeSession;
import no.kantega.exchange.model.CalendarItem;

import no.kantega.commons.log.Log;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:14:36 PM
 */
public class CalendarTag extends LoopTagSupport {

    private static final String SOURCE = "exchange.CalendarTag";
    private Iterator i;
    private String userid;
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

        Session cdosession = null;
        try {

            boolean notdone = true;
            items = new ArrayList();

            // Declear some datetime formats
            SimpleDateFormat format_ex = new SimpleDateFormat("EEE MMM MM HH:mm:ss zzz yyyy");
            SimpleDateFormat format_in = new SimpleDateFormat("d/M/yy");

            // Start connection to cdo & exhange server
            ExchangeSession Xsession = new ExchangeSession();
            cdosession = Xsession.getInstance(userid, request, pageContext);

            // retrieve appointments collection from the CalendarItem
            Integer defaultCalendar = new Integer(CdoDefaultFolderTypes.CdoDefaultFolderCalendar);
            Folder calendar = new FolderProxy(cdosession.getDefaultFolder(defaultCalendar));

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

            do {
                try {
                    AppointmentItem appo = new AppointmentItemProxy(calColl.getNext());
                    CalendarItem ci = new CalendarItem();

                    no.kantega.exchange.model.CalendarItem cis = new CalendarItem();

                    ci.setSubject(appo.getSubject().toString());
                    ci.setLocation(appo.getLocation().toString());
                    ci.setDescription(appo.getText().toString());
                    ci.setStarttime(format_ex.parse(appo.getStartTime().toString()));
                    ci.setEndtime(format_ex.parse(appo.getEndTime().toString()));

                    // Check if event is all day
                    if (appo.getAllDayEvent().toString().equals("true")) {
                        ci.setAllday(true);
                    }

                    items.add(ci);
                    i = items.iterator();
                } catch (IllegalArgumentException e) {
                    notdone = false;
                }
            }
            while (notdone);
        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        }
    }

    public void setUserid(String userid) {
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

