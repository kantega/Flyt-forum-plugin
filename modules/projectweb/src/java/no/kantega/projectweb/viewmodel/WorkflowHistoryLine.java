package no.kantega.projectweb.viewmodel;

import com.opensymphony.workflow.spi.Step;
import com.opensymphony.workflow.loader.ActionDescriptor;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 10.sep.2005
 * Time: 03:11:40
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowHistoryLine {

        private Step step;
        private ActionDescriptor actionDescriptor;

        public Step getStep() {
            return step;
        }

        public void setStep(Step step) {
            this.step = step;
        }

        public ActionDescriptor getActionDescriptor() {
            return actionDescriptor;
        }

        public void setActionDescriptor(ActionDescriptor actionDescriptor) {
            this.actionDescriptor = actionDescriptor;
        }
    }