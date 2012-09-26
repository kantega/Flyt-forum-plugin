package no.kantega.projectweb.propertyeditors;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.ActivityType;
import no.kantega.projectweb.model.ProjectPhase;

import java.beans.PropertyEditorSupport;

public class ProjectPhaseEditor extends PropertyEditorSupport {

    private ProjectWebDao dao;


    public ProjectPhaseEditor(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAsText(String s) throws IllegalArgumentException {
        long projectPhaseId = new Long(s).longValue();
        if(projectPhaseId == -1) {
            setValue(null);
        } else {
            ProjectPhase phase = dao.getProjectPhase(projectPhaseId);
            setValue(phase);
        }
    }

    public String getAsText() {
        if(getValue() == null) {
            return "-1";
        }
        return new Long(((ProjectPhase)getValue()).getId()).toString();

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
