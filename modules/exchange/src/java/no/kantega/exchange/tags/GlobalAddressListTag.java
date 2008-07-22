package no.kantega.exchange.tags;

import no.kantega.publishing.security.data.User;
import no.kantega.exchange.util.CdoSessionWrapper;
import no.kantega.exchange.util.ExchangeManager;
import no.kantega.commons.log.Log;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.http.HttpServletRequest;

import com.intrinsyc.cdo.*;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:15:37 PM
 */
public class GlobalAddressListTag extends TagSupport {

    //TODO; Class needs to be adjuste to a LoopTag
    private static final String SOURCE = "aksess.GlobalAddressListTag";
    private String userid;

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        JspWriter out;
//        Session cdosession = null;
        CdoSessionWrapper session = null;
        try {
            User user = null;
            boolean validuser = false;
            String results = "";

                // Start connection to cdo & exhange server
//                ExchangeSession Xsession = new ExchangeSession();
//                cdosession = Xsession.getCdoSession(userid, request, pageContext);
//                cdosession = ExchangeSession.getSession(userid, pageContext);
                session = ExchangeManager.getSession(userid, pageContext);

                // get the Global Address List
//                AddressListProxy addressList = new AddressListProxy(cdosession.getAddressList(new Integer(CdoAddressListTypes.CdoAddressListGAL)));
                AddressListProxy addressList = new AddressListProxy(session.getAddressList(CdoAddressListTypes.CdoAddressListGAL));
                // get the collection of addresses
                AddressEntriesProxy addressEntries = new AddressEntriesProxy(
                        addressList.getAddressEntries());
                // loop through the addresses and print them out
                int count = ((Integer) addressEntries.getCount()).intValue();
                if (count < 1) {
                    results += "No GAL entries found.";
                } else {
                    System.out.println("Found GAL entries...");
                    for (int i = 1; i <= count; i++) {
                        AddressEntryProxy addressEntry = new AddressEntryProxy(
                                addressEntries.getItem(new Integer(i)));
                        results += "NAME    : " + addressEntry.getName();
                        results += "ADDRESS : " + addressEntry.getAddress();
                    }
                }

                out = pageContext.getOut();
                out.write(results);

        } catch (IllegalArgumentException e) {
            System.out.println("No appointment found.\n" +
                            "Make sure there is at least one appointment exists in you filter.\n");
            e.printStackTrace();
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
