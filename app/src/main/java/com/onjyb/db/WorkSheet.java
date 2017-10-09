package com.onjyb.db;

import java.io.Serializable;
import java.util.ArrayList;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkSheet implements Serializable {
    String address;
    String approveById;
    String approveStatus;
    ArrayList<AttachmentMap> attachementList;
    ArrayList<AttachmentMap> attachementToDelete;
    String breakTime;
    String changeKM;
    String changeOverTime;
    String changeWorkhrs;
    String comments;
    String companyId;
    String createDate;
    String firstName;
    String getWorkOverTime2;
    String isEditable;
    String isworksheetEditmode;
    String kMDrive;
    String lastName;
    String overTime1EndTime;
    String overTime1StartTime;
    String overTime2EndTime;
    String overTime2StartTime;
    ArrayList<OvertimeRule> overtimeObjectsArray;
    ArrayList<OvertimeRule> overtimeRuleArrayList = new ArrayList();
    String profile_image;
    String projectName;
    String project_Number;
    String refBranchId;
    String refProjectId;
    String refRoleId;
    String refServiceId;
    String refUserId;
    String rejectComments;
    String serverWorkSheetId;
    ArrayList<Service> serviceObjectsArray;
    String syncStatus;
    String totalWorkTime;
    String total_work_overtime;
    String updatedate;
    String workDate;
    String workEndDate;
    String workEndTime;
    String workHrs;
    String workOverTime1;
    String workStatrTime;
    String worksheetId;
    String worksheetStatus;

    //jin
    String project_id;
    @JsonProperty("project_id")
    public String getProject_id() {
        return this.project_id;
    }

    @JsonProperty("project_id")
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    String is_ptobank;
    @JsonProperty("is_ptobank")
    public String getIs_ptobank() {
        return this.is_ptobank;
    }

    @JsonProperty("is_ptobank")
    public void setIs_ptobank(String is_ptobank) {
        this.is_ptobank = is_ptobank;
    }
    //end

    public String getWorksheetId() {
        return this.worksheetId;
    }

    public void setWorksheetId(String worksheetId) {
        this.worksheetId = worksheetId;
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

    public String getRefRoleId() {
        return this.refRoleId;
    }

    public void setRefRoleId(String refRoleId) {
        this.refRoleId = refRoleId;
    }

    @JsonProperty("ref_user_id")
    public String getRefUserId() {
        return this.refUserId;
    }

    @JsonProperty("ref_user_id")
    public void setRefUserId(String refUserId) {
        this.refUserId = refUserId;
    }

    //jin
    @JsonProperty("ref_project_id") //project_id
    public String getRefProjectId() {
        return this.refProjectId;
    }

    @JsonProperty("ref_project_id") //project_id
    public void setRefProjectId(String refProjectId) {
        this.refProjectId = refProjectId;
    }

    @JsonProperty("project_name")
    public String getProjectName() {
        return this.projectName;
    }

    @JsonProperty("project_name")
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @JsonProperty("ref_branch_id")
    public String getRefBranchId() {
        return this.refBranchId;
    }

    @JsonProperty("ref_branch_id")
    public void setRefBranchId(String refBranchId) {
        this.refBranchId = refBranchId;
    }

    @JsonProperty("ref_service_id")
    public String getRefServiceId() {
        return this.refServiceId;
    }

    @JsonProperty("ref_service_id")
    public void setRefServiceId(String refServiceId) {
        this.refServiceId = refServiceId;
    }

    @JsonProperty("reject_comments")
    public String getRejectComments() {
        return this.rejectComments;
    }

    @JsonProperty("reject_comments")
    public void setRejectComments(String rejectComments) {
        this.rejectComments = rejectComments;
    }

    @JsonProperty("approve_by_id")
    public String getApproveById() {
        return this.approveById;
    }

    @JsonProperty("approve_by_id")
    public void setApproveById(String approveById) {
        this.approveById = approveById;
    }

    @JsonProperty("work_hours")
    public String getWorkHrs() {
        return this.workHrs;
    }

    @JsonProperty("work_hours")
    public void setWorkHrs(String workHrs) {
        this.workHrs = workHrs;
    }

    @JsonProperty("work_date")
    public String getWorkDate() {
        return this.workDate;
    }

    @JsonProperty("work_date")
    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    @JsonProperty("km_drive")
    public String getkMDrive() {
        return this.kMDrive;
    }

    @JsonProperty("km_drive")
    public void setkMDrive(String kMDrive) {
        this.kMDrive = kMDrive;
    }

    @JsonProperty("work_overtime1")
    public String getWorkOverTime1() {
        return this.workOverTime1;
    }

    @JsonProperty("work_overtime1")
    public void setWorkOverTime1(String workOverTime1) {
        this.workOverTime1 = workOverTime1;
    }

    @JsonProperty("work_overtime2")
    public String getWorkOverTime2() {
        return this.getWorkOverTime2;
    }

    @JsonProperty("work_overtime2")
    public void setWorkOverTime2(String getWorkOverTime2) {
        this.getWorkOverTime2 = getWorkOverTime2;
    }

    @JsonProperty("break_time")
    public String getBreakTime() {
        return this.breakTime;
    }

    @JsonProperty("break_time")
    public void setBreakTime(String breakTime) {
        this.breakTime = breakTime;
    }

    @JsonProperty("total_work_time")
    public String getTotalWorkTime() {
        return this.totalWorkTime;
    }

    @JsonProperty("total_work_time")
    public void setTotalWorkTime(String totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    @JsonProperty("comments")
    public String getComments() {
        return this.comments;
    }

    @JsonProperty("comments")
    public void setComments(String comments) {
        this.comments = comments;
    }

    @JsonProperty("approve_status")
    public String getApproveStatus() {
        return this.approveStatus;
    }

    @JsonProperty("approve_status")
    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    @JsonProperty("change_km_drive")
    public String getChangeKM() {
        return this.changeKM;
    }

    @JsonProperty("change_km_drive")
    public void setChangeKM(String changeKM) {
        this.changeKM = changeKM;
    }

    public String getChangeOverTime() {
        return this.changeOverTime;
    }

    public void setChangeOverTime(String changeOverTime) {
        this.changeOverTime = changeOverTime;
    }

    public String getChangeWorkhrs() {
        return this.changeWorkhrs;
    }

    public void setChangeWorkhrs(String changeWorkhrs) {
        this.changeWorkhrs = changeWorkhrs;
    }

    public String getWorksheetStatus() {
        return this.worksheetStatus;
    }

    public void setWorksheetStatus(String worksheetStatus) {
        this.worksheetStatus = worksheetStatus;
    }

    public String getSyncStatus() {
        return this.syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    @JsonProperty("id")
    public String getServerWorkSheetId() {
        return this.serverWorkSheetId;
    }

    @JsonProperty("id")
    public void setServerWorkSheetId(String serverWorkSheetId) {
        this.serverWorkSheetId = serverWorkSheetId;
    }

    public ArrayList<Service> getServiceObjectsArray() {
        return this.serviceObjectsArray;
    }

    public void setServiceObjectsArray(ArrayList<Service> serviceObjectsArray) {
        this.serviceObjectsArray = serviceObjectsArray;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @JsonProperty("update_date")
    public String getUpdatedate() {
        return this.updatedate;
    }

    @JsonProperty("update_date")
    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @JsonProperty("address")
    public String getAddress() {
        return this.address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("workimage_details")
    public void setAttachementList(ArrayList<AttachmentMap> attachementList) {
        this.attachementList = attachementList;
    }

    @JsonProperty("workimage_details")
    public ArrayList<AttachmentMap> getAttachementList() {
        return this.attachementList;
    }

    @JsonIgnore
    public void setAttachementToDelete(ArrayList<AttachmentMap> attachementToDelete) {
        this.attachementToDelete = attachementToDelete;
    }

    @JsonIgnore
    public ArrayList<AttachmentMap> getAttachementToDelete() {
        return this.attachementToDelete;
    }

    @JsonProperty("work_end_time")
    public String getWorkEndTime() {
        return this.workEndTime;
    }

    @JsonProperty("work_end_time")
    public void setWorkEndTime(String workEndTime) {
        this.workEndTime = workEndTime;
    }

    @JsonProperty("work_start_time")
    public String getWorkStartTime() {
        return this.workStatrTime;
    }

    @JsonProperty("work_start_time")
    public void setWorkStartTime(String workStatrTime) {
        this.workStatrTime = workStatrTime;
    }

    @JsonProperty("work_overtime2_end_time")
    public String getOverTime1EndTime() {
        return this.overTime1EndTime;
    }

    @JsonProperty("work_overtime2_end_time")
    public void setOverTime1EndTime(String overTime1EndTime) {
        this.overTime1EndTime = overTime1EndTime;
    }

    @JsonProperty("work_overtime2_start_time")
    public String getOverTime1StartTime() {
        return this.overTime1StartTime;
    }

    @JsonProperty("work_overtime2_start_time")
    public void setOverTime1StartTime(String overTime1StartTime) {
        this.overTime1StartTime = overTime1StartTime;
    }

    @JsonProperty("work_overtime1_end_time")
    public String getOverTime2EndTime() {
        return this.overTime2EndTime;
    }

    @JsonProperty("work_overtime1_end_time")
    public void setOverTime2EndTime(String overTime2EndTime) {
        this.overTime2EndTime = overTime2EndTime;
    }

    @JsonProperty("work_overtime1_start_time")
    public String getOverTime2StartTime() {
        return this.overTime2StartTime;
    }

    @JsonProperty("work_overtime1_start_time")
    public void setOverTime2StartTime(String overTime2StartTime) {
        this.overTime2StartTime = overTime2StartTime;
    }

    @JsonProperty("profile_image")
    public String getProfile_image() {
        return this.profile_image;
    }

    @JsonProperty("profile_image")
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    @JsonProperty("work_end_date")
    public String getWorkEndDate() {
        return this.workEndDate;
    }

    @JsonProperty("work_end_date")
    public void setWorkEndDate(String workEndDate) {
        this.workEndDate = workEndDate;
    }

    @JsonProperty("is_edit")
    public void setIsEditable(String isEditable) {
        this.isEditable = isEditable;
    }

    @JsonProperty("is_edit")
    public String getIsEditable() {
        return this.isEditable;
    }

    public ArrayList<OvertimeRule> getOvertimeRuleArrayList() {
        return this.overtimeRuleArrayList;
    }

    public void setOvertimeRuleArrayList(ArrayList<OvertimeRule> overtimeRuleArrayList) {
        this.overtimeRuleArrayList = overtimeRuleArrayList;
    }

    public String getIsworksheetAutomaticEditmode() {
        return this.isworksheetEditmode;
    }

    public void setIsworksheetAutomaticEditmode(String isworksheetEditmode) {
        this.isworksheetEditmode = isworksheetEditmode;
    }

    @JsonProperty("total_work_overtime")
    public String getTotal_work_overtime() {
        return this.total_work_overtime;
    }

    @JsonProperty("total_work_overtime")
    public void setTotal_work_overtime(String total_work_overtime) {
        this.total_work_overtime = total_work_overtime;
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
