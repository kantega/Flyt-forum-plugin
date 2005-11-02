package no.kantega.projectweb.control.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.projectweb.model.Activity;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 14:16:52
 * To change this template use File | Settings | File Templates.
 */
public class ActivityValidator implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals(Activity.class);
    }

    public void validate(Object o, Errors errors) {
        Activity activity = (Activity) o;
        if(activity.getTitle().length() < 3) {
            errors.rejectValue("title", "title.too-short", "Title to short");
        }
        if(activity.getStartDate() != null && activity.getEndDate() != null) {
            if(activity.getStartDate().getTime() > activity.getEndDate().getTime()) {
                errors.rejectValue("endDate", "endDate.before-start", "Activity must end after it starts");
            }
        }
    }
}
