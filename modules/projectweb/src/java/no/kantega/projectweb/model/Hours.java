package no.kantega.projectweb.model;

import java.util.Set;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 05.sep.2005
 * Time: 13:37:17
 * To change this template use File | Settings | File Templates.
 */
public class Hours {

    private float estimatedHours;
    private float usedHours;
    private float estimatedLeftHours;

    public float getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(float estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public float getUsedHours() {
        return usedHours;
    }

    public void setUsedHours(float usedHours) {
        this.usedHours = usedHours;
    }

    public float getEstimatedLeftHours() {
        return estimatedLeftHours;
    }

    public void setEstimatedLeftHours(float estimatedLeftHours) {
        this.estimatedLeftHours = estimatedLeftHours;
    }
}
