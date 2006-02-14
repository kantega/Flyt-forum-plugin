package no.kantega.projectweb.workflow.functions.document;

import java.util.Map;

import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.model.WorkflowParticipator;
import no.kantega.projectweb.workflow.functions.AbstractStatusUpdateFunction;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 01:14:03
 * To change this template use File | Settings | File Templates.
 */
public class UpdateDocumentStatusFunction extends AbstractStatusUpdateFunction {
    
    protected void persist(Object object) {
        getDao().saveOrUpdate((Document)object);
    }

    protected void updateStatus(Object object, String status) {
        //((Document)object).setCategory(status);
    }

    protected String getStatusKey() {
        return "document.status";
    }

    protected WorkflowParticipator getWorkflowParticipator(Map transientVars) {
        long documentId = ((Long)transientVars.get("documentId")).longValue();
        return getDao().getDocument(documentId);

    }


}
