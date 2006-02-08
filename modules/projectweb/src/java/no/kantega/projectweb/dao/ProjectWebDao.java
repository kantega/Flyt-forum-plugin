package no.kantega.projectweb.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.apache.log4j.Logger;

import java.util.*;
import java.sql.SQLException;

import no.kantega.projectweb.model.*;
import no.kantega.projectweb.permission.scheme.PermissionScheme;
import no.kantega.projectweb.permission.scheme.PermissionEntry;
import no.kantega.projectweb.permission.PermissionInvalidationListener;
import no.kantega.projectweb.permission.PermissionInvalidator;

public class ProjectWebDao {

    private SessionFactory sessionFactory;

    private HibernateTemplate template;

    private Logger log = Logger.getLogger(ProjectWebDao.class);

    private PermissionInvalidator permissionInvalidator;

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    public List getProjectList() {
        return template.find("from Project order by name");
    }

    public void saveOrUpdate(Project project) {
        template.saveOrUpdate(project);
    }

    public void saveOrUpdate(Activity activity) {
            template.saveOrUpdate(activity);
        }


    public Project getPopulatedProject(final long projectId) {
        return (Project) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Project p = (Project) session.get(Project.class, new Long(projectId));
                p.getActivities().size();
                p.getDocuments().size();
                return p;
            }
        });
    }

    public Activity getPopulatedActivity(final long activityId) {
        return (Activity) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from Activity a inner join fetch a.type inner join fetch a.priority inner join fetch a.project left outer join fetch a.status left join fetch a.comments left outer join fetch a.projectPhase where a.id=:activityId");
                q.setLong("activityId", activityId);
                Object o = q.uniqueResult();
                log.info("O is: " +o);
                return o;
            }
        });
    }

    public Document getPopulatedDocument(final long documentId) {
        return (Document) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from Document d inner join fetch d.project left outer join fetch d.status where d.id=:documentId");
                q.setLong("documentId", documentId);
                Object o = q.uniqueResult();
                log.info("O is: " +o);
                return o;
            }
        });
    }


    public List getActivitiesInProject(final long projectId) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from Activity a inner join fetch a.type inner join fetch a.priority left outer join fetch a.status where a.project=:projectId");
                q.setLong("projectId", projectId);
                return q.list();
            }
        });
    }

    public List getDocumentsInProject(final long projectId) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from Document a inner join fetch a.type inner join fetch a.priority left outer join fetch a.status where a.project=:projectId");
                q.setLong("projectId", projectId);
                return q.list();
            }
        });
    }

    public Project getProject(long projectId) {
        return (Project) template.get(Project.class, new Long(projectId));
    }

    public void addActivityToProject(final long projectId, final Activity a) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Project project = (Project) session.get(Project.class, new Long(projectId));
                project.getActivities().add(a);
                a.setProject(project);
                session.saveOrUpdate(a);
                session.saveOrUpdate(project);
                return null;
            }
        });
    }

    public Activity getActivity(long activityId) {
        Activity activity = (Activity) template.get(Activity.class, new Long(activityId));
        return activity;
    }

    public Document getDocumentWithProject(final long documentId) {
           return (Document) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Document document = (Document) session.get(Document.class, new Long(documentId));
                document.getProject();
                return document;
            }
        });
    }

    public void addDocumentToProject(final long projectId, final Document doc) {
         template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Project project = (Project) session.get(Project.class, new Long(projectId));
                project.getDocuments().add(doc);
                doc.setProject(project);
                session.saveOrUpdate(doc);
                session.saveOrUpdate(project);
                return null;
            }
        });
    }

    public Document getDocument(long documentId) {
        return (Document) template.get(Document.class, new Long(documentId));
    }

    public void saveOrUpdate(Document document) {
        template.saveOrUpdate(document);
    }

    public List getActivityTypes() {
        return template.loadAll(ActivityType.class);
    }

    public ActivityType getActivityType(long activitTypeId) {
        return (ActivityType)template.get(ActivityType.class, new Long(activitTypeId));
    }

    public ActivityPriority getActivityPriority(long priorityId) {
        return (ActivityPriority) template.get(ActivityPriority.class, new Long(priorityId));
    }

    public List getActivityPriorities() {
        return template.loadAll(ActivityPriority.class);
    }

    public List getProjectParticipants(final long id) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query q = session.createQuery("from Participant p where p.project=:projectId order by p.user");
                q.setLong("projectId", id);
                List participants = q.list();
                for (int i = 0; i < participants.size(); i++) {
                    Participant participant = (Participant) participants.get(i);
                    participant.getRoles().size();
                }
                return participants;
            }
        });
    }
    public Participant getProjectParticipant(final long projectId, final String user) {
        return (Participant) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query q = session.createQuery("from Participant p where p.project.id=:projectId and p.user=:user");
                q.setLong("projectId", projectId);
                q.setString("user", user);
                List p = q.list();
                if(p.size() == 0) {
                    return null;
                } else {
                    Participant participant = (Participant) p.get(0);
                    participant.getRoles().size();
                    return participant;
                }
            }
        });
    }


    public void addParticipantToProject(final Participant participant, final Project project) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Project p = (Project) session.get(Project.class, new Long(project.getId()));
                participant.setProject(p);
                if(p.getParticipants() == null) {
                    p.setParticipants(new HashSet());
                }
                p.getParticipants().add(participant);
                session.saveOrUpdate(participant);
                session.saveOrUpdate(p);
                return null;
            }
        });
        if(permissionInvalidator != null) {
            permissionInvalidator.invalidate();
        }
    }

    public Participant getPopulatedParticipant(final long participantId) {
        return (Participant) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Participant p = (Participant) session.get(Participant.class, new Long(participantId));
                p.getProject().getName();
                p.getRoles().size();
                return p;
            }
        });
    }

    public ProjectRole[] getAllRoles() {
        List roles = template.loadAll(ProjectRole.class);
        return (ProjectRole[]) roles.toArray(new ProjectRole[0]);
    }

    public ProjectRole getRoleByCode(String code) {
        return (ProjectRole) template.find("from ProjectRole p where p.code=?", code).get(0);
    }

    public void saveOrUpdate(Participant p) {
        template.saveOrUpdate(p);
        if(permissionInvalidator != null) {
            permissionInvalidator.invalidate();
        }
    }



    public PermissionScheme getPopulatedPermissionScheme(final long permissionSchemeId) {
        return (PermissionScheme) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                PermissionScheme scheme = (PermissionScheme) session.get(PermissionScheme.class, new Long(permissionSchemeId));
                Set entries = scheme.getPermissionEntries();
                if(entries != null) {
                    Iterator it = entries.iterator();
                    while (it.hasNext()) {
                        PermissionEntry permissionEntry = (PermissionEntry) it.next();
                        permissionEntry.getRoles().size();
                    }
                } else {
                    log.warn("Permission scheme " + permissionSchemeId +" has no entries");
                }
                return scheme;
            }
        });
    }

    public ActivityStatus[] getActivityStatuses() {
        return (ActivityStatus[]) template.loadAll(ActivityStatus.class).toArray(new ActivityStatus[0]);
    }

    public ActivityStatus getActivityStatus(int activityStatusId) {
        return (ActivityStatus) template.get(ActivityStatus.class, new Long(activityStatusId));
    }

    public ActivityStatus getActivityStatusByCode(String status) {
        return (ActivityStatus) template.find("from ActivityStatus a where a.code=?", status).get(0);
    }

    public List getActivitiesInProject(final DetachedCriteria criteria) {

        Session session = null;
        try {
            session = template.getSessionFactory().openSession();
            Criteria exCriteria = criteria.getExecutableCriteria(session);
            log.info("getActivitiesInProject; Criteria: " + criteria.toString());
            exCriteria.setFetchMode("type", FetchMode.JOIN);
            exCriteria.setFetchMode("priority",FetchMode.JOIN);
            exCriteria.setFetchMode("status", FetchMode.JOIN);
            exCriteria.setFetchMode("projectPhase", FetchMode.JOIN);
            return exCriteria.list();
        } finally {
            session.close();
        }
    }

    public List getDocumentsInProject(final DetachedCriteria criteria) {
        Session session = null;
        try {
            session = template.getSessionFactory().openSession();
            Criteria exCriteria = criteria.getExecutableCriteria(session);
            log.info("getDocumentsInProject; Criteria: " + criteria.toString());
            exCriteria.setFetchMode("status", FetchMode.JOIN);
            return exCriteria.list();
        } finally {
            session.close();
        }
    }

    public List getProjectListForUser(final String user) {
        return (List) template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query c = session.createQuery("from Project p where p.publicProject=? or p.id in (select pp.project.id from Participant pp where pp.user=?) order by p.name");
                c.setBoolean(0, true);
                c.setString(1, user);
                return c.list();
            }
        });
    }

    public Participant getProjectParticipant(long participantId) {
        return (Participant) template.get(Participant.class, new Long(participantId));
    }

    public void removeParticipantFromProject(final long participantId, final long projectId) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Participant participant = (Participant)session.get(Participant.class, new Long(participantId));
                session.delete(participant);
                return null;
            }
        });
        if(permissionInvalidator != null) {
            permissionInvalidator.invalidate();
        }
    }

    public void addActivityComment(final ActivityComment comment, final long activityId) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Activity activity = (Activity) session.get(Activity.class, new Long(activityId));
                activity.getComments().add(comment);
                comment.setActivity(activity);
                session.saveOrUpdate(comment);
                session.saveOrUpdate(activity);
                return null;
            }
        });
    }

    public boolean isUserInGroup(String user, String role) {
        log.debug("Is user in group? " +user +"," +role);
        return template.find("from GroupMembership m where m.user=? and m.group.name=?", new String[] {user, role}).size() > 0;
    }

    public GroupMembership[] getMembersInGroup(String administratorGroup) {
        return (GroupMembership[]) template.find("from GroupMembership m where m.group.name=? order by m.user", administratorGroup).toArray(new GroupMembership[0]);

    }

    public void removeUserFromGroup(final String user, final String administratorGroup) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from GroupMembership gm where gm.user=? and gm.group.name=?");
                query.setString(0, user);
                query.setString(1, administratorGroup);
                GroupMembership gm = (GroupMembership) query.uniqueResult();
                session.delete(gm);
                return null;
            }
        });
        if(permissionInvalidator != null) {
            permissionInvalidator.invalidate();
        }
    }

    public void addUserToGroup(final String userName, final String administratorGroup) {
        template.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from Group g where g.name=?");
                query.setString(0, administratorGroup);
                Group group = (Group) query.uniqueResult();
                GroupMembership member = new GroupMembership();
                member.setUser(userName);
                member.setGroup(group);
                session.saveOrUpdate(member);
                return null;
            }
        });
        if(permissionInvalidator != null) {
            permissionInvalidator.invalidate();
        }
    }

    public List getProjectPhases() {
        return template.loadAll(ProjectPhase.class);
    }

    public ProjectPhase getProjectPhase(long projectPhaseId) {
        return (ProjectPhase) template.get(ProjectPhase.class, new Long(projectPhaseId));
    }

    public void setPermissionInvalidator(PermissionInvalidator permissionInvalidator) {
        this.permissionInvalidator = permissionInvalidator;
    }
}
