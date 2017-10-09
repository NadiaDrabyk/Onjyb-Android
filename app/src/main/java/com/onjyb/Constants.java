package com.onjyb;

import android.os.Environment;
import com.onjyb.db.OvertimeRule;
import java.util.ArrayList;

public class Constants {
    public static final String APIKEY = "AIzaSyAPjhDvxcjJKl2atsmCLrqkbgYs09YT-Ug";
    public static String APP_HOME = Environment.getExternalStorageDirectory().getPath();
    public static final String APP_NAME = "Onjyb";
    public static final int ASYNC_STATUS_COMPLETED = 102;
    public static final int ATTACHEMENT_TYPE_BITMAP = 1002;
    public static final int ATTACHEMENT_TYPE_URL = 1003;
    public static final String BASE_URL = "http://app.onjyb.com/api/";
    public static String BUILDVERSION = "1";
    public static final String Current_version_app = "cur_version_app";
    public static String DEVICE_ID = "";
    public static String DIR_MEDIA = (APP_HOME + "/onjyb");
    public static String DIR_AUDIO = (DIR_MEDIA + "/onjyb");
    public static String DIR_IMAGES = (DIR_MEDIA + "/.nomedia");
    public static String DIR_VIDEO = (DIR_MEDIA + "/onjyb");
    public static final int ERROR = 101;
    public static final int ERROR_NETWORK = 104;
    public static final int GET_REQUEST = 1;
    public static final int POST_REQUEST = 2;
    public static final String PREFS_NAME = "OnjybApp";
    public static String PREF_COMPANY_ID = "1";
    public static String PREF_COMPANY_LOGO = "company_logo";
    public static String PREF_DEFAULT_WORKHRS = "default_workhrs";
    public static String PREF_DETAIL_TASK_LIST_LAST_UPDATE = "tasklist_lastupdate";
    public static String PREF_FILE_DBUSER_USERNAME = "user_name";
    public static String PREF_FILE_DBUSER_USERTYPE = "user_type";
    public static String PREF_GET_LASTDATE_UNREAD_COUNT = "lastDate_Unread_Count";
    public static String PREF_HASE_RECORDS_AVAILABLE_APPROVE = "has_more_records_in_approve";
    public static String PREF_HASE_RECORDS_AVAILABLE_LEAVE = "has_more_records_in_leave";
    public static String PREF_HASE_RECORDS_AVAILABLE_MESSAGES = "has_more_records_in_messages";
    public static String PREF_HASE_RECORDS_AVAILABLE_NOT_APPROVE = "has_more_records_in_not_approve";
    public static String PREF_HASE_RECORDS_AVAILABLE_PENDDING = "has_more_records_in_pendding";
    public static String PREF_HASE_RECORDS_AVAILABLE_TOTAL_SUMMARY = "has_more_records_in_statistikk";
    public static String PREF_KEY_DBUSER_PASSWORD = "password";
    public static String PREF_KEY_USER_IMG_PATH = "profile_path";
    public static String PREF_KEY_USER_USER_ROLE_ID = "user_role_id";
    public static String PREF_LEAVE_ARRAY = "leaveCountArray";
    public static String PREF_LEFT_LEAVE = "left_leave";
    public static String PREF_MASTERLIST_LAST_UPDATE = "masterlist_lastupdate";
    public static String PREF_MOBILE = "mobile";
    public static String PREF_SENDER_ID = "sender_id";
    public static String PREF_UNREAD_LEAVE_COUNT = "leave_Unread_Count";
    public static String PREF_UNREAD_MANAGER_APPROVE_TASK = "work_manager_unread";
    public static String PREF_UNREAD_MANAGER_APPROVE_leave = "leave_manager_unread";
    public static String PREF_UNREAD_MESSAGE_COUNT = "message_Unread_Count";
    public static String PREF_UNREAD_WORK_COUNT = "worksheet_Unread_Count";
    public static String PREF_USER_EMAIL = "email";
    public static String PREF_USER_FNAME = "user_fname";
    public static String PREF_USER_ID = "user_id";
    public static String PREF_USER_LNAME = "user_lname";
    public static String PREF_USER_PROFILE_PIC = "user_profile_pic";
    public static String PREF_WORK_ID = "work_id";
    public static String PREF_WORK_OVERTIME_AUTOMATIC = "is_work_overtime_automatic";
    public static String PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO = "NO";
    public static String PREF_WORK_OVERTIME_AUTOMATIC_STATUS_YES = "YES";
    public static final String RESPONSE_KEY_CODE = "res_code";
    public static final String RESPONSE_KEY_MSG = "res_message";
    public static final String RESPONSE_KEY_OBJ = "res_object";
    public static final String RESPONSE_KEY_Profile_DETAIL = "profile_details";
    public static final String RESPONSE_KEY_SUBSCRPTION_DETAILS = "subscription_details";
    public static final String RESPONSE_KEY_TOKEN_DETAIL = "token_detail";
    public static final String RESPONSE_KEY_USER_DETAIL = "user_detail";
    public static final String RESPONSE_ROLE_ID = "ref_role_id";
    public static final int RESPONSE_STATUS_ERROR = 0;
    public static final int RESPONSE_STATUS_SUCCESS = 1;
    public static final String SENDER_ID = "122991711426";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_PENDING = "pendding";
    public static final String STATUS_REJECTED = "rejected";
    public static final int SUCCESS = 100;

        public static final String URL_PREFIX = "http://app.onjyb.com";
//    public static final String URL_PREFIX = "http://www.appbusinesspartner.com";

    public static final String BASE_IMAGE_URL = URL_PREFIX + "/attachment/";

    public static final String URL_ADD_EMP_LEAVE_FORM               = URL_PREFIX + "/api//Leave_Controller/addEmpLeave";
    public static final String URL_APPROVE_LEAVE                    = URL_PREFIX + "/api//Leave_Controller/approveLeave";
    public static final String URL_CHANGE_PASSWORD                  = URL_PREFIX + "/api//User_Controller/changePassword";
    public static final String URL_FORGOT_PASSWORD                  = URL_PREFIX + "/api/User_Controller/forgotPassword";
    public static final String URL_GET_MESSAGES                     = URL_PREFIX + "/api//Message_Controller/getMessages";
    public static final String URL_GET_UNREAD_COUNT                 = URL_PREFIX + "/api//User_Controller/getUnreadCount";
    public static final String URL_GRAPH_DETAIL                     = URL_PREFIX + "/api//WorkSheet_Controller/getGraph";
    public static final String URL_GROUP_CHAT                       = URL_PREFIX + "/api//Message_Controller/sendMessage";
    public static final String URL_LEAVE_LIST                       = URL_PREFIX + "/api//Leave_Controller/getLeaveList";
    public static final String URL_LOGIN                            = URL_PREFIX + "/api//User_Controller/login";
    public static final String URL_LOGOUT_USER                      = URL_PREFIX + "/api//User_Controller/logout";
    public static final String URL_MASTER_LIST                      = URL_PREFIX + "/api//WorkSheet_Controller/getUpdatedMaster_1";
    public static final String URL_MYTASK_WORKSHEET_DETAIL          = URL_PREFIX + "/api/WorkSheet_Controller/getTaskDetails_1";
    public static final String URL_MYTASK_WORKSHEET_LIST            = URL_PREFIX + "/api//WorkSheet_Controller/getTaskList";
    public static final String URL_PROFILE_DISPLAY                  = URL_PREFIX + "/api//User_Controller/getProfile";
    public static final String URL_PROFILE_EDIT                     = URL_PREFIX + "/api//User_Controller/editProfile";
    public static final String URL_REGISTER_DEVICE                  = URL_PREFIX + "/api//User_Controller/registerAPNS";
    public static final String URL_REJECT_TASK                      = URL_PREFIX + "/api//WorkSheet_Controller/rejectTask";
    public static final String URL_UPLOAD_IMAGES_TO_SERVER          = URL_PREFIX + "/api//WorkSheet_Controller/imagesUpload";
    public static final String URL_UPLOAD__WORKSHEET_TO_SERVER      = URL_PREFIX + "/api/WorkSheet_Controller/addEmpWorkSheet_1";

    public static final String USER_ROLE_CEO = "2";
    public static final String USER_ROLE_EMPLOYEE = "4";
    public static final String USER_ROLE_MANAGER = "3";
    public static final String USER_TYPE_APP = "ap";
    public static final ArrayList<OvertimeRule> UserDependsOvertimeRulesList = new ArrayList();
    public static final String forceUpdateApp = "force_update_app";
    public static final String isVersionDifferent = "is_version_diff";
    public static final String message = "message";
    public static final String skip_btn_name = "skip_btn_name";
    public static boolean updateActivityCall = true;
    public static final String updateUrl = "upade_url";
    public static final String update_btn_name = "update_btn_name";

    public static final String APPROVE_STATUS_APPROVE = "approve";
}