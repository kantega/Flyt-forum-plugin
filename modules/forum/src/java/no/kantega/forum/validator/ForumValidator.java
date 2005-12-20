package no.kantega.forum.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.forum.model.Forum;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 09:48:16
 * To change this template use File | Settings | File Templates.
 */
public class ForumValidator implements Validator {
    public boolean supports(Class aClass) {
        return aClass == Forum.class;
    }

    public void validate(Object object, Errors errors) {
    }
}
