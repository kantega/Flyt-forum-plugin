package no.kantega.exchange.tags;

import no.kantega.exchange.util.ExchangeSession;
import no.kantega.commons.log.Log;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;

import com.intrinsyc.cdo.Session;
import com.intrinsyc.cdo.AddressEntry;
import com.intrinsyc.cdo.AddressEntryProxy;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:15:06 PM
 */
public class GetUsernameTag extends TagSupport {
    private static final java.lang.String SOURCE = "exchange.GetUserNameTag";

    private String userid = null;

    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        JspWriter out;
        Session cdosession = null;
        try {
            // Start connection to cdo & exhange server
            ExchangeSession Xsession = new ExchangeSession();
            cdosession = Xsession.getInstance(userid, request, pageContext);

            // print the current user for this Session
            AddressEntry Xuser = new AddressEntryProxy(cdosession.getCurrentUser());

            out = pageContext.getOut();
            out.write(Xuser.getName().toString());

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
        this.userid = userid;
    }
}

