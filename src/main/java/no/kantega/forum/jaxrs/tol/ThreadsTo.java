package no.kantega.forum.jaxrs.tol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
@XmlType(name = "Treads")
@XmlRootElement(name = "threads")
@XmlAccessorType(XmlAccessType.NONE)
public class ThreadsTo  {

    @XmlElementWrapper(name = "threads")
    @XmlElement(name = "thread")
    private List<ThreadTo> threads;

    @XmlElementWrapper(name = "actions")
    @XmlElement(name = "action")
    private List<ResourceReferenceTo> actions;

    public ThreadsTo() {}

    public ThreadsTo(List<ThreadTo> threads, List<ResourceReferenceTo> actions) {
        this.threads = threads;
        this.actions = actions;
    }

    public List<ThreadTo> getThreads() {
        return threads;
    }

    public void setThreads(List<ThreadTo> threads) {
        this.threads = threads;
    }

    public List<ResourceReferenceTo> getActions() {
        return actions;
    }

    public void setActions(List<ResourceReferenceTo> actions) {
        this.actions = actions;
    }
}

