package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class AssociateService implements Serializable {
    String associateServiceId;
    String createDate;
    String id;
    String refAssociateId;
    String refServiceId;
    String serviceTime;
    String updateDate;

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("associate_service_id")
    public String getAssociateServiceId() {
        return this.associateServiceId;
    }

    @JsonProperty("associate_service_id")
    public void setAssociateServiceId(String associateServiceId) {
        this.associateServiceId = associateServiceId;
    }

    @JsonProperty("ref_associate_id")
    public String getRefAssociateId() {
        return this.refAssociateId;
    }

    @JsonProperty("ref_associate_id")
    public void setRefAssociateId(String refAssociateId) {
        this.refAssociateId = refAssociateId;
    }

    @JsonProperty("ref_service_id")
    public String getRefServiceId() {
        return this.refServiceId;
    }

    @JsonProperty("ref_service_id")
    public void setRefServiceId(String refServiceId) {
        this.refServiceId = refServiceId;
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
