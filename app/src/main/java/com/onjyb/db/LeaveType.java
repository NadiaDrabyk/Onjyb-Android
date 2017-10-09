package com.onjyb.db;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveType implements Serializable {
    String approveStatus;
    String createDate;
    String employeeId;
    String firstName;
    String id;
    String lastName;
    String leaveDays;
    String leaveFromDt;
    String leaveId;
    String leaveTillDt;
    String leaveTypeId;
    String leave_master_type;
    String leave_ref_user_id;
    String leavetypename;
    String leavetypestatus;
    String leftDays;
    String lvReason;
    String lvTitle;
    String note;
    String planed_leave;
    String profile_image;
    String project_id;
    String project_name;
    String refLeaveTypeId;
    String ref_company_id;
    String total_earn_leave;
    String total_leave;
    String total_remaining_leave;
    String updateDate;
    String used_leave;

    //jin
    String total_hours;

    @JsonProperty("total_hours")
    public String getTotal_hours(){return total_hours;}
    @JsonProperty("total_hours")
    public void setTotal_hours(String total){
        this.total_hours = total;
    }
    //

    @JsonProperty("leave_master_type")
    public String getLeave_master_type() {
        return this.leave_master_type;
    }

    @JsonProperty("leave_master_type")
    public void setLeave_master_type(String leave_master_type) {
        this.leave_master_type = leave_master_type;
    }

    @JsonProperty("leave_id")
    public String getLeaveId() {
        return this.leaveId;
    }

    @JsonProperty("leave_id")
    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("leavetype_id")
    public String getLeaveTypeId() {
        return this.leaveTypeId;
    }

    @JsonProperty("leavetype_id")
    public void setLeaveTypeId(String leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }

    @JsonProperty("ref_leave_type_id")
    public String getRefLeaveTypeId() {
        return this.refLeaveTypeId;
    }

    @JsonProperty("ref_leave_type_id")
    public void setRefLeaveTypeId(String refLeaveTypeId) {
        this.refLeaveTypeId = refLeaveTypeId;
    }

    @JsonProperty("ref_company_id")
    public String getRef_company_id() {
        return this.ref_company_id;
    }

    @JsonProperty("ref_company_id")
    public void setRef_company_id(String ref_company_id) {
        this.ref_company_id = ref_company_id;
    }

    @JsonProperty("leave_type")
    public String getLeavetypename() {
        return this.leavetypename;
    }

    @JsonProperty("leave_type")
    public void setLeavetypename(String leavetypename) {
        this.leavetypename = leavetypename;
    }

    @JsonProperty("leave_status")
    public String getLeavetypestatus() {
        return this.leavetypestatus;
    }

    @JsonProperty("leave_status")
    public void setLeavetypestatus(String leavetypestatus) {
        this.leavetypestatus = leavetypestatus;
    }

    @JsonProperty("start_date")
    public String getLeaveFromDt() {
        return this.leaveFromDt;
    }

    @JsonProperty("start_date")
    public void setLeaveFromDt(String leaveFromDt) {
        this.leaveFromDt = leaveFromDt;
    }

    @JsonProperty("end_date")
    public String getLeaveTillDt() {
        return this.leaveTillDt;
    }

    @JsonProperty("end_date")
    public void setLeaveTillDt(String leaveTillDt) {
        this.leaveTillDt = leaveTillDt;
    }

    @JsonProperty("leave_description")
    public String getLvReason() {
        return this.lvReason;
    }

    @JsonProperty("leave_description")
    public void setLvReason(String lvReason) {
        this.lvReason = lvReason;
    }

    @JsonProperty("leave_subject")
    public String getLvTitle() {
        return this.lvTitle;
    }

    @JsonProperty("leave_subject")
    public void setLvTitle(String lvTitle) {
        this.lvTitle = lvTitle;
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

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getLeaveDays() {
        return this.leaveDays;
    }

    @JsonProperty("ref_project_id")
    public String getProject_id() {
        return this.project_id;
    }

    @JsonProperty("ref_project_id")
    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    @JsonProperty("approve_status")
    public String getApproveStatus() {
        return this.approveStatus;
    }

    @JsonProperty("approve_status")
    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getProject_name() {
        return this.project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return this.firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("profile_image")
    public String getProfile_image() {
        return this.profile_image;
    }

    @JsonProperty("profile_image")
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @JsonProperty("note")
    public String getNote() {
        return this.note;
    }

    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return this.lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("left_leave")
    public String getLeftDays() {
        return this.leftDays;
    }

    @JsonProperty("left_leave")
    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public String getLeave_ref_user_id() {
        return this.leave_ref_user_id;
    }

    public void setLeave_ref_user_id(String leave_ref_user_id) {
        this.leave_ref_user_id = leave_ref_user_id;
    }

    public String getTotal_earn_leave() {
        return this.total_earn_leave;
    }

    public void setTotal_earn_leave(String total_earn_leave) {
        this.total_earn_leave = total_earn_leave;
    }

    public String getTotal_leave() {
        return this.total_leave;
    }

    public void setTotal_leave(String total_leave) {
        this.total_leave = total_leave;
    }

    @JsonProperty("total_remaining_leave")
    public String getTotal_remaining_leave() {
        return this.total_remaining_leave;
    }

    @JsonProperty("total_remaining_leave")
    public void setTotal_remaining_leave(String total_remaining_leave) {
        this.total_remaining_leave = total_remaining_leave;
    }

    @JsonProperty("total_pending_leave")
    public String getPlaned_leave() {
        return this.planed_leave;
    }

    @JsonProperty("total_pending_leave")
    public void setPlaned_leave(String planed_leave) {
        this.planed_leave = planed_leave;
    }

    @JsonProperty("total_approve_leave")
    public String getUsed_leave() {
        return this.used_leave;
    }

    @JsonProperty("total_approve_leave")
    public void setUsed_leave(String used_leave) {
        this.used_leave = used_leave;
    }
}
