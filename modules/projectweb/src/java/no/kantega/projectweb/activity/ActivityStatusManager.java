package no.kantega.projectweb.activity;

import no.kantega.projectweb.model.ActivityStatus;
import no.kantega.projectweb.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 26.sep.2005
 * Time: 16:24:34
 * To change this template use File | Settings | File Templates.
 */
public interface ActivityStatusManager {
    public ActivityStatus[] getActivityStatuses(Project project);

    ActivityStatus getDefaultStatus(Project project);
}
