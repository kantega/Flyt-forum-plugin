package no.kantega.projectweb.propertyeditors;

import no.kantega.projectweb.dao.ProjectWebDao;
import no.kantega.projectweb.model.ActivityPriority;
import no.kantega.projectweb.model.DocumentCategory;

import java.beans.PropertyEditorSupport;


public class DocumentCategoryEditor extends PropertyEditorSupport {

    private ProjectWebDao dao;


    public DocumentCategoryEditor(ProjectWebDao dao) {
        this.dao = dao;
    }

    public void setAsText(String s) throws IllegalArgumentException {
        System.out.println("setastext: " + s);
        long categoryId;

        try {
            categoryId = Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        if(categoryId == -1) {
            setValue(null);
        } else {
            DocumentCategory category = dao.getDocumentCategory(categoryId);
            if(category == null) {
                throw new IllegalArgumentException("No such category: " + categoryId);
            }
            setValue(category);
        }
    }

    public String getAsText() {
        if(getValue() == null) {
            return "-1";
        }
        return new Long(((DocumentCategory)getValue()).getId()).toString();

    }

    public void setDao(ProjectWebDao dao) {
        this.dao = dao;
    }
}
