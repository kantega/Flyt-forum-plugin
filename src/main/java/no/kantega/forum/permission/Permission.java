package no.kantega.forum.permission;

public enum Permission {
    EDIT_CATEGORY(0),

    EDIT_FORUM(1),

    EDIT_THREAD(2),

    POST_IN_THREAD(3),

    EDIT_POST(4),

    DELETE_POST(5),

    DELETE_THREAD(6),

    ADD_THREAD(7),

    ATTACH_FILE(8),

    APPROVE_POST(9),

    VIEW(10);

    public final int permissionId;

    Permission(int permissionId) {
        this.permissionId = permissionId;
    }
}
