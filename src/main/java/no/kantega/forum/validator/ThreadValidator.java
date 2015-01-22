package no.kantega.forum.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.forum.model.ForumThread;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 13:44:04
 * To change this template use File | Settings | File Templates.
 */
public class ThreadValidator implements Validator {
    public boolean supports(Class aClass) {
         return aClass == ForumThread.class;
    }

    public void validate(Object object, Errors errors) {
    }
}
