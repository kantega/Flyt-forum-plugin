package no.kantega.exchange.tags;

import com.intrinsyc.cdo.Session;
import com.intrinsyc.cdo.AddressEntry;
import com.intrinsyc.cdo.AddressEntryProxy;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspTagException;
import javax.servlet.http.HttpServletRequest;

import no.kantega.exchange.util.ExchangeSession;
import no.kantega.commons.log.Log;
import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.exception.ConfigurationException;
import no.kantega.publishing.common.Aksess;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:15:06 PM
 */
public class GetActiveTag extends BodyTagSupport {
    private static final String SOURCE = "exchange.GetActiveTag";

    private String userid = null;

    public int doStartTag() throws JspException {
        try {
            Configuration config = Aksess.getConfiguration();
            String active = config.getString("jintegra.exchange.active");
            if ("true".equalsIgnoreCase(active)) {
                return EVAL_BODY_AGAIN;
            }
            return SKIP_BODY;

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        }

    }

    public int doAfterBody() throws JspException {


        try {
            Configuration config = Aksess.getConfiguration();
            String active = config.getString("jintegra.exchange.active");

            if ("true".equalsIgnoreCase(active)) {
                bodyContent.writeOut(getPreviousOut());
            } else {
            }

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
            throw new JspTagException(SOURCE + ":" + e.getMessage());
        }

        return SKIP_BODY;
    }

}
