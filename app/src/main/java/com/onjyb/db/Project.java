package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Project implements Serializable {
    String createDate;
    String id;
    String proDetailid;
    String proName;
    String projectStatus;
    String project_Number;
    String ref_company_id;
    String ref_manager_id;
    String updateDate;

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("project_id")
    public String getproDetailId() {
        return this.proDetailid;
    }

    @JsonProperty("project_id")
    public void setproDetailId(String proDetailid) {
        this.proDetailid = proDetailid;
    }

    @JsonProperty("ref_company_id")
    public String getRef_company_id() {
        return this.ref_company_id;
    }

    @JsonProperty("ref_company_id")
    public void setRef_company_id(String ref_company_id) {
        this.ref_company_id = ref_company_id;
    }

    @JsonProperty("project_name")
    public String getProName() {
        return this.proName;
    }

    @JsonProperty("project_name")
    public void setpname(String proName) {
        this.proName = proName;
    }

    @JsonProperty("project_status")
    public String getProjectStatus() {
        return this.projectStatus;
    }

    @JsonProperty("project_status")
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    @JsonProperty("create_date")
    public String getCreateDate() {
        return this.createDate;
    }

    @JsonProperty("create_date")
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @JsonProperty("update_date")
    public String getUpdateDate() {
        return this.updateDate;
    }

    @JsonProperty("update_date")
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @JsonProperty("ref_manager_id")
    public String getRef_manager_id() {
        return this.ref_manager_id;
    }

    @JsonProperty("ref_manager_id")
    public void setRef_manager_id(String ref_manager_id) {
        this.ref_manager_id = ref_manager_id;
    }

    @JsonProperty("project_number")
    public String getProject_Number() {
        return this.project_Number;
    }

    @JsonProperty("project_number")
    public void setProject_Number(String project_Number) {
        this.project_Number = project_Number;
    }
}
