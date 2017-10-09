package com.onjyb.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import com.onjyb.Constants;

public class notificationUtil {
    public static boolean isAddUpdateGCMcalled(Context context, String userid) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getBoolean(userid, false);
    }

    public static void saveUSerGCM(Context context, String userid) {
        Editor edit = context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).edit();
        edit.putBoolean(userid, true);
        edit.commit();
    }

    public static void saveGCMSENDERID(Context context, String senderID) {
        Editor edit = context.getSharedPreferences("sender_pref", 0).edit();
        edit.putString("senderID", senderID);
        edit.commit();
    }

    public static String getGCMSENDERID(Context context) {
        return Preference.getSharedPref(Constants.PREF_SENDER_ID, null);
    }

    public static void setAgeVerified(Context context, boolean verified) {
        Editor edit = context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).edit();
        edit.putBoolean("AgeVerified", verified);
        edit.commit();
    }

    public static void setEmailId(Context context, String emailId) {
        if (emailId != null && emailId.length() > 0) {
            Editor edit = context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).edit();
            edit.putString("emailID", emailId);
            edit.commit();
        }
    }

    public static boolean getAgeVerified(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getBoolean("AgeVerified", false);
    }

    public static String getFirstName(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getString("FirstName", null);
    }

    public static String getLastName(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getString("LastName", null);
    }

    public static void saveUserPhone(Context context, String phone) {
        Editor edit = context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).edit();
        edit.putString("phoneNo", phone);
        edit.commit();
    }

    public static String getUserPhone(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getString("phoneNo", "");
    }

    public static String getUserEmail(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getString("emailID", "");
    }

    public static void saveAccessToken(Context context, String access_token) {
        Editor edit = context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).edit();
        System.out.println("=========saving access_token==========" + access_token);
        edit.putString("access_token", access_token);
        edit.commit();
    }

    public static String getAccessToken(Context context) {
        return context.getSharedPreferences(Constants.RESPONSE_KEY_USER_DETAIL, 0).getString("access_token", "");
    }
}
