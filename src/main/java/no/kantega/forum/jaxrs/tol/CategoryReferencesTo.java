package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlType(name = "CategoryReferences")
@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso({CategoryReferenceTo.class})
public class CategoryReferencesTo extends ArrayList<CategoryReferenceTo> {

    public CategoryReferencesTo(int initialCapacity) {
        super(initialCapacity);
    }

    public CategoryReferencesTo() {
    }

    public CategoryReferencesTo(Collection c) {
        super(c);
    }

    @XmlElement(name = "category")
    public List<CategoryReferenceTo> getUsers() {
        return this;
    }
}

