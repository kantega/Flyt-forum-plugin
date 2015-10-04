package no.kantega.modules.user;

class AksessGroup implements Group {

    private final String groupId;
    private final String groupName;

    public AksessGroup(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    @Override
    public String getId() {
        return groupId;
    }

    @Override
    public String getName() {
        return groupName;
    }
}
