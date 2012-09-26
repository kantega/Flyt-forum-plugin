package no.kantega.exchange.tags;

import com.intrinsyc.cdo.AddressEntry;
import com.intrinsyc.cdo.AddressEntryProxy;
import no.kantega.commons.log.Log;
import no.kantega.exchange.util.CdoSessionWrapper;
import no.kantega.exchange.util.ExchangeManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:15:06 PM
 */
public class GetUsernameTag extends TagSupport {

    private static final String SOURCE = GetUsernameTag.class.toString();

    private String userid = null;


    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        JspWriter out;
        CdoSessionWrapper session = null;
        try {
            // Start connection to cdo & exhange server
            session = ExchangeManager.getSession(userid, pageContext);

            // print the current user for this Session
            AddressEntry Xuser = new AddressEntryProxy(session.getCurrentUser());

            out = pageContext.getOut();
            out.write(Xuser.getName().toString());

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
