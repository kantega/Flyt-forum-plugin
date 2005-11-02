package no.kantega.projectweb.propertyeditors;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.ActivityType;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 16:50:03
 * To change this template use File | Settings | File Templates.
 */
public class ActivityTypeEditor extends PropertyEditorSupport {

    private ProjectWebDao dao;


    public ActivityTypeEditor(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAsText(String s) throws IllegalArgumentException {
        System.out.println("type setastext: " + s);
        long activitTypeId = new Long(s).longValue();
        if(activitTypeId == -1) {
            setValue(null);
        } else {
            ActivityType type = dao.getActivityType(activitTypeId);
            setValue(type);
        }
    }

    public String getAsText() {
        if(getValue() == null) {
            return "-1";
        }
        return new Long(((ActivityType)getValue()).getId()).toString();

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
