package no.kantega.exchange.util;

import com.intrinsyc.cdo.Session;

import java.util.concurrent.*;

/**
 * User: tarkil
 * Date: Jul 21, 2008
 * Time: 8:00:58 AM
 *
 * Dette er en wrapper-klasse for com.intrinsyc.cdo.Session. Alle operasjoner på
 * objekter fra den klassen bør gå gjennom denne.
 */
public class CdoSessionWrapper {

    private static final String SOURCE = CdoSessionWrapper.class.toString();

    private ExecutorService executorService;
    private Session session = null;
    private boolean loggedOn = false;


    public CdoSessionWrapper(String cdoServerAddress) throws Exception {
        executorService = Executors.newCachedThreadPool();
        Callable createSessionWrapper = new JIntegraCreateCdoSessionWrapper(cdoServerAddress);
        Future f = executorService.submit(createSessionWrapper);
        session = (Session)f.get(ExchangeManager.getTimeout(), ExchangeManager.getUnit());
    }

    public void logon(String server, String id) throws Exception {
        Callable logonWrapper = new JIntegraActionWrapper(session, JIntegraActionWrapper.ACTION_LOGON, new Object[]{server, id});
        Future f = executorService.submit(logonWrapper);
        f.get(ExchangeManager.getTimeout(), ExchangeManager.getUnit());
        loggedOn = true;
    }

    public void logoff() throws Exception {
        if (isLoggedOn()) {
            Callable logoffWrapper = new JIntegraActionWrapper(session, JIntegraActionWrapper.ACTION_LOGOFF, null);
            Future f = executorService.submit(logoffWrapper);
            f.get(ExchangeManager.getTimeout(), ExchangeManager.getUnit());
            loggedOn = false;
        }
    }

    public boolean isLoggedOn() {
        return loggedOn;
    }

    public Object getInbox() {
        return getObject(JIntegraGetObjectWrapper.OBJECT_INBOX, null);
    }

    public Object getDefaultFolder(Object defaultFolderType) {
        return getObject(JIntegraGetObjectWrapper.OBJECT_DEFAULT_FOLDER, new Object[]{defaultFolderType});
    }

    public Object getCurrentUser() {
        return getObject(JIntegraGetObjectWrapper.OBJECT_CURRENT_USER, null);
    }

    public Object getAddressList(Object addressListType) {
        return getObject(JIntegraGetObjectWrapper.OBJECT_ADDRESS_LIST, new Object[]{addressListType});
    }

    public Object getFolder(Object mailbox, Object o2) {
        return getObject(JIntegraGetObjectWrapper.OBJECT_FOLDER, new Object[]{mailbox, o2});
    }

    public Object getName() {
        return getObject(JIntegraGetObjectWrapper.OBJECT_NAME, null);
    }

    private Object getObject(int type, Object[] params) {
        return execute(new JIntegraGetObjectWrapper(this.session, type, params));
    }

    private Object execute(Callable callableObject) {
        Object retVal = null;
        Future f = executorService.submit(callableObject);
        try {
            retVal = f.get(ExchangeManager.getTimeout(), ExchangeManager.getUnit());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            logoff();
        } catch (Exception e) {
        }
    }

}
