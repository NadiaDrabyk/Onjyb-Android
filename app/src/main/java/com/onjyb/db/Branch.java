package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Branch implements Serializable {
    String bName;
    String branchStatus;
    String branchid;
    String createDate;
    String id;
    String ref_company_id;
    String updateDate;

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("branch_id")
    public String getBranchid() {
        return this.branchid;
    }

    @JsonProperty("branch_id")
    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    @JsonProperty("branch_name")
    public String getBranchName() {
        return this.bName;
    }

    @JsonProperty("branch_name")
    public void setBranchName(String bName) {
        this.bName = bName;
    }

    @JsonProperty("branch_status")
    public String getBranchStatus() {
        return this.branchStatus;
    }

    @JsonProperty("branch_status")
    public void setBranchStatus(String branchStatus) {
        this.branchStatus = branchStatus;
    }

    @JsonProperty("ref_company_id")
    public String getRef_company_id() {
        return this.ref_company_id;
    }

    @JsonProperty("ref_company_id")
    public void setRef_company_id(String ref_company_id) {
        this.ref_company_id = ref_company_id;
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
}
