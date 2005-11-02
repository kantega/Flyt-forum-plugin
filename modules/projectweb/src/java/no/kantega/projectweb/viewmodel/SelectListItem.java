package no.kantega.projectweb.viewmodel;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Sep 27, 2005
 * Time: 5:45:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectListItem {
    private String value;
    private String text;
    private boolean selected;

    public SelectListItem(String value, String text, boolean selected) {
        this.value = value;
        this.text = text;
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return selected;
    }
}
