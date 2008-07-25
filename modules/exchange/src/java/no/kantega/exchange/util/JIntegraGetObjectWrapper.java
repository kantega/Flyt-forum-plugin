package no.kantega.exchange.util;

import com.intrinsyc.cdo.Session;

import java.util.concurrent.Callable;

import no.kantega.commons.log.Log;

/**
 * User: tarkil
 * Date: Jul 21, 2008
 * Time: 11:54:52 AM
 */
public class JIntegraGetObjectWrapper implements Callable {

    private static final String SOURCE = JIntegraGetObjectWrapper.class.toString();

    public static final int OBJECT_ADDRESS_LIST = 1;
    public static final int OBJECT_CURRENT_USER = 2;
    public static final int OBJECT_DEFAULT_FOLDER = 4;
    public static final int OBJECT_FOLDER = 8;
    public static final int OBJECT_INBOX = 16;
    public static final int OBJECT_NAME = 32;

    private Session session;
    private int object;
    private Object[] params;


    public JIntegraGetObjectWrapper(Session session, int object, Object[] params) {
        this.session = session;
        this.object = object;
        this.params = params;
    }

    public Object call() throws Exception {
        Object retVal = null;
        long start = System.nanoTime();
        switch (object) {
            case OBJECT_ADDRESS_LIST:
                retVal = session.getAddressList(params[0]);
                break;
            case OBJECT_CURRENT_USER:
                retVal = session.getCurrentUser();
                break;
            case OBJECT_DEFAULT_FOLDER:
                retVal = session.getDefaultFolder(params[0]);
                break;
            case OBJECT_FOLDER:
                retVal = session.getFolder(params[0], params[1]);
                break;
            case OBJECT_INBOX:
                retVal = session.getInbox();
                break;
            case OBJECT_NAME:
                retVal = session.getName();
                break;
            default:
                break;
        }

        String paramString = null;
        if (params != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                builder.append(params[i]).append(" ");
            }
            paramString = "["+builder.toString().trim()+"]".replaceAll(" ", ", ");
        }
        Log.debug(SOURCE, "Get object: " + object + " with param: " + paramString + " in " + ((System.nanoTime() - start) / 1000000.0) + " millisecs", null, null);
        return retVal;
    }

}
