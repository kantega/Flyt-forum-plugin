package no.kantega.forum.jaxrs.tol;

import no.kantega.publishing.api.rating.Rating;
import no.kantega.publishing.security.data.User;
import org.joda.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Kristian Myrhaug
 * @since 2015-06-28
 */
@XmlType(name = "Like")
@XmlRootElement(name = "like")
@XmlAccessorType(XmlAccessType.NONE)
public class LikeTo {

    @XmlElement(name = "rating")
    private Integer rating;
    @XmlElement(name = "context")
    private String context;
    @XmlElement(name = "userid")
    private String userid;
    @XmlElement(name = "objectId")
    private String objectId;
    @XmlElement(name = "date")
    private Instant date;
    @XmlElement(name = "comment")
    private String comment;
    @XmlElement(name = "userFullName")
    private String userFullName;

    public LikeTo() {
    }

    public LikeTo(Rating rating, User user) {
        this.rating = rating.getRating();
        this.context = rating.getContext();
        this.userid = rating.getUserid();
        this.objectId = rating.getObjectId();
        this.date = rating.getDate() != null ? new Instant(rating.getDate().getTime()) : null;
        this.comment = rating.getComment();
        this.userFullName = user.getName();
    }

    public LikeTo(Integer rating, String context, String userid, String objectId, Instant date, String comment, String userFullName) {
        this.rating = rating;
        this.context = context;
        this.userid = userid;
        this.objectId = objectId;
        this.date = date;
        this.comment = comment;
        this.userFullName = userFullName;
    }
}
