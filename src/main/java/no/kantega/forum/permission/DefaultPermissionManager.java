package no.kantega.forum.permission;

import no.kantega.forum.jaxrs.bol.ForumBo;
import no.kantega.forum.jaxrs.bol.GroupDo;
import no.kantega.forum.model.Forum;
import no.kantega.forum.model.ForumThread;
import no.kantega.forum.model.Post;
import no.kantega.modules.user.GroupResolver;
import no.kantega.publishing.api.configuration.SystemConfiguration;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static no.kantega.utilities.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class DefaultPermissionManager implements PermissionManager {

    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    private GroupResolver groupResolver;

    private List<String> administratorGroups;

    @Autowired
    private SystemConfiguration configuration;

    public boolean hasPermission(String user, Permission permission, Object object) {
        return getPermission(user, permission, object);
    }

    @Override
    public boolean hasPermission(String user, Permission permission, ForumBo forumBo, List<GroupDo> groupBos) {
        boolean isAdmin = false;

        // Sjekk om bruker er forumadministrator
        if(isNotBlank(user)) {
            for (String group : administratorGroups) {
                if (groupResolver.isInGroup(user, group)) {
                    isAdmin = true;
                    return isAdmin;
                }
            }
        }
        if (nonNull(forumBo)) {
            if((permission == Permission.ADD_THREAD || permission == Permission.EDIT_THREAD)) {
                if (forumBo.getAnonymousPostAllowed() || isAuthorizedInForum(user, forumBo, groupBos)) {
                    return true;
                }
            }
            if (permission == Permission.VIEW) {
                return isAuthorizedInForum(user, forumBo, groupBos);

            }
        }
        return isAdmin;
    }


    private boolean getPermission(String user, Permission permission, Object object) {
        boolean isAdmin = false;

        // Sjekk om bruker er forumadministrator
        if(isNotBlank(user)) {
            for (String group : administratorGroups) {
                if (groupResolver.isInGroup(user, group)) {
                    isAdmin = true;
                    return isAdmin;
                }
            }
        }

        if (object != null) {
            // Tillatt posting kun for registrerte brukere og i �pne forum
            if (permission == Permission.POST_IN_THREAD && object instanceof ForumThread) {
                ForumThread thread = (ForumThread)object;
                if (thread.getForum().isAnonymousPostAllowed()  || isAuthorizedInForum(user, thread.getForum())) {
                    return true;
                }
            }

            if((permission == Permission.ADD_THREAD || permission == Permission.EDIT_THREAD) && object instanceof Forum) {
                Forum forum = (Forum)object;
                if (forum.isAnonymousPostAllowed() || isAuthorizedInForum(user, forum)) {
                    return true;
                }
            }

            if (permission == Permission.VIEW) {
                Forum forum = null;
                if (object instanceof Forum) {
                    forum = (Forum)object;
                } else if (object instanceof ForumThread) {
                    forum = ((ForumThread)object).getForum();
                } else if (object instanceof Post) {
                    forum = ((Post)object).getThread().getForum();
                }
                if (forum != null) {
                    return isAuthorizedInForum(user, forum);
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
                if(permission == Permission.EDIT_POST) {
                    return user != null && user.equals(post.getOwner());
                }

                // Folk kan slette egne innlegg hvis config tillater dette. Default vil dette ikke være tillatt.
                boolean canDeleteOwnPost = configuration.getBoolean("forum.permission.user.deleteownpost", false);
                if(permission == Permission.DELETE_POST && canDeleteOwnPost) {
                    return user != null && user.equals(post.getOwner());
                }

                // Bare moderatorer kan moderere
                if(permission == Permission.APPROVE_POST) {
                    if (!forum.isApprovalRequired()) {
                        return true;
                    }
                }

                // Legge inn vedlegg
                if (user != null && permission == Permission.ATTACH_FILE) {
                    ForumThread thread = post.getThread();
                    return thread.getForum().isAttachmentsAllowed();
                }
            } else if (object instanceof ForumThread) {

                // Folk kan slette egne threads hvis config tillater dette. Default vil dette ikke være tillatt.
                ForumThread thread = (ForumThread)object;
                boolean canDeleteOwnThread = configuration.getBoolean("forum.permission.user.deleteownthread", false);
                if(permission == Permission.DELETE_THREAD && canDeleteOwnThread) {
                    return user != null && user.equals(thread.getOwner());
                }

            }
        }

        return isAdmin;
    }

    private boolean isAuthorizedInForum(String user, Forum forum) {
        boolean isAuthorized = false;
        Set<String> groups = forum.getGroups();
        if (groups == null || groups.isEmpty()) {
            isAuthorized = true;
        } else {
            for (String groupId : groups) {
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
        return isAuthorized;
    }



    private boolean isAuthorizedInForum(String user, ForumBo forumBo, List<GroupDo> groupDos) {
        boolean isAuthorized = false;
        if (groupDos == null || groupDos.isEmpty()) {
            isAuthorized = true;
        } else {
            for (GroupDo groupDo : groupDos) {
                if (groupResolver.isInGroup(user, groupDo.getId())) {
                    isAuthorized = true;
                    break;
                }
            }

            if (!isAuthorized) {
                // Forum-moderator skal alltid ha tilgang
                if (user != null && user.equals(forumBo.getModerator())) {
                    isAuthorized = true;
                }
            }

        }
        return isAuthorized;
    }

    public void setGroupResolver(GroupResolver groupResolver) {
        this.groupResolver = groupResolver;
    }

    public void setAdministratorGroups(String administratorGroups) {
        this.administratorGroups = Arrays.asList(administratorGroups.split(","));
    }

}
