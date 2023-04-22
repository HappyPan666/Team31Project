package edu.northeastern.team31project;

public class final_user {
    String uid;
    String firstName;
    String lastName;
    String userName;
    String phoneNumber;
    String height;
    String weight;
    String email;

    public final_user() {
    }

    public final_user(String uid, String firstName, String lastName, String userName, String phoneNumber, String height, String weight, String email) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.height = height;
        this.weight = weight;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
