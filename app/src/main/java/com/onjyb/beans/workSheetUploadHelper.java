package com.onjyb.beans;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.WorkServiceMap;
import com.onjyb.db.WorkSheet;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.reqreshelper.AsyncTaskCompleteListener;
import com.onjyb.reqreshelper.ETechAsyncTask;
import com.onjyb.reqreshelper.FileObject;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class workSheetUploadHelper implements AsyncTaskCompleteListener<String> {
    String TAG = UserHelper.class.getName();
    ActionCallback actionCallback;
    ArrayList<HashMap<String, String>> alistproject;
    ArrayList<WorkServiceMap> arrExtraService;
    Context context;
    DatabaseHelper dbHelper;
    HashMap<String, String> imgData = new HashMap();
    ArrayList<OvertimeRule> overtimerules = new ArrayList();
    ArrayList<String> tbl_Project_Field;
    String tbl_project = "tblproject";
    String userId = "";
    WorkSheet workSheet;

    public workSheetUploadHelper(Context context) {
        this.context = context;
    }

    public void apiUploadImages(HashMap<String, String> imgData, ActionCallback imgUpload) {
        Exception e;
        this.actionCallback = imgUpload;
        this.imgData = imgData;
        HashMap<String, Object> map = new HashMap();
        map.put("map_id", imgData.get("server_attachment_id"));
        map.put("map_type", imgData.get("map_name"));
        File file = new File((String) imgData.get("attachment_name"));
        try {
            if (file.exists()) {
                FileObject fObj = new FileObject();
                FileObject fileObject;
                try {
                    fObj.setContentType("image/*");
                    fObj.setByteData(AppUtils.readFile(file));
                    fObj.setFileName(file.getName());
                    map.put("user_photo", fObj);
                    new ETechAsyncTask(this.context, this, Constants.URL_UPLOAD_IMAGES_TO_SERVER, map, 2, true, false).execute(new String[]{Constants.URL_UPLOAD_IMAGES_TO_SERVER});
                    fileObject = fObj;
                    return;
                } catch (Exception e2) {
                    e = e2;
                    fileObject = fObj;
                    e.printStackTrace();
                }
            }
            this.actionCallback.onActionComplete(1, Constants.URL_UPLOAD_IMAGES_TO_SERVER, "");
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
        }
    }
    public String encodeToBase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    public void apiUploadWorksheetToServer(String signedby, String singedEmail, String signedPerson, String signedEmail,Bitmap signedBitmap,//0918
                                           WorkSheet workSheet, ArrayList<WorkServiceMap> arrExtraService, ArrayList<OvertimeRule> overtimeruleslist, long halfOvertime, long fullOvertime, ActionCallback myTaskDetailCallback) {
        try {
            int i;
            JSONObject obj;
            HashMap<String, Object> map;
            this.actionCallback = myTaskDetailCallback;
            this.workSheet = workSheet;
            this.arrExtraService = arrExtraService;
            this.overtimerules = overtimeruleslist;
            String roleId = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            JSONObject objJson = new JSONObject();
            String strAttIds = "";
            if (workSheet.getServerWorkSheetId() != null) {
                objJson.put("work_id", workSheet.getServerWorkSheetId());
                ArrayList<AttachmentMap> attachementToDelete = workSheet.getAttachementToDelete();
                if (attachementToDelete != null && attachementToDelete.size() > 0) {
                    for (i = 0; i < attachementToDelete.size(); i++) {
                        strAttIds = strAttIds + "," + ((AttachmentMap) attachementToDelete.get(i)).getAttachmentId();
                    }
                    strAttIds = strAttIds.substring(1, strAttIds.length());
                }
            } else {
                objJson.put("attachment_ids", "");
            }
            //0918
            if(signedby != null && signedby.length()  > 0 )
                objJson.put("txtSignedBy", signedby);
            if(singedEmail != null && singedEmail.length()  > 0 )
                objJson.put("txtSignedEmail", singedEmail);
            if(signedPerson != null && signedPerson.length()  > 0 )
                objJson.put("txtContactName", signedPerson);
            if(signedEmail != null && signedEmail.length()  > 0 )
                objJson.put("txtContactEmail", signedEmail);
            if( signedBitmap != null ) {
                //0920
                String str = this.encodeToBase64(signedBitmap);
                objJson.put("signature", "data:image/png;base64," + str);
            }
            //end
            objJson.put("is_ptobank", workSheet.getIs_ptobank());
            objJson.put("company_id", workSheet.getCompanyId());
            objJson.put("user_id", workSheet.getRefUserId());
            objJson.put("role_id", roleId);
            objJson.put("project_id", workSheet.getRefProjectId());
            if (workSheet.getRefServiceId() == null || !workSheet.getRefServiceId().equalsIgnoreCase("")) {
                objJson.put("service_id", "0");//jin "1"
            } else {
                objJson.put("service_id", workSheet.getRefServiceId());
            }
            if (workSheet.getRefBranchId() == null || workSheet.getRefBranchId().length() <= 0) {
                objJson.put("branch_id", "");
            } else {
                objJson.put("branch_id", workSheet.getRefBranchId());
            }
            String strDate = AppUtils.getFormattedDate(workSheet.getWorkDate(), "dd MMM yy", "dd/MM/yyyy");
            if (strDate != null) {
                objJson.put("work_date", strDate);
            }
            String endDate = AppUtils.getFormattedDate(workSheet.getWorkEndDate(), "dd MMM yy", "dd/MM/yyyy");
            if (strDate != null) {
                objJson.put("work_end_date", endDate);
            }
            objJson.put("total_time", workSheet.getTotalWorkTime());
            objJson.put("work_hour", workSheet.getWorkHrs());
            objJson.put("work_start_time", workSheet.getWorkStartTime());
            objJson.put("work_end_time", workSheet.getWorkEndTime());
            if (workSheet.getOverTime2StartTime() != null) {
                objJson.put("over_time2_start_time", workSheet.getOverTime1StartTime());
            }
            if (workSheet.getOverTime2EndTime() != null) {
                objJson.put("over_time2_end_time", workSheet.getOverTime1EndTime());
            }
            if (workSheet.getWorkOverTime1() != null) {
                objJson.put("over_time1", workSheet.getWorkOverTime1());
            } else {
                objJson.put("over_time1", "0");
            }
            if (workSheet.getWorkOverTime2() != null) {
                objJson.put("over_time2", workSheet.getWorkOverTime2());
            } else {
                objJson.put("over_time2", "0");
            }
            if (workSheet.getOverTime1StartTime() != null) {
                objJson.put("over_time1_start_time", workSheet.getOverTime2StartTime());
            }
            if (workSheet.getOverTime1EndTime() != null) {
                objJson.put("over_time1_end_time", workSheet.getOverTime2EndTime());
            }
            objJson.put("language", AppUtils.getDefaultPerameters(this.context).get("language"));
            objJson.put("break_time", workSheet.getBreakTime());
            objJson.put("km_drive", workSheet.getkMDrive());
            objJson.put("comments", workSheet.getComments());
            objJson.put("approve_status", workSheet.getApproveStatus() != null ? workSheet.getApproveStatus() : "");
            JSONArray servicearray = new JSONArray();
            if (arrExtraService != null && arrExtraService.size() > 0) {
                for (i = 0; i < arrExtraService.size(); i++) {
                    obj = new JSONObject();
                    obj.put("service_id", ((WorkServiceMap) arrExtraService.get(i)).getRefServiceId());
                    obj.put("service_time", ((WorkServiceMap) arrExtraService.get(i)).getServiceTime());
                    obj.put("local_service_id", "");
                    obj.put("service_comment", ((WorkServiceMap) arrExtraService.get(i)).getService_comments());
                    ArrayList<AttachmentMap> attachementToDeleteService = ((WorkServiceMap) arrExtraService.get(i)).getAttachementToDelete();
                    if (attachementToDeleteService != null && attachementToDeleteService.size() > 0) {
                        String strattservice = "";
                        for (int j = 0; j < attachementToDeleteService.size(); j++) {
                            strattservice = strattservice + "," + ((AttachmentMap) attachementToDeleteService.get(j)).getAttachmentId();
                        }
                        if (!strattservice.equalsIgnoreCase("") && strattservice.length() > 0) {
                            if (strAttIds.equalsIgnoreCase("") && strAttIds.length() == 0) {
                                strAttIds = strattservice.substring(1, strattservice.length());
                            } else {
                                strAttIds = strAttIds + strattservice;
                            }
                        }
                    }
                    servicearray.put(obj);
                }
                if (workSheet.getServerWorkSheetId() != null) {
                    objJson.put("attachment_ids", strAttIds);
                }
                objJson.put("extra_service", servicearray);
            }
            JSONArray overtimeList = new JSONArray();
            String isautomatic = workSheet.getIsworksheetAutomaticEditmode();
            if (isautomatic != null) {
                if (isautomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_YES)) {
                    objJson.put("is_automatic", "yes");
                    if (this.overtimerules != null || this.overtimerules.size() <= 0) {
                        if (!(objJson.has("overtime_details") || isautomatic == null)) {
                            if (isautomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO)) {
                                for (i = 0; i < 2; i++) {
                                    obj = new JSONObject();
                                    obj.put("minutes", "0");
                                    obj.put("ot_type", "M");
                                    if (i != 0) {
                                        obj.put("rule", "50");
                                        if (workSheet.getWorkOverTime2() == null && !workSheet.getWorkOverTime2().isEmpty()) {
                                            obj.put("actual_minute", workSheet.getWorkOverTime2());
                                        } else if (halfOvertime <= 0) {
                                            obj.put("actual_minute", halfOvertime);
                                        } else {
                                            obj.put("actual_minute", "0");
                                        }
                                    } else {
                                        obj.put("rule", "100");
                                        if (workSheet.getWorkOverTime1() == null && !workSheet.getWorkOverTime1().isEmpty()) {
                                            obj.put("actual_minute", workSheet.getWorkOverTime1());
                                        } else if (fullOvertime <= 0) {
                                            obj.put("actual_minute", fullOvertime);
                                        } else {
                                            obj.put("actual_minute", "0");
                                        }
                                    }
                                    overtimeList.put(obj);
                                }
                                objJson.put("overtime_details", overtimeList);
                            }
                        }
                    } else {
                        for (i = 0; i < this.overtimerules.size(); i++) {
                            obj = new JSONObject();
                            obj.put("rule", ((OvertimeRule) this.overtimerules.get(i)).getRuleName());
                            obj.put("minutes", ((OvertimeRule) this.overtimerules.get(i)).getRuleValue());
                            if (((OvertimeRule) this.overtimerules.get(i)).getActualvalue() != null) {
                                obj.put("actual_minute", ((OvertimeRule) this.overtimerules.get(i)).getActualvalue());
                            } else {
                                obj.put("actual_minute", "0");
                            }
                            obj.put("ot_type", ((OvertimeRule) this.overtimerules.get(i)).getOt_type());
                            overtimeList.put(obj);
                        }
                        objJson.put("overtime_details", overtimeList);
                    }
                    map = new HashMap();
                    map.put("jsondata", objJson);
                    new ETechAsyncTask(this.context, this, Constants.URL_UPLOAD__WORKSHEET_TO_SERVER, map, 2, false, true, true).execute(new String[]{Constants.URL_UPLOAD__WORKSHEET_TO_SERVER});
                }
            }
            objJson.put("is_automatic", "no");
            if (this.overtimerules != null) {
            }
            if (isautomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO)) {
                for (i = 0; i < 2; i++) {
                    obj = new JSONObject();
                    obj.put("minutes", "0");
                    obj.put("ot_type", "M");
                    if (i != 0) {
                        obj.put("rule", "100");
                        if (workSheet.getWorkOverTime1() == null) {
                        }
                        if (fullOvertime <= 0) {
                            obj.put("actual_minute", "0");
                        } else {
                            obj.put("actual_minute", fullOvertime);
                        }
                    } else {
                        obj.put("rule", "50");
                        if (workSheet.getWorkOverTime2() == null) {
                        }
                        if (halfOvertime <= 0) {
                            obj.put("actual_minute", "0");
                        } else {
                            obj.put("actual_minute", halfOvertime);
                        }
                    }
                    overtimeList.put(obj);
                }
                objJson.put("overtime_details", overtimeList);
            }
            map = new HashMap();
            map.put("jsondata", objJson);
            new ETechAsyncTask(this.context, this, Constants.URL_UPLOAD__WORKSHEET_TO_SERVER, map, 2, false, true, true).execute(new String[]{Constants.URL_UPLOAD__WORKSHEET_TO_SERVER});
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.context, e.toString(), Toast.LENGTH_SHORT).show();
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
                if (((Integer) jObj.get(Constants.RESPONSE_KEY_CODE)).intValue() == 1) {
                    if (webserviceCb.equalsIgnoreCase(Constants.URL_UPLOAD__WORKSHEET_TO_SERVER)) {
                        JSONObject resobj = jObj.getJSONObject(Constants.RESPONSE_KEY_OBJ);
                        JSONArray serviceImagname = resobj.getJSONArray("extra_service");
                        String serverWorksheetId = resobj.getString("work_id");
                        Preference.setSharedPref(Constants.PREF_WORK_ID, serverWorksheetId);
                        this.dbHelper = new DatabaseHelper(this.context);
                        if (this.workSheet.getAttachementList() != null && this.workSheet.getAttachementList().size() > 0) {
                            this.dbHelper.saveAttachments(this.workSheet.getAttachementList(), serverWorksheetId);
                        }
                        if (this.arrExtraService != null) {
                            int i = 0;
                            while (i < serviceImagname.length() && i < this.arrExtraService.size()) {
                                String serviceId = serviceImagname.getJSONObject(i).getString("service_id");
                                this.dbHelper.saveAttachments(((WorkServiceMap) this.arrExtraService.get(i)).getAttachmentList(), serviceId);
                                i++;
                            }
                        }
                        this.actionCallback.onActionComplete(1, webserviceCb, jObj);
                        return;
                    }
                    if (webserviceCb.equalsIgnoreCase(Constants.URL_MYTASK_WORKSHEET_DETAIL)) {
                        JSONObject jobject = jObj;
                        this.actionCallback.onActionComplete(1, webserviceCb, jObj.getJSONObject(Constants.RESPONSE_KEY_OBJ));
                        return;
                    }
                    if (webserviceCb.equalsIgnoreCase(Constants.URL_UPLOAD_IMAGES_TO_SERVER)) {
                        File file = new File((String) this.imgData.get("attachment_name"));
                        if (file.exists()) {
                            file.delete();
                        }
                        this.actionCallback.onActionComplete(1, webserviceCb, jObj);
                        return;
                    }
                    return;
                }
                this.actionCallback.onActionComplete(0, webserviceCb, jObj.get(Constants.RESPONSE_KEY_MSG).toString());

            }catch (Exception e) {
                e.printStackTrace();
                this.actionCallback.onActionComplete(0, webserviceCb, e.getMessage().toString() + "");
            }
        }
    }
}
