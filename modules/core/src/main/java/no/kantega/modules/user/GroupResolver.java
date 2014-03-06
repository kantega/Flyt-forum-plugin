package no.kantega.modules.user;


public interface GroupResolver {
    public boolean isInGroup(String user, String group);
}
