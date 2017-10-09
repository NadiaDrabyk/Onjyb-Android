package com.onjyb.reqreshelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.layout.UpdateAvailableActivity;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

public class ETechAsyncTask extends AsyncTask<String, Void, String> {
    public static final int CANCELED = 101;
    public static final int COMPLETED = 102;
    private static final String CRLF = "\r\n";
    public static final int ERROR = 103;
    public static final int ERROR_NETWORK = 104;
    public static final int REQUEST_METHOD_GET = 1;
    public static final int REQUEST_METHOD_POST = 2;
    private static final String boundary = "---------------------------";
    private static final String twoHyphens = "--";
    private final String TAG;
    private AsyncTaskCompleteListener<String> callback;
    private final CommonClass checkConnection;
    private Context context;
    private HttpClient httpClient;
    private HttpPost httpPost;
    private HttpGet httpget;
    public boolean isJson;
    public boolean isMultiPartData;
    private Boolean isNetAvailable;
    private final OnDismissListener onDismissListener;
    private HashMap<String, Object> paramValues;
    private ProgressDialog progressDialog;
    private int requestMethod;
    private HttpResponse response;
    private String responseString;
    public boolean showProgressDialog;
    public int status;
    private String webserviceCb;

    class C07931 implements OnDismissListener {
        C07931() {
        }

        public void onDismiss(DialogInterface dialog) {
            if (!ETechAsyncTask.this.isCancelled() && ETechAsyncTask.this.cancel(false)) {
                ETechAsyncTask.this.responseString = "Loading canced.";
            }
        }
    }

    public ETechAsyncTask(Context context, AsyncTaskCompleteListener<String> cb, String webserviceCb, HashMap<String, Object> paramValues) {
        this(context, cb, webserviceCb, paramValues, 1, false, true);
    }

    public ETechAsyncTask(Context context, AsyncTaskCompleteListener<String> cb, String webserviceCb, HashMap<String, Object> paramValues, int requestMethod) {
        this(context, cb, webserviceCb, paramValues, requestMethod, false, true);
    }

    public ETechAsyncTask(Context context, AsyncTaskCompleteListener<String> cb, String webserviceCb, HashMap<String, Object> paramValues, int requestMethod, boolean isMultiPartData, boolean showProgressDialog) {
        this.TAG = "ETechAsyncTask";
        this.context = null;
        this.status = 103;
        this.isMultiPartData = false;
        this.isJson = false;
        this.showProgressDialog = true;
        this.callback = null;
        this.progressDialog = null;
        this.responseString = "";
        this.paramValues = null;
        this.isNetAvailable = Boolean.valueOf(false);
        this.requestMethod = 1;
        this.onDismissListener = new C07931();
        this.webserviceCb = "";
        this.context = context;
        this.callback = cb;
        this.showProgressDialog = showProgressDialog;
        this.webserviceCb = webserviceCb;
        if (paramValues != null) {
            this.paramValues = paramValues;
            Log.v("map", paramValues.toString());
        }
        this.requestMethod = requestMethod;
        this.isMultiPartData = isMultiPartData;
        if (showProgressDialog) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(false);
        }
        this.checkConnection = new CommonClass(context);
    }

    public ETechAsyncTask(Context context, AsyncTaskCompleteListener<String> cb, String webserviceCb, HashMap<String, Object> paramValues, int requestMethod, boolean isMultiPartData, boolean isJson, boolean showProgressDialog) {
        this.TAG = "ETechAsyncTask";
        this.context = null;
        this.status = 103;
        this.isMultiPartData = false;
        this.isJson = false;
        this.showProgressDialog = true;
        this.callback = null;
        this.progressDialog = null;
        this.responseString = "";
        this.paramValues = null;
        this.isNetAvailable = Boolean.valueOf(false);
        this.requestMethod = 1;
        this.onDismissListener = new C07931();
        this.webserviceCb = "";
        this.context = context;
        this.callback = cb;
        this.webserviceCb = webserviceCb;
        this.isJson = isJson;
        this.requestMethod = requestMethod;
        this.isMultiPartData = isMultiPartData;
        this.showProgressDialog = showProgressDialog;
        if (paramValues != null) {
            this.paramValues = paramValues;
            Log.v("map", paramValues.toString());
        }
        if (showProgressDialog) {
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setCancelable(false);
        }
        this.checkConnection = new CommonClass(context);
    }

    public void hideProgressDialog() {
        this.progressDialog = null;
        this.showProgressDialog = false;
    }

    protected void onPreExecute() {
        if (this.progressDialog != null) {
            this.progressDialog.setMessage("Loading...");
            this.progressDialog.show();
            this.progressDialog.setOnDismissListener(this.onDismissListener);
        }
        this.isNetAvailable = Boolean.valueOf(this.checkConnection.isConnectingToInternet());
    }

    protected String doInBackground(String... url) {
        if (this.isNetAvailable.booleanValue()) {
            this.paramValues.putAll(AppUtils.getDefaultPerameters(this.context));
            try {
                HttpEntity entity;
                String strUrl = url[0];
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(httpParameters, 120000);
                this.httpClient = new DefaultHttpClient(httpParameters);
                Iterator it;
                if (this.requestMethod == 1) {
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
                    if (this.paramValues != null) {
                        it = new ArrayList(this.paramValues.keySet()).iterator();
                        while (it.hasNext()) {
                            String string = (String) it.next();
                            strUrl = strUrl + string + "=" + this.paramValues.get(string) + "&";
                        }
                    }
                    this.httpget = new HttpGet(strUrl);
                    this.response = this.httpClient.execute(this.httpget);
                } else {
                    String strOnlyURL = strUrl;
                    if (this.isMultiPartData) {
                        HttpConnectionParams.setConnectionTimeout(httpParameters, 600000);
                        entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                        for (String key : this.paramValues.keySet()) {
                            Object objValue = this.paramValues.get(key);
                            if (objValue instanceof FileObject) {
                                writeFile((MultipartEntity) entity, key, objValue);
                            } else {
                                writeField((MultipartEntity) entity, key, this.paramValues.get(key).toString());
                            }
                        }
                        this.httpPost = new HttpPost(strOnlyURL);
                        this.httpPost.setEntity(entity);
                    } else if (this.isJson) {
                        this.httpPost = new HttpPost(strOnlyURL);
                        this.httpPost.setEntity(new StringEntity(((JSONObject) this.paramValues.get("jsondata")).toString(), "UTF8"));
                    } else {
                        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
                        List<NameValuePair> pairs = new ArrayList();
                        for (String strKey : this.paramValues.keySet()) {
                            String strKey2 = null;
                            String strValue = this.paramValues.get(strKey2).toString();
                            if (strKey2 != null) {
                                pairs.add(new BasicNameValuePair(strKey2, strValue));
                            }
                        }
                        this.httpPost = new HttpPost(strOnlyURL);
                        if (pairs.size() > 0) {
                            this.httpPost.setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
                        }
                    }
                    this.response = this.httpClient.execute(this.httpPost);
                    Log.v("response code", this.response.getStatusLine().getStatusCode() + "");
                }
                entity = this.response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line + StringUtils.LF);
                        Log.d("ParsingData", "Response String : " + line);
                    }
                    this.status = 102;
                    this.responseString = sb.toString();
                    Log.d("DataParsigFile", "Status Get : " + this.status + ", Response String : " + this.responseString);
                    instream.close();
                }
            } catch (UnknownHostException e) {
                Log.e("ETechAsyncTask", " doInBackground > UnknownHostException : " + e);
                this.responseString = e.getMessage();
                this.status = 103;
            } catch (Exception e2) {
                Log.e("ETechAsyncTask", " doInBackground > Exception : " + e2, e2);
                this.responseString = e2.getMessage();
                this.status = 103;
            }
            return this.responseString;
        }
        this.status = 104;
        cancel(this.isNetAvailable.booleanValue());
        return null;
    }

    private void printentity(MultipartEntity entity) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream((int) entity.getContentLength());
            entity.writeTo(out);
            byte[] entityContentAsBytes = out.toByteArray();
            Log.e("printentity", "entityContentAsString : " + new String(out.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeField(MultipartEntity entity, String key, String value) {
        try {
            Log.d("ETechAsyncTask", "==============-------->value: " + value);
            entity.addPart(new FormBodyPart(key, new StringBody(value, Charset.forName("UTF-8"))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(MultipartEntity entity, String key, Object objValue) {
        FileObject fileObj = (FileObject) objValue;
        entity.addPart(key, new ByteArrayBody(fileObj.getByteData(), fileObj.getContentType(), fileObj.getFileName()));
    }

    public void cancelTask() {
        try {
            Log.e("ETechAsyncTask", "CANCELED");
            this.status = 101;
            cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onProgressUpdate() {
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    @SuppressLint({"LongLogTag"})
    protected void onPostExecute(String responseString) {
        super.onPostExecute(responseString);
        try {
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
            }
        } catch (Exception e) {
            Log.e("ETechAsyncTask", e.toString());
        }
        Log.e("ETechAsyncTask:onPostExecute()", "status : " + this.status + ", Response : " + responseString);
        if (responseString != null) {
            String username = Preference.getSharedPref(Constants.PREF_FILE_DBUSER_USERNAME, "");
            if (!(username == null || username.equalsIgnoreCase(""))) {
//jin                checkforUpadeVesion(responseString);
            }
            this.callback.onTaskComplete(this.status, responseString, this.webserviceCb, "");
            return;
        }
        this.callback.onTaskComplete(this.status, this.context.getString(R.string.msg_error_network), this.webserviceCb, "");
    }

    private void checkforUpadeVesion(String responseString) {
        try {
            JSONObject jObj = new JSONObject(responseString);
            if (((Integer) jObj.get(Constants.RESPONSE_KEY_CODE)).intValue() == 1) {
                JSONObject Android_detail = jObj.getJSONObject("upgrade").getJSONObject("Android");
                if (Android_detail != null) {
                    String forcetoupdate = Android_detail.getString("forceUpdateApp");
                    String isVersionDifferent = Android_detail.getString("isVersionDifferent");
                    String MessageType = Android_detail.getString("MessageType");
                    String update_button_title = Android_detail.getString("update_button_title");
                    String skip_button_title = Android_detail.getString("skip_button_title");
                    if (forcetoupdate != null && forcetoupdate.equalsIgnoreCase("yes")) {
                        Constants.updateActivityCall = true;
                    }
                    Preference.setSharedPref(Constants.updateUrl, Android_detail.getString("URL"));
                    Preference.setSharedPref(Constants.forceUpdateApp, forcetoupdate);
                    Preference.setSharedPref(Constants.isVersionDifferent, isVersionDifferent);
                    Preference.setSharedPref("message", MessageType);
                    Preference.setSharedPref(Constants.update_btn_name, update_button_title);
                    Preference.setSharedPref(Constants.skip_btn_name, skip_button_title);
                    if (isVersionDifferent != null && isVersionDifferent.equalsIgnoreCase("yes")) {
                        try {
                            PackageInfo pInfo = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0);
                            if (!(pInfo.versionName == null || pInfo.versionName.equalsIgnoreCase(""))) {
                                Preference.setSharedPref(Constants.Current_version_app, pInfo.versionName);
                            }
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent();
                        intent.setClass(this.context, UpdateAvailableActivity.class);
                        if (forcetoupdate != null && forcetoupdate.equalsIgnoreCase("yes")) {
                            intent.setFlags(67108864);
                        }
                        if (Constants.updateActivityCall) {
                            Constants.updateActivityCall = false;
                            this.context.startActivity(intent);
                            if (forcetoupdate != null && forcetoupdate.equalsIgnoreCase("yes")) {
                                ((Activity) this.context).finish();
                            }
                        }
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @SuppressLint({"LongLogTag"})
    protected void onCancelled() {
        super.onCancelled();
        try {
            if (this.progressDialog != null) {
                this.progressDialog.dismiss();
            }
        } catch (Exception e) {
        }
        this.status = 101;
        Log.e("ETechAsyncTask:onCancelled()", "status : " + this.status);
        this.callback.onTaskComplete(this.status, this.context.getString(R.string.msg_error_network), this.webserviceCb, "");
    }

    private void writeFormField(ByteArrayOutputStream baos, String fieldName, String fieldValue) throws IOException {
        baos.write("-----------------------------\r\n".getBytes());
        baos.write(("Content-Disposition: form-data;name=\"" + fieldName + "\"" + CRLF).getBytes("UTF-8"));
        baos.write((CRLF + fieldValue + CRLF).getBytes());
    }

    private void writeFileField(ByteArrayOutputStream baos, String fieldName, String fileName, String contentType, byte[] buf) throws IOException {
        baos.write("-----------------------------\r\n".getBytes());
        baos.write(("Content-Disposition: form-data;name=\"" + fieldName + "\";filename=\"" + fileName + "\"" + CRLF).getBytes("UTF-8"));
        baos.write(("Content-Type: " + contentType + CRLF + CRLF).getBytes());
        writeFile(baos, buf);
        baos.write(CRLF.getBytes());
    }

    private void writeFile(ByteArrayOutputStream baos, byte[] buf) {
        try {
            byte[] data = new byte[20000];
            InputStream fileInputStream = new ByteArrayInputStream(buf);
            while (true) {
                int nRead = fileInputStream.read(data, 0, data.length);
                if (nRead != -1) {
                    Log.e("", "writeFile :: nRead : " + nRead);
                    baos.write(data, 0, nRead);
                } else {
                    fileInputStream.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
