package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class Employee implements Serializable {
    String employeeId;
    String firstName;
    String lastName;

    @JsonProperty("employee_id")
    public String getEmployeeId() {
        return this.employeeId;
    }

    @JsonProperty("employee_id")
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
}
