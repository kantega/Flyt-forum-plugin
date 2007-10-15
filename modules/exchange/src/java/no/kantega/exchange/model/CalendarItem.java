package no.kantega.exchange.model;

import java.util.Date;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:10:41 PM
 */
public class CalendarItem {

    private String subject = "";
    private Date starttime;
    private Date endtime;
    private String location = "";
    private String description = "";
    private boolean allday = false;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getAllday() {
        return allday;
    }

    public void setAllday(boolean allday) {
        this.allday = allday;
    }
}
