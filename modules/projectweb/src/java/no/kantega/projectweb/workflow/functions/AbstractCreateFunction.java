package no.kantega.projectweb.workflow.functions;

import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.loader.WorkflowDescriptor;

import java.util.Map;
import java.util.Collection;

import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.WorkflowParticipator;
import no.kantega.projectweb.dao.ProjectWebDao;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 13:29:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCreateFunction implements FunctionProvider {
    protected ProjectWebDao dao;

    public void execute(Map transientVars, Map args, com.opensymphony.module.propertyset.PropertySet propertySet) throws WorkflowException {

        Object object = createObject(transientVars);
        WorkflowDescriptor descriptor = (WorkflowDescriptor) transientVars.get("descriptor");
        WorkflowEntry entry = (WorkflowEntry)transientVars.get("entry");
        Integer action = (Integer) transientVars.get("actionId");
        Collection steps = (Collection) transientVars.get("currentSteps");
        if(steps.size() == 1) {
            Step step = (Step) steps.iterator().next();
            Map meta = descriptor.getStep(step.getStepId()).getMetaAttributes();
            String status = (String) meta.get(getStatusKey());
            if(status != null) {
                updateStatus(object, status);
            }
        }
        ((WorkflowParticipator)object).setWorkflowId(entry.getId());

        persist(object, transientVars);


    }

    public abstract void persist(Object object, Map transientVars);

    public abstract void updateStatus(Object object, String status);

    public abstract String getStatusKey();

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
    protected ProjectWebDao getDao() {
        return dao;
    }

    public abstract Object createObject(Map transientVars);

}
