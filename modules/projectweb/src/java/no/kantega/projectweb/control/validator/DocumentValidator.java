package no.kantega.projectweb.control.validator;

import org.springframework.validation.Validator;
import org.springframework.validation.Errors;
import no.kantega.projectweb.model.Activity;
import no.kantega.projectweb.model.Document;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 11.sep.2005
 * Time: 14:16:52
 * To change this template use File | Settings | File Templates.
 */
public class DocumentValidator implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals(Document.class);
    }


    public void validate(Object o, Errors errors) {
        Document document = (Document) o;
        if (document.getTitle().length() < 3) {
            errors.rejectValue("title", "title.too-short", "Title to short");
        }
        if (document.getDocumentContent() == null) {
            errors.rejectValue("documentContent", "documentContent.noFile", "File must be supplied");
        }
    }
}
