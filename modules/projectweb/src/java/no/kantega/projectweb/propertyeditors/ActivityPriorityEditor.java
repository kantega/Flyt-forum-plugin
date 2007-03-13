package no.kantega.projectweb.propertyeditors;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.ActivityType;
import no.kantega.projectweb.model.ActivityPriority;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;


public class ActivityPriorityEditor extends PropertyEditorSupport {

    private ProjectWebDao dao;


    public ActivityPriorityEditor(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAsText(String s) throws IllegalArgumentException {
        System.out.println("setastext: " + s);
        long priorityId;

        try {
            priorityId = Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if(priorityId == -1) {
            setValue(null);
        } else {
            ActivityPriority priority = dao.getActivityPriority(priorityId);
            if(priority == null) {
                throw new IllegalArgumentException("No such priority: " + priorityId);
            }
            setValue(priority);
        }
    }

    public String getAsText() {
        if(getValue() == null) {
            return "-1";
        }
        return new Long(((ActivityPriority)getValue()).getId()).toString();

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
