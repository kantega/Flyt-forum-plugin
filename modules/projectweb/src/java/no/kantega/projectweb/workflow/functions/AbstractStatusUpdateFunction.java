package no.kantega.projectweb.workflow.functions;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.WorkflowParticipator;

import java.util.Map;

import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.WorkflowStore;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 13:52:10
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractStatusUpdateFunction implements FunctionProvider {
    private ProjectWebDao dao;

    public void execute(Map transientVars, Map args, com.opensymphony.module.propertyset.PropertySet propertySet) throws WorkflowException {

        WorkflowParticipator object = getWorkflowParticipator(transientVars);

        WorkflowDescriptor descriptor = (WorkflowDescriptor) transientVars.get("descriptor");
        WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
        WorkflowStore store = (WorkflowStore) transientVars.get("store");
        Step step = (Step) store.findCurrentSteps(entry.getId()).get(0);
        Map meta = descriptor.getStep(step.getStepId()).getMetaAttributes();
        String status = (String) meta.get(getStatusKey());

        if(status  != null) {
            updateStatus(object, status);
        }

        object.setWorkflowId(entry.getId());

        persist(object);

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
    protected ProjectWebDao getDao() {
        return dao;
    }

    protected abstract void persist(Object object);

    protected abstract void updateStatus(Object object, String status);

    protected abstract String getStatusKey();

    protected abstract WorkflowParticipator getWorkflowParticipator(Map transientVars);
}
