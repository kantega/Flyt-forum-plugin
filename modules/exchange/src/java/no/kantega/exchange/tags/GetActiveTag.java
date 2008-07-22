package no.kantega.exchange.tags;

import no.kantega.commons.configuration.Configuration;
import no.kantega.commons.log.Log;
import no.kantega.exchange.util.ExchangeManager;
import no.kantega.publishing.common.Aksess;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * User: Espen A. Fossen @ Kantega
 * Date: Oct 9, 2007
 * Time: 12:15:06 PM
 */
public class GetActiveTag extends BodyTagSupport {

    private static final String SOURCE = GetActiveTag.class.toString();

    private String userid = null;


    public int doStartTag() throws JspException {
        int retVal = SKIP_BODY;
        try {
//            Configuration config = Aksess.getConfiguration();
//            String active = config.getString("jintegra.exchange.active");
//            if ("true".equalsIgnoreCase(active)) {
//                retVal = EVAL_BODY_AGAIN;
//            }

            boolean active = Aksess.getConfiguration().getBoolean("jintegra.exchange.active", false);
            active &= ExchangeManager.verifyConnection(pageContext);

            if (active) {
                retVal = EVAL_BODY_BUFFERED;
            }

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
        return retVal;
    }

    public int doAfterBody() throws JspException {
        try {
            Configuration config = Aksess.getConfiguration();
            String active = config.getString("jintegra.exchange.active");

            if ("true".equalsIgnoreCase(active)) {
                bodyContent.writeOut(getPreviousOut());
            }

        } catch (Exception e) {
            Log.error(SOURCE, e, null, null);
        }
        return EVAL_PAGE;
    }

}
