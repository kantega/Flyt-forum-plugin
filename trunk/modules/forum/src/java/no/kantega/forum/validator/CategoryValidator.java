package no.kantega.forum.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.forum.model.ForumCategory;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 13.des.2005
 * Time: 13:05:33
 * To change this template use File | Settings | File Templates.
 */
public class CategoryValidator implements Validator {
    public boolean supports(Class aClass) {
        return aClass == ForumCategory.class;
    }

    public void validate(Object object, Errors errors) {

    }
}
