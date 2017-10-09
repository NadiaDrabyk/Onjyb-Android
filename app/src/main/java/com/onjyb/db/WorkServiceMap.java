package com.onjyb.db;

import java.io.Serializable;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonProperty;

public class WorkServiceMap implements Serializable {
    ArrayList<AttachmentMap> attachementToDelete;
    ArrayList<AttachmentMap> attachmentList = new ArrayList();
    String baseService;
    int imgCount;
    String refServiceId;
    String refServiceName;
    String serviceTime;
    String service_comments;

    public String getServiceTime() {
        return this.serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    @JsonProperty("ref_service_id")
    public String getRefServiceId() {
        return this.refServiceId;
    }

    @JsonProperty("ref_service_id")
    public void setRefServiceId(String refServiceId) {
        this.refServiceId = refServiceId;
    }

    @JsonProperty("service_time")
    public String getRefServiceName() {
        return this.refServiceName;
    }

    @JsonProperty("service_time")
    public void setRefServiceName(String refServiceName) {
        this.refServiceName = refServiceName;
    }

    public int getImgCount() {
        return this.imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }

    public void setAttachmentList(ArrayList<AttachmentMap> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @JsonProperty("service_comment")
    public String getService_comments() {
        return this.service_comments;
    }

    @JsonProperty("service_comment")
    public void setService_comments(String service_comments) {
        this.service_comments = service_comments;
    }

    public ArrayList<AttachmentMap> getAttachmentList() {
        return this.attachmentList;
    }

    public void setAttachementToDelete(ArrayList<AttachmentMap> attachementToDelete) {
        this.attachementToDelete = attachementToDelete;
    }

    public ArrayList<AttachmentMap> getAttachementToDelete() {
        return this.attachementToDelete;
    }

    public String getBaseService() {
        return this.baseService;
    }

    public void setBaseService(String baseService) {
        this.baseService = baseService;
    }
}
