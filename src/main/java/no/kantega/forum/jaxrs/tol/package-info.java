/**
 * @author Kristian Myrhaug
 * @since 2015-06-24
 */
@XmlSchema(
        xmlns = {
                @XmlNs(
                        prefix = "tol",
                        namespaceURI = "urn:tol.jaxrs.forum.kantega.no"
                )
        },
        namespace = "urn:tol.jaxrs.forum.kantega.no",
        elementFormDefault = XmlNsForm.QUALIFIED
)
package no.kantega.forum.jaxrs.tol;

        import javax.xml.bind.annotation.XmlNs;
        import javax.xml.bind.annotation.XmlNsForm;
        import javax.xml.bind.annotation.XmlSchema;
