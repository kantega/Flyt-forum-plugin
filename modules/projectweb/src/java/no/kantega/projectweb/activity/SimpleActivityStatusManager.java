package no.kantega.projectweb.activity;

import no.kantega.projectweb.model.ActivityStatus;
import no.kantega.projectweb.model.Project;
import no.kantega.projectweb.dao.ProjectWebDao;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 26.sep.2005
 * Time: 16:26:54
 * To change this template use File | Settings | File Templates.
 */
public class SimpleActivityStatusManager implements ActivityStatusManager {
    private ProjectWebDao dao;

    public ActivityStatus[] getActivityStatuses(Project project) {
        return dao.getActivityStatuses();
    }

    public ActivityStatus getDefaultStatus(Project project) {
        return dao.getActivityStatus(1);
    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
