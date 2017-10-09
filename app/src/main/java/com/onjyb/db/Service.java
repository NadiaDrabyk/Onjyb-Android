package com.onjyb.db;

import java.io.Serializable;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service implements Serializable {
    ArrayList<AttachmentMap> attachmentList = new ArrayList();
    String createDate;
    String extraServiceComment;
    String id;
    String refServiceId;
    String refWorksheetId;
    String ref_company_id;
    String serviceName;
    String serviceStatus;
    String serviceTime;
    String updateDate;

    @JsonProperty("service_id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("service_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("ref_service_id")
    public String getRefServiceId() {
        return this.refServiceId;
    }

    @JsonProperty("ref_service_id")
    public void setRefServiceId(String refServiceId) {
        this.refServiceId = refServiceId;
    }

    @JsonProperty("ref_company_id")
    public String getRef_company_id() {
        return this.ref_company_id;
    }

    @JsonProperty("ref_company_id")
    public void setRef_company_id(String ref_company_id) {
        this.ref_company_id = ref_company_id;
    }

    @JsonProperty("service_name")
    public String getServiceName() {
        return this.serviceName;
    }

    @JsonProperty("service_name")
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @JsonProperty("service_status")
    public String getServiceStatus() {
        return this.serviceStatus;
    }

    @JsonProperty("service_status")
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
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

    public String getRefWorksheetId() {
        return this.refWorksheetId;
    }

    public void setRefWorksheetId(String refWorksheetId) {
        this.refWorksheetId = refWorksheetId;
    }

    @JsonProperty("service_time")
    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    @JsonProperty("service_time")
    public String getServiceTime() {
        return this.serviceTime;
    }

    @JsonProperty("extra_images")
    public void setAttachmentList(ArrayList<AttachmentMap> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @JsonProperty("extra_images")
    public ArrayList<AttachmentMap> getAttachmentList() {
        return this.attachmentList;
    }

    @JsonProperty("service_comment")
    public String getExtraServiceComment() {
        return this.extraServiceComment;
    }

    @JsonProperty("service_comment")
    public void setExtraServiceComment(String extraServiceComment) {
        this.extraServiceComment = extraServiceComment;
    }
}
