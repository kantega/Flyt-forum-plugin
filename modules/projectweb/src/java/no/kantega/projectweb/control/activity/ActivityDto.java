package no.kantega.projectweb.control.activity;

import no.kantega.projectweb.model.Activity;
import no.kantega.modules.user.UserProfile;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 18:29:29
 * To change this template use File | Settings | File Templates.
 */
public class ActivityDto {
    private Activity activity;
    private UserProfile assigneeProfile;
    private UserProfile reporterProfile;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public UserProfile getAssigneeProfile() {
        return assigneeProfile;
    }

    public void setAssigneeProfile(UserProfile assigneeProfile) {
        this.assigneeProfile = assigneeProfile;
    }

    public UserProfile getReporterProfile() {
        return reporterProfile;
    }

    public void setReporterProfile(UserProfile reporerProfile) {
        this.reporterProfile = reporerProfile;
    }
}
