package no.kantega.exchange.model;

import java.util.Date;
import java.util.List;

/**
 * User: tarkil
 * Date: Jul 25, 2008
 * Time: 8:29:44 AM
 */
public class MailTagModel {

    private static final String SOURCE = MailTagModel.class.toString();

    private String userid;
    private String mailbox;
    private boolean unreadonly;
    private int max;
    private List mailList;
    private Date timeRetrieved;
    private List fieldList;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public boolean isUnreadonly() {
        return unreadonly;
    }

    public void setUnreadonly(boolean unreadonly) {
        this.unreadonly = unreadonly;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List getMailList() {
        return mailList;
    }

    public void setMailList(List mailList) {
        this.mailList = mailList;
    }

    public Date getTimeRetrieved() {
        return timeRetrieved;
    }

    public void setTimeRetrieved(Date timeRetrieved) {
        this.timeRetrieved = timeRetrieved;
    }

    public List getFieldList() {
        return fieldList;
    }

    public void setFieldList(List fieldList) {
        this.fieldList = fieldList;
    }

}
