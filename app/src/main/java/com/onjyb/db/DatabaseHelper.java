package com.onjyb.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.util.Log;
import com.onjyb.Constants;
import com.onjyb.util.Preference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static String DB_NAME = "Onjyb.sqlite";
    public static String DB_NAME_NEW = "Onjybnew.sqlite";
    public static String DB_NAME_NEW1 = "Onjybnew1.sqlite";
    private static DatabaseHelper mDBConnection;
    private String DB_PATH;
    private final String TAG = DatabaseHelper.class.getName();
    Context context = null;
    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME_NEW1, null, 1);
        if (VERSION.SDK_INT >= 17) {
            this.DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            this.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
        try {
            createDataBase();
            openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseHelper getDBAdapterInstance(Context context) {
        DatabaseHelper databaseHelper;
        synchronized (DatabaseHelper.class) {
            if (mDBConnection == null) {
                Log.v(DatabaseHelper.class + "", " :: getDBAdapterInstance :: Creating Instance mDBConnection");
                mDBConnection = new DatabaseHelper(context);
            } else {
                Log.v(DatabaseHelper.class + "", " :: getDBAdapterInstance :: mDBConnection not null..");
            }
            databaseHelper = mDBConnection;
        }
        return databaseHelper;
    }

    public static synchronized void clearInsance() {
        synchronized (DatabaseHelper.class) {
            mDBConnection = null;
        }
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void createTable(String tableName, HashMap<String, String> primaryKey, HashMap<String, String> columns) {
        try {
            String columnName;
            StringBuilder createTableQuery = new StringBuilder("Create Table ");
            createTableQuery.append(tableName + " ( ");
            if (primaryKey != null) {
                columnName = (String) new ArrayList(primaryKey.keySet()).get(0);
                createTableQuery.append(columnName + " " + ((String) primaryKey.get(columnName)) + " INTEGER PRIMARY KEY, ");
            }
            if (columns != null) {
                ArrayList<String> columnNames = new ArrayList(columns.keySet());
                for (int i = 0; i < columnNames.size(); i++) {
                    columnName = (String) columnNames.get(i);
                    if (i == columnNames.size() - 1) {
                        createTableQuery.append(columnName + " " + ((String) columns.get(columnName)));
                    } else {
                        createTableQuery.append(columnName + " " + ((String) columns.get(columnName)) + ",");
                    }
                }
            }
            createTableQuery.append(")");
            this.db.execSQL(createTableQuery.toString());
        } catch (Exception e) {
            Log.e(this.TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void createDataBase() throws IOException {
        if (!checkDataBase()) {
            getReadableDatabase();
            close();
            try {
                copyDataBase();
                Log.e(this.TAG, "createDatabase database created");
            } catch (IOException e) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(this.DB_PATH + DB_NAME_NEW1);
        File dbFileold1 = new File(this.DB_PATH + DB_NAME_NEW);
        File dbFileold = new File(this.DB_PATH + DB_NAME);
        if (dbFileold.exists()) {
            dbFileold.delete();
            Log.d(this.TAG, ">>>checkDataBase() " + dbFileold.getPath());
        }
        if (dbFileold1.exists()) {
            dbFileold1.delete();
            Log.d(this.TAG, ">>>checkDataBase() " + dbFileold1.getPath());
        }
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = this.context.getAssets().open(DB_NAME_NEW1);
        String outFileName = this.DB_PATH + DB_NAME_NEW1;
        Log.v("outFileName", outFileName);
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        while (true) {
            int mLength = mInput.read(mBuffer);
            if (mLength > 0) {
                mOutput.write(mBuffer, 0, mLength);
            } else {
                mOutput.flush();
                mOutput.close();
                mInput.close();
                return;
            }
        }
    }

    public boolean openDataBase() throws SQLException {
        this.db = SQLiteDatabase.openDatabase(this.DB_PATH + DB_NAME_NEW1, null, 268435456);
        return this.db != null;
    }

    public synchronized void close() {
        if (this.db != null) {
            this.db.close();
        }
        super.close();
    }

    public void deleteDatabase() {
        this.db.execSQL("DELETE FROM  tblproject");
        this.db.execSQL("DELETE FROM  tblservice");
        this.db.execSQL("DELETE FROM  tblassociateservice");
        this.db.execSQL("DELETE FROM  tblbranch");
        this.db.execSQL("DELETE FROM  tblemployee");
        this.db.execSQL("DELETE FROM  tblleavetype");
    }

    public long insertRecordsInDB(String tableName, ContentValues initialValues) {
        long count = -1;
        try {
            count = this.db.insert(tableName, null, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int updateRecordsInDB(String tableName, ContentValues initialValues, String whereClause, String[] whereArgs) {
        int count = -1;
        try {
            count = this.db.update(tableName, initialValues, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int deleteRecordInDB(String tableName, String whereClause, String[] whereArgs) {
        int count = -1;
        try {
            count = this.db.delete(tableName, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @SuppressLint({"LongLogTag"})
    public ArrayList<HashMap<String, String>> selectRecordsFromDBList(String query, String[] selectionArgs) {
        Exception e;
        Throwable th;
        ArrayList<HashMap<String, String>> retList = null;
        Cursor cursor = null;
        try {
            Log.d("selectRecordsFromDB() :", "query : " + query);
            cursor = this.db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                ArrayList<HashMap<String, String>> retList2 = new ArrayList();
                do {
                    try {
                        HashMap<String, String> mapRow = new HashMap();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            mapRow.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        retList2.add(mapRow);
                    } catch (Exception e2) {
                        e = e2;
                        retList = retList2;
                    } catch (Throwable th2) {
                        th = th2;
                        retList = retList2;
                    }
                } while (cursor.moveToNext());
                retList = retList2;
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
            return retList;
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
//                throw th;
            }
            return null;
        }
    }

    public ArrayList<HashMap<String, String>> selectRecordsOlnyOneField(String query, String[] selectionArgs) {
        Exception e;
        Throwable th;
        ArrayList<HashMap<String, String>> retList = null;
        Cursor cursor = null;
        try {
            Log.d("selectRecordsFromDB() :", "query : " + query);
            cursor = this.db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                ArrayList<HashMap<String, String>> retList2 = new ArrayList();
                do {
                    try {
                        HashMap<String, String> mapRow = new HashMap();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            mapRow.put(cursor.getColumnName(i), cursor.getString(i));
                        }
                        retList2.add(mapRow);
                    } catch (Exception e2) {
                        e = e2;
                        retList = retList2;
                    } catch (Throwable th2) {
                        th = th2;
                        retList = retList2;
                    }
                } while (cursor.moveToNext());
                retList = retList2;
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
            return retList;
        } catch (Exception e3) {
            e = e3;
            try {
                Log.e(this.TAG, e.toString());
                e.printStackTrace();
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
//                throw th;
            }
            return null;
        }
    }

    @SuppressLint({"LongLogTag"})
    public HashMap<String, String> getEmployee(String empId) {
        Exception e;
        Throwable th;
        HashMap<String, String> hashMap = null;
        Cursor cursor = null;
        try {
            cursor = this.db.rawQuery("SELECT * FROM tblemployee WHERE employee_id = " + empId, null);
            if (cursor.moveToFirst()) {
                HashMap<String, String> mapRow = new HashMap();
                int i = 0;
                while (i < cursor.getColumnCount()) {
                    try {
                        mapRow.put(cursor.getColumnName(i), cursor.getString(i));
                        i++;
                    } catch (Exception e2) {
                        e = e2;
                        hashMap = mapRow;
                    } catch (Throwable th2) {
                        th = th2;
                        hashMap = mapRow;
                    }
                }
                hashMap = mapRow;
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e3) {
            e = e3;
            try {
                Log.e("getEmployee()", "Exception: " + e.getMessage());
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
//                throw th;
            }
        }
        return hashMap;
    }

    @SuppressLint({"LongLogTag"})
    public HashMap<String, String> selSingleRecordFromDB(String query, String[] selectionArgs) {
        Exception e;
        Throwable th;
        HashMap<String, String> hashMap = null;
        Cursor cursor = null;
        try {
            Log.v(" : selSingleRecordFromDB() : ", "query : " + query);
            cursor = this.db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                HashMap<String, String> mapRow = new HashMap();
                int i = 0;
                while (i < cursor.getColumnCount()) {
                    try {
                        mapRow.put(cursor.getColumnName(i), cursor.getString(i));
                        i++;
                    } catch (Exception e2) {
                        e = e2;
                        hashMap = mapRow;
                    } catch (Throwable th2) {
                        th = th2;
                        hashMap = mapRow;
                    }
                }
                hashMap = mapRow;
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    int count = cursor.getCount();
                }
            }
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        } catch (Exception e3) {
            e = e3;
            try {
                Log.e(this.TAG, e.toString());
                hashMap = null;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
//                return hashMap;
            } catch (Throwable th3) {
                th = th3;
                if (!(cursor == null || cursor.isClosed())) {
                    cursor.close();
                }
//                throw th;
            }
        }
        return hashMap;
    }

    public boolean bulkInsertWithJSON(String strTableName, ArrayList<String> arrColumnsToInsert, JSONArray arrayOfData) {
        String method = "bulkInsertWithJSON";
        if (arrColumnsToInsert == null) {
            return false;
        }
        try {
            if (arrColumnsToInsert.size() == 0 || arrayOfData == null || arrayOfData.length() == 0) {
                return false;
            }
            StringBuffer sbInsertQuery;
            this.db.execSQL("delete from " + strTableName);
            StringBuffer sbInsertBaseQuery = new StringBuffer("INSERT INTO " + strTableName + (" (" + arrColumnsToInsert.toString().substring(1, arrColumnsToInsert.toString().length() - 1) + ") ") + " VALUES ");
            int recordCountToInsert = 0;
            int i = 0;
            StringBuffer sbInsertQuery2 = sbInsertBaseQuery;
            while (i < arrayOfData.length()) {
                JSONObject obj = arrayOfData.getJSONObject(i);
                StringBuffer sbDataElement = new StringBuffer("(");
                for (int j = 0; j < arrColumnsToInsert.size(); j++) {
                    sbDataElement.append("'" + obj.get((String) arrColumnsToInsert.get(j)).toString().replaceAll("'", "''") + "',");
                }
                StringBuffer sbDataElement2 = new StringBuffer(sbDataElement.substring(0, sbDataElement.length() - 1));
                sbDataElement2.append("),");
                sbInsertQuery2.append(sbDataElement2);
                recordCountToInsert++;
                if (recordCountToInsert % 400 == 0) {
                    this.db.execSQL(new StringBuffer(sbInsertQuery2.substring(0, sbInsertQuery2.length() - 1)).toString());
                    sbInsertQuery = sbInsertBaseQuery;
                    recordCountToInsert = 0;
                } else {
                    sbInsertQuery = sbInsertQuery2;
                }
                i++;
                sbInsertQuery2 = sbInsertQuery;
            }
            if (recordCountToInsert > 0) {
                this.db.execSQL(new StringBuffer(sbInsertQuery2.substring(0, sbInsertQuery2.length() - 1)).toString());
                sbInsertQuery = sbInsertBaseQuery;
            } else {
                sbInsertQuery = sbInsertQuery2;
            }
            return true;
        } catch (Exception e) {
            Log.e(this.TAG, e.toString());
            return false;
        }
    }

    public void inserOrReplaceEmployeeDetail(ArrayList<Employee> employees) {
        String METHOD = "insertorReplace";
        String STATEMENT_INSERT_REPLACE_WORK_SERVICE = "Insert or replace into tblemployee(employee_id,first_name,last_name)values (?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblemployee(employee_id,first_name,last_name)values (?,?,?)");
            Iterator it = employees.iterator();
            while (it.hasNext()) {
                Employee employee = (Employee) it.next();
                try {
                    String qry = "select * from tblemployee where employee_id='" + employee.getEmployeeId() + "'";
                    getDBAdapterInstance(this.context);
                    HashMap<String, String> list = selSingleRecordFromDB(qry, null);
                    if (list == null || list.size() <= 0) {
                        stmt.bindString(1, String.valueOf(employee.getEmployeeId()) == null ? "" : String.valueOf(employee.getEmployeeId()));
                        stmt.bindString(2, String.valueOf(employee.getFirstName()) == null ? "" : String.valueOf(employee.getFirstName()));
                        stmt.bindString(3, String.valueOf(employee.getLastName()) == null ? "" : String.valueOf(employee.getLastName()));
                        stmt.execute();
                        stmt.clearBindings();
                    } else {
                        ContentValues convalue = new ContentValues();
                        convalue.put("employee_id", employee.getEmployeeId());
                        convalue.put("first_name", employee.getFirstName());
                        convalue.put("last_name", employee.getLastName());
                        updateRecordsInDB("tblemployee", convalue, "employee_id = ?", new String[]{employee.getEmployeeId()});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertUser() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void inserOrReplaceProjectDetail(ArrayList<Project> projectDetails) {
        String METHOD = "insertprojectDetail";
        String STATEMENT_INSERT_REPLACE_PROJECT_DETAIL = "Insert or replace into tblproject(project_id,project_name,ref_company_id,project_status,create_date,update_date,ref_manager_id,project_number)values (?,?,?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblproject(project_id,project_name,ref_company_id,project_status,create_date,update_date,ref_manager_id,project_number)values (?,?,?,?,?,?,?,?)");
            Iterator it = projectDetails.iterator();
            while (it.hasNext()) {
                Project projectDetail = (Project) it.next();
                if (projectDetail != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(projectDetail.getproDetailId()) == null ? "" : String.valueOf(projectDetail.getproDetailId()));
                        stmt.bindString(2, String.valueOf(projectDetail.getProName()) == null ? "" : String.valueOf(projectDetail.getProName()));
                        stmt.bindString(3, String.valueOf(projectDetail.getRef_company_id()) == null ? "" : String.valueOf(projectDetail.getRef_company_id()));
                        stmt.bindString(4, String.valueOf(projectDetail.getProjectStatus()) == null ? "" : String.valueOf(projectDetail.getProjectStatus()));
                        stmt.bindString(5, String.valueOf(projectDetail.getCreateDate()) == null ? "" : String.valueOf(projectDetail.getCreateDate()));
                        stmt.bindString(6, String.valueOf(projectDetail.getUpdateDate()) == null ? "" : String.valueOf(projectDetail.getUpdateDate()));
                        stmt.bindString(7, String.valueOf(projectDetail.getRef_manager_id()) == null ? "" : String.valueOf(projectDetail.getRef_manager_id()));
                        if (String.valueOf(projectDetail.getProject_Number()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(projectDetail.getProject_Number());
                        }
                        stmt.bindString(8, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertUser() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void inserOrReplaceService(ArrayList<Service> services) {
        String METHOD = "insertprojectDetail";
        String STATEMENT_INSERT_REPLACE_PROJECT_DETAIL = "Insert or replace into tblservice(service_id,ref_company_id,service_name,service_status,create_date,update_date)values (?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblservice(service_id,ref_company_id,service_name,service_status,create_date,update_date)values (?,?,?,?,?,?)");
            Iterator it = services.iterator();
            while (it.hasNext()) {
                Service service = (Service) it.next();
                if (service != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(service.getId()) == null ? "" : String.valueOf(service.getId()));
                        stmt.bindString(2, String.valueOf(service.getRef_company_id()) == null ? "" : String.valueOf(service.getRef_company_id()));
                        stmt.bindString(3, String.valueOf(service.getServiceName()) == null ? "" : String.valueOf(service.getServiceName()));
                        stmt.bindString(4, String.valueOf(service.getServiceStatus()) == null ? "" : String.valueOf(service.getServiceStatus()));
                        stmt.bindString(5, String.valueOf(service.getCreateDate()) == null ? "" : String.valueOf(service.getCreateDate()));
                        if (String.valueOf(service.getUpdateDate()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(service.getUpdateDate());
                        }
                        stmt.bindString(6, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertUser() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void insertOrReplaceBranch(ArrayList<Branch> branches) {
        String METHOD = "insertprojectDetail";
        String STATEMENT_INSERT_REPLACE_BRANCH_DETAIL = "Insert or replace into tblbranch(branch_id,ref_company_id,branch_name,branch_status,create_date,update_date)values (?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblbranch(branch_id,ref_company_id,branch_name,branch_status,create_date,update_date)values (?,?,?,?,?,?)");
            Iterator it = branches.iterator();
            while (it.hasNext()) {
                Branch branch = (Branch) it.next();
                if (branch != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(branch.getBranchid()) == null ? "" : String.valueOf(branch.getBranchid()));
                        stmt.bindString(2, String.valueOf(branch.getRef_company_id()) == null ? "" : String.valueOf(branch.getRef_company_id()));
                        stmt.bindString(3, String.valueOf(branch.getBranchName()) == null ? "" : String.valueOf(branch.getBranchName()));
                        stmt.bindString(4, String.valueOf(branch.getBranchStatus()) == null ? "" : String.valueOf(branch.getBranchStatus()));
                        stmt.bindString(5, String.valueOf(branch.getCreateDate()) == null ? "" : String.valueOf(branch.getCreateDate()));
                        if (String.valueOf(branch.getUpdateDate()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(branch.getUpdateDate());
                        }
                        stmt.bindString(6, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertUser() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void inserOrReplaceLeaveType(ArrayList<LeaveType> leaveTypes) {
        String METHOD = "insertorReplaceLeaveType";
        String STATEMENT_INSERT_REPLACE_LEAVE_TYPE = "Insert or replace into tblleavetype(leavetype_id,ref_company_id,leave_type,leave_status,create_date,update_date,leave_master_type)values (?,?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblleavetype(leavetype_id,ref_company_id,leave_type,leave_status,create_date,update_date,leave_master_type)values (?,?,?,?,?,?,?)");
            Iterator it = leaveTypes.iterator();
            while (it.hasNext()) {
                LeaveType leaveType = (LeaveType) it.next();
                if (leaveType != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(leaveType.getLeaveTypeId()) == null ? "" : String.valueOf(leaveType.getLeaveTypeId()));
                        stmt.bindString(2, String.valueOf(leaveType.getRef_company_id()) == null ? "" : String.valueOf(leaveType.getRef_company_id()));
                        stmt.bindString(3, String.valueOf(leaveType.getLeavetypename()) == null ? "" : String.valueOf(leaveType.getLeavetypename()));
                        stmt.bindString(4, String.valueOf(leaveType.getLeavetypestatus()) == null ? "" : String.valueOf(leaveType.getLeavetypestatus()));
                        stmt.bindString(5, String.valueOf(leaveType.getCreateDate()) == null ? "" : String.valueOf(leaveType.getCreateDate()));
                        stmt.bindString(6, String.valueOf(leaveType.getUpdateDate()) == null ? "" : String.valueOf(leaveType.getUpdateDate()));
                        if (String.valueOf(leaveType.getLeave_master_type()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(leaveType.getLeave_master_type());
                        }
                        stmt.bindString(7, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertLeaveType() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public long getLastInsertId(String tablename) {
        long index = 0;
        Cursor cursor = getWritableDatabase().query("sqlite_sequence", new String[]{"seq"}, "name = ?", new String[]{tablename}, null, null, null, null);
        if (cursor.moveToFirst()) {
            index = cursor.getLong(cursor.getColumnIndex("seq"));
        }
        cursor.close();
        return index;
    }

    public void insertOrUpdateWorkSheet(WorkSheet workSheet) {
        String METHOD = "inserTaskData";
        String TABLE_NAME = "tblworksheet";
        String WORKSEET_ID = "worksheet_id";
        String REF_PROJECT_ID = "ref_project_id";
        String REF_BRANCH_ID = "ref_branch_id";
        String REF_SERVICE_ID = "ref_service_id";
        String WORK_DATE = "work_date";
        String WORK_HOURS = "work_hours";
        String WORK_OVER_TIME = "work_overtime";
        String KM_DRIVE = "km_drive";
        String BREAK_TIME = "break_time";
        String TOTAL_WORK_TIME = "total_work_time";
        String COMMENTS = "comments";
        String CHNG_WORK_HRS = "change_work_hours";
        String CHNG_WORK_KM = "change_km_drive";
        String CHNG_OVER_TIME = "change_over_time";
        String PROJECT_NAME = "project_name";
        String STATEMENT_INSERT_REPLACE_WORKSHEET_RECORD = "Insert  into tblworksheet(ref_service_id,work_date,work_hours,work_overtime,break_time,km_drive,total_work_time,comments,project_name,ref_project_id,ref_branch_id)values (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            try {
                getDBAdapterInstance(this.context);
                HashMap<String, String> list = selSingleRecordFromDB("select * from tblworksheet where worksheet_id='" + workSheet.getWorksheetId() + "'", null);
                if (list == null || list.size() <= 0) {
                    SQLiteStatement stmt = this.db.compileStatement("Insert  into tblworksheet(ref_service_id,work_date,work_hours,work_overtime,break_time,km_drive,total_work_time,comments,project_name,ref_project_id,ref_branch_id)values (?,?,?,?,?,?,?,?,?,?,?)");
                    if (workSheet != null) {
                        String str;
                        stmt.bindString(1, String.valueOf(workSheet.getRefServiceId()) == null ? "" : String.valueOf(workSheet.getRefServiceId()));
                        stmt.bindString(2, String.valueOf(workSheet.getWorkDate()) == null ? "" : String.valueOf(workSheet.getWorkDate()));
                        stmt.bindString(3, String.valueOf(workSheet.getWorkHrs()) == null ? "" : String.valueOf(workSheet.getWorkHrs()));
                        stmt.bindString(4, String.valueOf(workSheet.getWorkOverTime1()) == null ? "" : String.valueOf(workSheet.getWorkOverTime1()));
                        stmt.bindString(5, String.valueOf(workSheet.getBreakTime()) == null ? "" : String.valueOf(workSheet.getBreakTime()));
                        stmt.bindString(6, String.valueOf(workSheet.getkMDrive()) == null ? "" : String.valueOf(workSheet.getkMDrive()));
                        stmt.bindString(7, String.valueOf(workSheet.getTotalWorkTime()) == null ? "" : String.valueOf(workSheet.getTotalWorkTime()));
                        stmt.bindString(8, String.valueOf(workSheet.getComments()) == null ? "" : String.valueOf(workSheet.getComments()));
                        stmt.bindString(9, String.valueOf(workSheet.getProjectName()) == null ? "" : String.valueOf(workSheet.getProjectName()));
                        stmt.bindString(10, String.valueOf(workSheet.getRefProjectId()) == null ? "" : String.valueOf(workSheet.getRefProjectId()));
                        if (String.valueOf(workSheet.getRefBranchId()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(workSheet.getRefBranchId());
                        }
                        stmt.bindString(11, str);
                        stmt.execute();
                        stmt.clearBindings();
                    }
                    this.db.setTransactionSuccessful();
                }
                ContentValues convalue = new ContentValues();
                convalue.put("ref_service_id", workSheet.getRefServiceId());
                convalue.put("work_date", workSheet.getWorkDate());
                convalue.put("work_hours", workSheet.getWorkHrs());
                convalue.put("work_overtime", workSheet.getWorkOverTime1());
                convalue.put("break_time", workSheet.getBreakTime());
                convalue.put("km_drive", workSheet.getkMDrive());
                convalue.put("total_work_time", workSheet.getTotalWorkTime());
                convalue.put("comments", workSheet.getComments());
                convalue.put("project_name", workSheet.getProjectName());
                convalue.put("syn_status", workSheet.getSyncStatus());
                convalue.put("server_worksheet_id", workSheet.getServerWorkSheetId());
                updateRecordsInDB("tblworksheet", convalue, "worksheet_id = ?", new String[]{String.valueOf(getLastInsertId("tblworksheet"))});
                this.db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertworksheet() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public ArrayList<String> generateImageNames(ArrayList<Bitmap> bitmaps) {
        ArrayList<String> imgNameList = new ArrayList();
        String userId = Preference.getSharedPref(Constants.PREF_USER_ID, "");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SS");
        for (int i = 0; i < bitmaps.size(); i++) {
            String imgName = formatter.format(new Date()) + "_" + userId + ".png";
            Log.d("Image Name>>>>", imgName);
            imgNameList.add(imgName);
        }
        return imgNameList;
    }

    public void saveAttachments(ArrayList<AttachmentMap> imgNameList, String mapType, String mapTypeId) {
        String STATEMENT_INSERT_ATTACHMENT_MAP = "Insert into  tblattachmentmap(ref_map_id,attachment_name,map_name,attachment_status)values (?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert into  tblattachmentmap(ref_map_id,attachment_name,map_name,attachment_status)values (?,?,?,?)");
            for (int i = 0; i < imgNameList.size(); i++) {
                try {
                    stmt.bindString(1, String.valueOf(mapTypeId) == null ? "" : String.valueOf(mapTypeId));
                    stmt.bindString(2, String.valueOf(((AttachmentMap) imgNameList.get(i)).getAttachmentName()) == null ? "" : String.valueOf(((AttachmentMap) imgNameList.get(i)).getAttachmentName()));
                    stmt.bindString(3, String.valueOf(mapType) == null ? "" : String.valueOf(mapType));
                    stmt.bindString(4, String.valueOf("Active") == null ? "" : String.valueOf("Active"));
                    stmt.execute();
                    stmt.clearBindings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertAttachment():", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void saveAttachments(ArrayList<AttachmentMap> imgNameList, String serverAttachId) {
        String STATEMENT_INSERT_ATTACHMENT_MAP = "Insert into tblattachmentmap(attachment_name,map_name,server_attachment_id,attachment_status) values (?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert into tblattachmentmap(attachment_name,map_name,server_attachment_id,attachment_status) values (?,?,?,?)");
            for (int i = 0; i < imgNameList.size(); i++) {
                try {
                    stmt.bindString(1, String.valueOf(((AttachmentMap) imgNameList.get(i)).getAttachmentName()) == null ? "" : String.valueOf(((AttachmentMap) imgNameList.get(i)).getAttachmentName()));
                    stmt.bindString(2, String.valueOf(((AttachmentMap) imgNameList.get(i)).getMapName()) == null ? "" : String.valueOf(((AttachmentMap) imgNameList.get(i)).getMapName()));
                    stmt.bindString(3, String.valueOf(serverAttachId) == null ? "" : String.valueOf(serverAttachId));
                    stmt.bindString(4, String.valueOf("Active") == null ? "" : String.valueOf("Active"));
                    stmt.execute();
                    stmt.clearBindings();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : saveAttachments() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void inserOrReplaceAttachmentMap(ArrayList<AttachmentMap> attachmentMaps) {
        String METHOD = "insertorReplaceAttachmentmap";
        String STATEMENT_INSERT_REPLACE_ATTACHMENT_MAP = "Insert or replace into tblattachmentmap(attachment_id,ref_map_id,attachment_name,map_name,attachment_status,sync_status,server_attachment_id)values (?,?,?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblattachmentmap(attachment_id,ref_map_id,attachment_name,map_name,attachment_status,sync_status,server_attachment_id)values (?,?,?,?,?,?,?)");
            Iterator it = attachmentMaps.iterator();
            while (it.hasNext()) {
                AttachmentMap attachmentMap = (AttachmentMap) it.next();
                if (attachmentMap != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(attachmentMap.getAttachmentId()) == null ? "" : String.valueOf(attachmentMap.getAttachmentId()));
                        stmt.bindString(2, String.valueOf(attachmentMap.getRefMapId()) == null ? "" : String.valueOf(attachmentMap.getRefMapId()));
                        stmt.bindString(3, String.valueOf(attachmentMap.getAttachmentName()) == null ? "" : String.valueOf(attachmentMap.getAttachmentName()));
                        stmt.bindString(4, String.valueOf(attachmentMap.getMapName()) == null ? "" : String.valueOf(attachmentMap.getMapName()));
                        stmt.bindString(5, String.valueOf(attachmentMap.getAttachmentStatus()) == null ? "" : String.valueOf(attachmentMap.getAttachmentStatus()));
                        stmt.bindString(6, String.valueOf(attachmentMap.getSyncStatus()) == null ? "" : String.valueOf(attachmentMap.getSyncStatus()));
                        if (String.valueOf(attachmentMap.getServerWorkSheetId()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(attachmentMap.getServerWorkSheetId());
                        }
                        stmt.bindString(7, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertUser() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void insertOrReplaceWorkService(ArrayList<AssociateService> associateServices) {
        String METHOD = "insertorReplaceWorksheetService";
        String STATEMENT_INSERT_REPLACE_WORK_SERVICE = "Insert or replace into tblassociateservice(associate_service_id,create_date,ref_associate_id,ref_service_id,update_date)values (?,?,?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblassociateservice(associate_service_id,create_date,ref_associate_id,ref_service_id,update_date)values (?,?,?,?,?)");
            Iterator it = associateServices.iterator();
            while (it.hasNext()) {
                AssociateService associateService = (AssociateService) it.next();
                if (associateService != null) {
                    try {
                        String str;
                        stmt.bindString(1, String.valueOf(associateService.getAssociateServiceId()) == null ? "" : String.valueOf(associateService.getAssociateServiceId()));
                        stmt.bindString(3, String.valueOf(associateService.getRefAssociateId()) == null ? "" : String.valueOf(associateService.getRefAssociateId()));
                        stmt.bindString(4, String.valueOf(associateService.getRefServiceId()) == null ? "" : String.valueOf(associateService.getRefServiceId()));
                        stmt.bindString(2, String.valueOf(associateService.getCreateDate()) == null ? "" : String.valueOf(associateService.getCreateDate()));
                        if (String.valueOf(associateService.getUpdateDate()) == null) {
                            str = "";
                        } else {
                            str = String.valueOf(associateService.getUpdateDate());
                        }
                        stmt.bindString(5, str);
                        stmt.execute();
                        stmt.clearBindings();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : insertOrReplaceWorkService()", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void insertOrUpdateServiceList(ArrayList<WorkServiceMap> workServiceMaps, String Worksheetid, ArrayList<ArrayList<AttachmentMap>> arrayLists) {
        String METHOD = "insertorReplaceWorksheetService";
        String STATEMENT_INSERT_REPLACE_WORK_SERVICE = "Insert or replace into tblworkservicemap(ref_service_id,service_time,ref_work_id)values (?,?,?)";
        try {
            this.db.beginTransaction();
            SQLiteStatement stmt = this.db.compileStatement("Insert or replace into tblworkservicemap(ref_service_id,service_time,ref_work_id)values (?,?,?)");
            for (int i = 0; i < workServiceMaps.size(); i++) {
                try {
                    if (workServiceMaps.get(i) != null) {
                        stmt.bindString(1, String.valueOf(((WorkServiceMap) workServiceMaps.get(i)).getRefServiceId()) == null ? "" : String.valueOf(((WorkServiceMap) workServiceMaps.get(i)).getRefServiceId()));
                        stmt.bindString(2, String.valueOf(((WorkServiceMap) workServiceMaps.get(i)).getServiceTime()) == null ? "" : String.valueOf(((WorkServiceMap) workServiceMaps.get(i)).getServiceTime()));
                        stmt.bindString(3, String.valueOf(Worksheetid) == null ? "" : String.valueOf(Worksheetid));
                        stmt.execute();
                        stmt.clearBindings();
                    }
                    saveAttachments((ArrayList) arrayLists.get(i), "service", (String) selSingleRecordFromDB("SELECT * FROM tblworkservicemap ORDER BY worksheet_service_id DESC LIMIT 1", null).values().toArray()[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.db.setTransactionSuccessful();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(DatabaseHelper.class + " : inserworkmap() : ", "Exception : " + e2);
        } finally {
            this.db.endTransaction();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
