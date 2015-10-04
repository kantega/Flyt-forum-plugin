package no.kantega.modules.user;

class AksessUserProfile implements UserProfile {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final String source;

    public AksessUserProfile(String id, String name, String email, String phone, String source) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.source = source;
    }

    @Override
    public String getUser() {
        return id;
    }

    @Override
    public String getFullName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getSource() {
        return source;
    }
}
