package no.kantega.modules.user;

import java.util.List;

public interface UserSearcher {
    List<UserProfile> findUsers(String substring);
}
