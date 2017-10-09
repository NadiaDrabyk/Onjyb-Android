package com.onjyb.beans;

import android.content.Context;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.db.LeaveType;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.reqreshelper.AsyncTaskCompleteListener;
import com.onjyb.reqreshelper.ETechAsyncTask;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import java.util.HashMap;
import org.json.JSONObject;

public class LeavesApiHelper implements AsyncTaskCompleteListener<String> {
    ActionCallback actionCallback;
    Context context;

    public LeavesApiHelper(Context context) {
        this.context = context;
    }

    public void apiGetLeaveDetail(String status, LeaveType leaveType, ActionCallback actionCallback) {
        try {
            this.actionCallback = actionCallback;
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            String comp_id = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            HashMap<String, Object> map = new HashMap();
            if (status != null) {
                map.put("approve_status", status);
            }
            map.put("user_id", user_id);
            map.put("role_id", role_id);
            map.put("company_id", comp_id);
            map.put("page_number", "1");
            if (leaveType.getLeaveId() != null && leaveType.getLeaveId().equalsIgnoreCase("")) {
                map.put("leave_id", leaveType.getLeaveId());
            }
            ETechAsyncTask task = new ETechAsyncTask(this.context, this, Constants.URL_LEAVE_LIST, map, 2, true, true);
            task.hideProgressDialog();
            task.execute(new String[]{Constants.URL_LEAVE_LIST});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiAddEmpLeaveForm(LeaveType leaveType, ActionCallback actionCallback) {
        try {
            this.actionCallback = actionCallback;
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            String comp_id = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
            HashMap<String, Object> map = new HashMap();
            if (leaveType != null) {
                map.put("user_id", user_id);
                map.put("company_id", comp_id);
                map.put("leavetype_id", leaveType.getLeaveTypeId());
                if (leaveType.getProject_id() != null) {
                    map.put("project_id", leaveType.getProject_id());
                }
                String strDate = AppUtils.getFormattedDate(leaveType.getLeaveFromDt(), "dd MMM yy", "dd/MM/yyyy");
                String endDate = AppUtils.getFormattedDate(leaveType.getLeaveTillDt(), "dd MMM yy", "dd/MM/yyyy");
                if (!(strDate == null || endDate == null)) {
                    map.put("start_date", strDate);
                    map.put("end_date", endDate);
                }
                if (leaveType.getLeaveId() != null) {
                    map.put("leave_id", leaveType.getLeaveId());
                } else {
                    map.put("leave_id", "");
                }
                map.put("leave_description", leaveType.getLvReason());

                if(leaveType.getTotal_hours() != null)
                    map.put("total_hours", leaveType.getTotal_hours());
            }
            ETechAsyncTask task = new ETechAsyncTask(this.context, this, Constants.URL_ADD_EMP_LEAVE_FORM, map, 2, true, true);
            task.hideProgressDialog();
            task.execute(new String[]{Constants.URL_ADD_EMP_LEAVE_FORM});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiApproveLeave(String status, LeaveType leaveType, ActionCallback actionCallback) {
        try {
            this.actionCallback = actionCallback;
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            String comp_id = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            HashMap<String, Object> map = new HashMap();
            if (status != null) {
                map.put("approve_status", status);
            }
            map.put("user_id", user_id);
            map.put("leave_id", leaveType.getLeaveId());
            map.put("note", leaveType.getNote());
            new ETechAsyncTask(this.context, this, Constants.URL_APPROVE_LEAVE, map, 2, true, true).execute(new String[]{Constants.URL_APPROVE_LEAVE});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiLeaveListing(String status, int pageNo, boolean is_mng_leave, ActionCallback actionCallback) {
        try {
            this.actionCallback = actionCallback;
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            String comp_id = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            HashMap<String, Object> map = new HashMap();
            if (status != null) {
                map.put("approve_status", status);
            }
            map.put("user_id", user_id);
            map.put("role_id", role_id);
            map.put("company_id", comp_id);
            map.put("page_number", Integer.valueOf(pageNo));
            if (is_mng_leave) {
                map.put("is_mng_jobber", "yes");
            } else {
                map.put("is_mng_jobber", "");
            }
            ETechAsyncTask task = new ETechAsyncTask(this.context, this, Constants.URL_LEAVE_LIST, map, 2, true, true);
            task.hideProgressDialog();
            task.execute(new String[]{Constants.URL_LEAVE_LIST});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apiLeaveListingWithFilter(String status, int pageNo, LeaveType leaveType, boolean is_mng_leave, ActionCallback actionCallback) {
        try {
            this.actionCallback = actionCallback;
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            String comp_id = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            HashMap<String, Object> map = new HashMap();
            if (status != null) {
                map.put("approve_status", status);
            }
            map.put("user_id", user_id);
            map.put("role_id", role_id);
            map.put("company_id", comp_id);
            map.put("page_number", Integer.valueOf(pageNo));
            if (is_mng_leave) {
                map.put("is_mng_jobber", "yes");
            } else {
                map.put("is_mng_jobber", "");
            }
            if (leaveType != null) {
                if (!(leaveType.getLeaveFromDt() == null || leaveType.getLeaveFromDt().equalsIgnoreCase(""))) {
                    map.put("start_date", leaveType.getLeaveFromDt());
                }
                if (!(leaveType.getLeaveTillDt() == null || leaveType.getLeaveTillDt().equalsIgnoreCase(""))) {
                    map.put("end_date", leaveType.getLeaveTillDt());
                }
                if (!(leaveType.getProject_id() == null || leaveType.getProject_id().equalsIgnoreCase(""))) {
                    map.put("project_id", leaveType.getProject_id());
                }
                if (!(leaveType.getLeaveTypeId() == null || leaveType.getLeaveTypeId().equalsIgnoreCase(""))) {
                    map.put("leavetype_id", leaveType.getLeaveTypeId());
                }
                if (!(leaveType.getEmployeeId() == null || leaveType.getEmployeeId().equalsIgnoreCase(""))) {
                    map.put("employee_id", leaveType.getEmployeeId());
                }
            }
            ETechAsyncTask task = new ETechAsyncTask(this.context, this, Constants.URL_LEAVE_LIST, map, 2, true, true);
            task.hideProgressDialog();
            task.execute(new String[]{Constants.URL_LEAVE_LIST});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTaskComplete(int statusCode, String result, String webserviceCb, Object tag) {
        if (statusCode == 104) {
            try {
                this.actionCallback.onActionComplete(statusCode, webserviceCb, result);
            } catch (Exception e) {
                e.printStackTrace();
                this.actionCallback.onActionComplete(0, webserviceCb, e.getMessage().toString() + "");
            }
        } else if (statusCode != 102 || result == null) {
            this.actionCallback.onActionComplete(statusCode, webserviceCb, this.context.getString(R.string.response_error_msg));
        } else if (statusCode == 102) {
            try {
                JSONObject jObj = new JSONObject(result);
                if (((Integer) jObj.get(Constants.RESPONSE_KEY_CODE)).intValue() != 1) {
                    this.actionCallback.onActionComplete(0, webserviceCb, jObj.get(Constants.RESPONSE_KEY_MSG).toString());
                } else if (webserviceCb.equalsIgnoreCase(Constants.URL_ADD_EMP_LEAVE_FORM)) {
                    this.actionCallback.onActionComplete(1, webserviceCb, jObj);
                } else if (webserviceCb.equalsIgnoreCase(Constants.URL_LEAVE_LIST)) {
                    JSONObject resobj = jObj.getJSONObject(Constants.RESPONSE_KEY_OBJ);
                    Preference.setSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_LEAVE, String.valueOf(resobj.getBoolean("has_more_records")));
                    this.actionCallback.onActionComplete(1, webserviceCb, resobj);
                } else if (webserviceCb.equalsIgnoreCase(Constants.URL_APPROVE_LEAVE)) {
                    this.actionCallback.onActionComplete(1, webserviceCb, jObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.actionCallback.onActionComplete(0, webserviceCb, e.getMessage().toString() + "");
            }
        }
    }
}
