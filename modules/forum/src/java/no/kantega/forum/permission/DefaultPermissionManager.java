package no.kantega.forum.permission;

import no.kantega.commons.exception.ConfigurationException;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.modules.user.GroupResolver;
import no.kantega.publishing.common.Aksess;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class  DefaultPermissionManager implements PermissionManager {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    private Field[] permissionFields = Permissions.class.getFields();

    private GroupResolver groupResolver;

    private List administratorGroups;

    public boolean hasPermission(String user, long permission, Object object) {
        return getPermission(user, permission, object);
    }


    private boolean getPermission(String user, long permission, Object object) {
        boolean isAdmin = false;

        // Sjekk om bruker er forumadministrator
        if(user != null && !user.trim().equals("")) {
            for (int i = 0; i < administratorGroups.size(); i++) {
                String group = (String) administratorGroups.get(i);
                if(groupResolver.isInGroup(user, group)) {
                    isAdmin = true;
                    return isAdmin;
                }
            }
        }

        if (object != null) {
            // Tillatt posting kun for registrerte brukere og i �pne forum
            if (permission == Permissions.POST_IN_THREAD && object instanceof ForumThread) {
                ForumThread thread = (ForumThread)object;
                if (thread.getForum().isAnonymousPostAllowed() || user != null) {
                    return true;
                }
            }

            if((permission == Permissions.ADD_THREAD || permission == Permissions.EDIT_THREAD) && object instanceof Forum) {
                Forum forum = (Forum)object;
                if (forum.isAnonymousPostAllowed() || user != null) {
                    return true;
                }
            }

            if (permission == Permissions.VIEW) {
                Forum forum = null;
                if (object instanceof Forum) {
                    forum = (Forum)object;
                } else if (object instanceof ForumThread) {
                    forum = ((ForumThread)object).getForum();
                } else if (object instanceof Post) {
                    forum = ((Post)object).getThread().getForum();
                }
                if (forum != null) {
                    boolean isAuthorized = false;
                    Set groups = forum.getGroups();
                    if (groups == null || groups.isEmpty()) {
                        isAuthorized = true;
                    } else {
                        Iterator it = groups.iterator();
                        while (it.hasNext()) {
                            String groupId = (String)it.next();
                            if (groupResolver.isInGroup(user, groupId)) {
                                isAuthorized = true;
                                break;
                            }
                        }

                        if (!isAuthorized) {
                            // Forum-moderator skal alltid ha tilgang
                            if (user != null && user.equals(forum.getModerator())) {
                                isAuthorized = true;
                            }
                        }

                    }

                    return isAdmin | isAuthorized;
                }

            }

            if (object instanceof Post) {
                Post post = (Post)object;

                // Moderator kan gj�re alt
                Forum forum = post.getThread().getForum();
                if (user != null && user.equals(forum.getModerator())) {
                    return true;
                }

                // Folk kan redigere egne poster
                if(permission == Permissions.EDIT_POST) {
                    return user != null && user.equals(post.getOwner());
                }

                // Folk kan slette egne innlegg hvis config tillater dette. Default vil dette ikke være tillatt.
                boolean canDeleteOwnPost = Aksess.getConfiguration().getBoolean("forum.permission.user.deleteownpost", false);
                if(permission == Permissions.DELETE_POST && canDeleteOwnPost) {
                    return user != null && user.equals(post.getOwner());
                }

                // Bare moderatorer kan moderere
                if(permission == Permissions.APPROVE_POST) {
                    if (!forum.isApprovalRequired()) {
                        return true;
                    }
                }

                // Legge inn vedlegg
                if (user != null && permission == Permissions.ATTACH_FILE) {
                    ForumThread thread = post.getThread();
                    return thread.getForum().isAttachmentsAllowed();
                }
            } else if (object instanceof ForumThread) {

                // Folk kan slette egne threads hvis config tillater dette. Default vil dette ikke være tillatt.
                ForumThread thread = (ForumThread)object;
                boolean canDeleteOwnThread = Aksess.getConfiguration().getBoolean("forum.permission.user.deleteownthread", false);
                if(permission == Permissions.DELETE_THREAD && canDeleteOwnThread) {
                    return user != null && user.equals(thread.getOwner());
                }

            }
        }

        return isAdmin;
    }

    private String getPermission(long value) {
        try {
            for (int i = 0; i < permissionFields.length; i++) {
                Field permissionField = permissionFields[i];
                if(permissionField.getLong(null) == value) {
                    return permissionField.getName();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return "Unknown field " + value;
    }

    public void setGroupResolver(GroupResolver groupResolver) {
        this.groupResolver = groupResolver;
    }

    public void setAdministratorGroups(String administratorGroups) {
        this.administratorGroups = Arrays.asList(administratorGroups.split(","));
    }

}
