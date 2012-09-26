package no.kantega.projectweb.workflow.functions.activity;

import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.SimpleStep;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.WorkflowParticipator;
import no.kantega.projectweb.workflow.functions.AbstractStatusUpdateFunction;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 01:14:03
 * To change this template use File | Settings | File Templates.
 */
public class UpdateActivityStatusFunction extends  AbstractStatusUpdateFunction {

    protected void persist(Object object) {
        getDao().saveOrUpdate((Activity) object);
    }

    protected void updateStatus(Object object, String status) {
        Activity activity = (Activity) object;
        activity.setStatus(getDao().getActivityStatusByCode(status));
    }

    protected String getStatusKey() {
        return "activity.status";
    }

    protected WorkflowParticipator getWorkflowParticipator(Map transientVars) {
        long activityId = ((Long)transientVars.get("activityId")).longValue();

         return getDao().getActivity(activityId);
    }
}
