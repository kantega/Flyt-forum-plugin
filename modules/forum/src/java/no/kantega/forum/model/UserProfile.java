package no.kantega.forum.model;

/**
 * Created by IntelliJ IDEA.
 * User: HAREVE
 * Date: 12.des.2005
 * Time: 09:14:34
 * To change this template use File | Settings | File Templates.
 */
public class UserProfile {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobilePhone;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
