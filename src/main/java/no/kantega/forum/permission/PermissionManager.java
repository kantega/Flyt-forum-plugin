package no.kantega.forum.permission;

import no.kantega.forum.jaxrs.bol.ForumBo;
import no.kantega.forum.jaxrs.bol.GroupDo;

import java.util.List;

public interface PermissionManager {
    boolean hasPermission(String user, Permission permission, Object object);
    boolean hasPermission(String user, Permission permission, ForumBo forumBo, List<GroupDo> groups);
}
