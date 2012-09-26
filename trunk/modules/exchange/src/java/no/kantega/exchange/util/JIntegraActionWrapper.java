package no.kantega.exchange.util;

import no.kantega.commons.log.Log;

import java.util.concurrent.Callable;

import com.intrinsyc.cdo.Session;

/**
 * User: tarkil
 * Date: Jul 18, 2008
 * Time: 9:15:33 AM
 */
public class JIntegraActionWrapper implements Callable {

    private static final String SOURCE = JIntegraActionWrapper.class.toString();

    public static final int ACTION_LOGON = 1;
    public static final int ACTION_LOGOFF = 2;

    private Session session;
    private int action;
    private Object[] params;

    
    public JIntegraActionWrapper(Session session, int action, Object[] params) {
        this.session = session;
        this.action = action;
        this.params = params;
    }

    public Object call() throws Exception {
        Object retVal = null;
        long start = System.nanoTime();
        switch (action) {
            case ACTION_LOGON:
                retVal = session.logon(null, null, new Boolean(false), new Boolean(true), new Integer(0), new Boolean(true), params[0] + "\n" + params[1]);
                break;
            case ACTION_LOGOFF:
                retVal = session.logoff();
                break;
            default:
                break;
        }
        Log.debug(SOURCE, "Action: " + action + " with param: " + (params != null && params.length == 2 ? "[" + params[0] + ", " + params[1] + "]": null) + " in " + ((System.nanoTime() - start) / 1000000.0) + " millisecs", null, null);
        return retVal;
    }
    
}
