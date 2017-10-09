package com.onjyb.db;

import org.codehaus.jackson.annotate.JsonProperty;

public class GroupChat {
    String createDate;
    String currentDate;
    String currentTime;
    String firstName;
    String id;
    String lastName;
    String message;
    String profilePic;
    String ref_user_id;
    String senderName;
    String status;
    String type;

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("message")
    public String getMessage() {
        return this.message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @JsonProperty("profile_image")
    public String getProfilePic() {
        return this.profilePic;
    }

    @JsonProperty("profile_image")
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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

    @JsonProperty("ref_user_id")
    public String getRef_user_id() {
        return this.ref_user_id;
    }

    @JsonProperty("ref_user_id")
    public void setRef_user_id(String ref_user_id) {
        this.ref_user_id = ref_user_id;
    }

    @JsonProperty("status")
    public String getStatus() {
        return this.status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("create_date")
    public String getCreateDate() {
        return this.createDate;
    }

    @JsonProperty("create_date")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
