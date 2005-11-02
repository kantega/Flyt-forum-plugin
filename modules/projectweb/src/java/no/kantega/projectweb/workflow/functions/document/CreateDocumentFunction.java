package no.kantega.projectweb.workflow.functions.document;

import com.opensymphony.workflow.FunctionProvider;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.WorkflowDescriptor;
import com.opensymphony.workflow.spi.WorkflowEntry;
import com.opensymphony.workflow.spi.Step;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;
import no.kantega.projectweb.workflow.functions.AbstractCreateFunction;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 01:14:03
 * To change this template use File | Settings | File Templates.
 */
public class CreateDocumentFunction extends AbstractCreateFunction {

    public void persist(Object object, Map transientVars) {
        Document doc = (Document) object;
        getDao().addDocumentToProject(doc.getProject().getId(), (Document) object);
    }

    public void updateStatus(Object object, String status) {
        ((Document) object).setStatus(status);
    }

    public String getStatusKey() {
        return "document.status";
    }

    public Object createObject(Map transientVars) {
        return transientVars.get("document");
    }
}
