package no.kantega.projectweb.control.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Project;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 14:16:52
 * To change this template use File | Settings | File Templates.
 */
public class ProjectValidator implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals(Project.class);
    }

    public void validate(Object o, Errors errors) {
        Project project = (Project) o;
        if(project.getName().length() < 3) {
            errors.rejectValue("name", "name.too-short", "Name too short");
        }
        if(project.getCode().length() < 2) {
            errors.rejectValue("code", "code.too-short", "Code too short");
        }
        for (int i = 0; i < project.getCode().toCharArray().length; i++) {
            char c = project.getCode().toCharArray()[i];
            if(!Character.isLetter(c) || !Character.isUpperCase(c)) {
                errors.rejectValue("code", "code.illegal", "Illegal code");
                break;
            }
        }
        if(project.getStartDate() != null && project.getEndDate() != null) {
            if(project.getStartDate().getTime() > project.getEndDate().getTime()) {
                errors.rejectValue("endDate", "endDate.before-start", "The project end must end after the start");
            }
        }
    }
}
