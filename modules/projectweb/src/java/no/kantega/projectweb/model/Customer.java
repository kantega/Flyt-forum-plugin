package no.kantega.porjectweb.model;

/**
 * Created by IntelliJ IDEA.
 * User: runmoe
 * Date: 28.jul.2005
 * Time: 08:34:36
 * To change this template use File | Settings | File Templates.
 */

 public class Customer{
    private long id;
    private String companyName;
    private String address;
    private String postalCode;
    private String place;
    // Hvis kundebrukere skal være egen type og ikke user.
    // private CustomerUserList userList;

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getCompanyName(){
        return this.companyName;
    }

    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    public String getPostalCode(){
        return this.postalCode;
    }

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public String getPlace(){
        return this.place;
    }

    public void setPlace(String place){
        this.place = place;
    }
}