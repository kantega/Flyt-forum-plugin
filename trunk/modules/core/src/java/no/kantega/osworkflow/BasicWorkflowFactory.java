package no.kantega.osworkflow;

import com.opensymphony.workflow.config.Configuration;
import com.opensymphony.workflow.TypeResolver;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.basic.BasicWorkflow;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 09.sep.2005
 * Time: 23:42:44
 * To change this template use File | Settings | File Templates.
 */
public class BasicWorkflowFactory {
    private Configuration configuration;

    private TypeResolver typeResolver;


    public Workflow createBasicWorkflow(String caller) {
        BasicWorkflow workflow = new BasicWorkflow(caller);
        workflow.setConfiguration(configuration);
        workflow.setResolver(typeResolver);
        return workflow;
    }
    
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setTypeResolver(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }
}
