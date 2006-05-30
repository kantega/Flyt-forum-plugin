package no.kantega.forum.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.forum.model.Post;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 20.des.2005
 * Time: 14:16:47
 * To change this template use File | Settings | File Templates.
 */
public class PostValidator implements Validator {
    public boolean supports(Class aClass) {
        return aClass == Post.class;
    }

    public void validate(Object object, Errors errors) {
        Post post = (Post) object;
        if(post.getSubject().trim().length() ==0) {
            errors.rejectValue("subject", "post.subject.too-short", "Subject too short");
        }
        if(post.getOwner() == null) {
            if(post.getAuthor() == null || post.getAuthor().trim().length() == 0) {
                errors.rejectValue("author", "post.author.too-short", "Name must be filled out");
            }
        }
    }
}
