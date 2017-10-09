package com.onjyb.beans;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class User implements Serializable {
    String address;
    String companyId;
    String email;
    String firstName;
    String lastName;
    String last_date;
    String mobile;
    String password;
    String profile_image;
    String userId;
    String user_RoleId;
    String user_status;

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLast_date() {
        return this.last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getUser_RoleId() {
        return this.user_RoleId;
    }

    public void setUser_RoleId(String user_RoleId) {
        this.user_RoleId = user_RoleId;
    }

    @JsonProperty("email")
    public String getEmail() {
        return this.email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return this.password;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public void setPassword(String psw) {
        this.password = psw;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return this.firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return this.lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("address")
    public String getAddress() {
        return this.address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("mobile")
    public String getMobile() {
        return this.mobile;
    }

    @JsonProperty("mobile")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @JsonProperty("profile_image")
    public String getProfile_image() {
        return this.profile_image;
    }

    @JsonProperty("profile_image")
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @JsonProperty("user_status")
    public String getUser_status() {
        return this.user_status;
    }

    @JsonProperty("user_status")
    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
