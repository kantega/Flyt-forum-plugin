package no.kantega.modules.user;


public interface GroupResolver {
    boolean isInGroup(String user, String group);
}
