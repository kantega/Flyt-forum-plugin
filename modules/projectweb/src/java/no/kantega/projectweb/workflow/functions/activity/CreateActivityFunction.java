package no.kantega.projectweb.workflow.functions.activity;

import com.opensymphony.workflow.FunctionProvider;

import java.util.Map;

import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.workflow.functions.AbstractCreateFunction;
import no.kantega.projectweb.activity.ActivityStatusManager;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 01:14:03
 * To change this template use File | Settings | File Templates.
 */
public class CreateActivityFunction extends AbstractCreateFunction implements FunctionProvider {
    private ActivityStatusManager statusManager;
    public void persist(Object object, Map transientVars) {
        Activity activity = (Activity) object;
        getDao().addActivityToProject(activity.getProject().getId(), (Activity) object);
    }

    public void updateStatus(Object object, String status) {
        Activity activity = (Activity) object;
        activity.setStatus(statusManager.getDefaultStatus(activity.getProject()));
    }

    public String getStatusKey() {
        return "activity.status";
    }

    public Object createObject(Map transientVars) {
        return transientVars.get("activity");
    }

    public void setStatusManager(ActivityStatusManager statusManager) {
        this.statusManager = statusManager;
    }
}
