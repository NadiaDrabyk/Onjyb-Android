package com.onjyb.layout;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.Constants;
import com.onjyb.R;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.GroupChat;
import com.onjyb.receiver.MyBroadcastMessageReceiver;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class GroupChatActivity extends BaseDrawerActivity {
    public static String TYPE_OF_SENDER_FROM = "from";
    public static String TYPE_OF_SENDER_ME = "me";
    static GroupChat chat = new GroupChat();
    static MyBaseAdapter chatAdapter;
    static ArrayList<GroupChat> chatList = new ArrayList();
    static String companyId;
    private static ActionCallback getMeassageApiCallBack1 = new C06428();
    static UserHelper helper;
    public static boolean isInFront;
    static ArrayList<GroupChat> messageList = new ArrayList();
    private static String user_id = "";
    private final String Tag = "GroupchatActivity";
    private Context context = this;
    DatabaseHelper dbHelper;
    ETechEditText edtMeassage;
    private ActionCallback getMeassageApiCallBack = new C06407();
    ImageView imgSendMsg;
    OnClickListener imgSendMsgClickListener = new C06375();
    ListView lstMessages;
    String msgTime = "";
    private final MyBroadcastMessageReceiver mybroadcast = new MyBroadcastMessageReceiver();
    int pageNo = 1;
    ProgressHelper progressHelper;
    RelativeLayout rlSendMeassage;
    private ActionCallback sendMeassageApiCallBack = new C06386();

    class C06331 implements OnClickListener {
        C06331() {
        }

        public void onClick(View v) {
            GroupChatActivity.this.finish();
        }
    }

    class C06342 implements OnScrollListener {
        int counter = 1;
        int currentFirstVisibleItem;
        int currentScrollState;
        int currentTotalItemCount;
        int currentVisibleItemCount;

        C06342() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
            this.currentScrollState = arg1;
            if (GroupChatActivity.this.lstMessages.getFirstVisiblePosition() == 0 && arg1 == 0) {
                Log.d("GroupchatActivity", "State Change" + arg1);
                if (Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_MESSAGES, "false"))).booleanValue()) {
                    GroupChatActivity.helper = new UserHelper(GroupChatActivity.this.context);
                    GroupChatActivity.this.progressHelper.showDialog("Loading..");
                    GroupChatActivity.helper.apigetMessages(GroupChatActivity.companyId, GroupChatActivity.this.pageNo, GroupChatActivity.this.getMeassageApiCallBack);
                }
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    class C06353 implements OnClickListener {
        C06353() {
        }

        public void onClick(View v) {
            GroupChatActivity.this.finish();
        }
    }

    class C06364 implements OnScrollListener {
        int counter = 1;
        int currentFirstVisibleItem;
        int currentScrollState;
        int currentTotalItemCount;
        int currentVisibleItemCount;

        C06364() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
            this.currentScrollState = arg1;
            if (GroupChatActivity.this.lstMessages.getFirstVisiblePosition() == 0 && arg1 == 0) {
                Log.d("GroupchatActivity", "State Change" + arg1);
                if (Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_MESSAGES, "false"))).booleanValue()) {
                    GroupChatActivity.helper = new UserHelper(GroupChatActivity.this.context);
                    GroupChatActivity.this.progressHelper.showDialog("Loading..");
                    GroupChatActivity.helper.apigetMessages(GroupChatActivity.companyId, GroupChatActivity.this.pageNo, GroupChatActivity.this.getMeassageApiCallBack);
                }
            }
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    class C06375 implements OnClickListener {
        C06375() {
        }

        public void onClick(View v) {
            String msg = GroupChatActivity.this.edtMeassage.getText().toString();
            if (msg.trim().length() > 0) {
                GroupChatActivity.helper = new UserHelper(GroupChatActivity.this.context);
                GroupChatActivity.this.progressHelper.showDialog("Loading..");
                GroupChatActivity.helper.apiSendGroupMessage(GroupChatActivity.user_id, GroupChatActivity.companyId, msg, GroupChatActivity.this.sendMeassageApiCallBack);
                return;
            }
            AlertDialogHelper.getNotificatonAlert(GroupChatActivity.this.context, GroupChatActivity.this.getString(R.string.app_name), GroupChatActivity.this.getResources().getString(R.string.pls_enter_msg));
        }
    }

    class C06386 implements ActionCallback {
        C06386() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (GroupChatActivity.this.progressHelper != null) {
                GroupChatActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    String id = ((JSONObject) res).getJSONObject(Constants.RESPONSE_KEY_OBJ).getString("id");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str1 = "";
                    GroupChat chat1 = new GroupChat();
                    String url = Preference.getSharedPref(Constants.PREF_USER_PROFILE_PIC, "");
                    if (!(id == null || id.equalsIgnoreCase(""))) {
                        chat1.setId(id);
                    }
                    chat1.setProfilePic(url);
                    chat1.setMessage(GroupChatActivity.this.edtMeassage.getText().toString());
                    try {
                        str1 = output.format(Calendar.getInstance().getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String fname = Preference.getSharedPref(Constants.PREF_USER_FNAME, "");
                    String lname = Preference.getSharedPref(Constants.PREF_USER_LNAME, "");
                    chat1.setCreateDate(str1);
                    chat1.setFirstName(fname);
                    chat1.setLastName(lname);
                    chat1.setRef_user_id(GroupChatActivity.user_id);
                    chat1.setType(GroupChatActivity.TYPE_OF_SENDER_ME);
                    GroupChatActivity.chatList.add(chat1);
                    GroupChatActivity.chat = chat1;
                    GroupChatActivity.chatAdapter.notifyDataSetChanged();
                    GroupChatActivity.this.edtMeassage.setText("");
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            Toast.makeText(GroupChatActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    }

    class C06407 implements ActionCallback {

        class C06391 extends TypeReference<ArrayList<GroupChat>> {
            C06391() {
            }
        }

        C06407() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (GroupChatActivity.this.progressHelper != null) {
                GroupChatActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    GroupChatActivity.this.pageNo++;
                    JSONObject jsonObject = (JSONObject) res;
                    Preference.setSharedPref(Constants.PREF_GET_LASTDATE_UNREAD_COUNT, jsonObject.getString("last_date"));
                    JSONArray jArrWorsheets = jsonObject.getJSONArray("message_details");
                    ObjectMapper mapper = new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    ArrayList<GroupChat> list = new ArrayList();
                    GroupChatActivity.chatList.addAll(0, (ArrayList) mapper.readValue(jArrWorsheets.toString(), new C06391()));
                    GroupChatActivity.chat = (GroupChat) GroupChatActivity.chatList.get(GroupChatActivity.chatList.size() - 1);
                    GroupChatActivity.chatAdapter.notifyDataSetChanged();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(GroupChatActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    }

    static class C06428 implements ActionCallback {

        class C06411 extends TypeReference<ArrayList<GroupChat>> {
            C06411() {
            }
        }

        C06428() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                try {
                    JSONObject jsonObject = (JSONObject) res;
                    Preference.setSharedPref(Constants.PREF_GET_LASTDATE_UNREAD_COUNT, jsonObject.getString("last_date"));
                    JSONArray jArrWorsheets = jsonObject.getJSONArray("message_details");
                    ObjectMapper mapper = new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    ArrayList<GroupChat> list1 = new ArrayList();
                    list1 = (ArrayList) mapper.readValue(jArrWorsheets.toString(), new C06411());
                    GroupChatActivity.chatList.remove(GroupChatActivity.chat);
                    GroupChatActivity.chatList.addAll(list1);
                    GroupChatActivity.chatAdapter.notifyDataSetChanged();
                    Log.d("OnRefreshViewCallBack", "onActionBack----" + GroupChatActivity.messageList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class MyBaseAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<GroupChat> myList;

        class C06463 implements OnClickListener {
            C06463() {
            }

            public void onClick(View v) {
            }
        }

        public MyBaseAdapter(Context context, ArrayList<GroupChat> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        public int getCount() {
            return this.myList.size();
        }

        public GroupChat getItem(int position) {
            return (GroupChat) this.myList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;
            String dateTime = "";
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.group_chat_list, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
                mViewHolder.profile_img = (ImageView) convertView.findViewById(R.id.from_img_profile);
                mViewHolder.profile_imgRight = (ImageView) convertView.findViewById(R.id.from_img_profileRight);
                mViewHolder.rlTimeDatelayout = (RelativeLayout) convertView.findViewById(R.id.rlTimeDateLayout);
                mViewHolder.rlTimeDatelayoutRight = (RelativeLayout) convertView.findViewById(R.id.rlTimeDateLayoutRight);
                mViewHolder.linMsgLeft = (LinearLayout) convertView.findViewById(R.id.lin_receive);
                mViewHolder.linMsgRight = (LinearLayout) convertView.findViewById(R.id.lin_receiveRight);
                mViewHolder.rlReceviceMessage = (RelativeLayout) convertView.findViewById(R.id.show_recivemsg);
                mViewHolder.rlReceviceMessageRight = (RelativeLayout) convertView.findViewById(R.id.show_recivemsgRight);
                mViewHolder.rlImages = (RelativeLayout) convertView.findViewById(R.id.rel_img);
                mViewHolder.rlImagesRight = (RelativeLayout) convertView.findViewById(R.id.rel_imgRight);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            GroupChat currentChat = getItem(position);
            String url = Constants.BASE_IMAGE_URL + currentChat.getProfilePic();
            GroupChat prviousMsg = null;
            if (position > 0) {
                prviousMsg = getItem(position - 1);
            }
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat output1 = new SimpleDateFormat("dd MMM yy");
            SimpleDateFormat df = new SimpleDateFormat("HH:m");
            try {
                Date date = output.parse(currentChat.getCreateDate());
                GroupChatActivity.this.msgTime = df.format(date);
                dateTime = output1.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long noOfDays;
            if (GroupChatActivity.user_id.equalsIgnoreCase(getItem(position).getRef_user_id())) {
                mViewHolder.rlReceviceMessageRight.setVisibility(View.VISIBLE);
                mViewHolder.rlTimeDatelayoutRight.setVisibility(View.VISIBLE);
                mViewHolder.rlImagesRight.setVisibility(View.VISIBLE);
                mViewHolder.rlReceviceMessage.setVisibility(View.GONE);
                mViewHolder.rlTimeDatelayout.setVisibility(View.GONE);
                mViewHolder.rlImages.setVisibility(View.GONE);
                mViewHolder.currentTimeRight.setText(GroupChatActivity.this.msgTime);
                mViewHolder.messageRight.setText(currentChat.getMessage());
                noOfDays = GroupChatActivity.this.getDateDifference(currentChat.getCreateDate());
                if (position == 0 || !(prviousMsg == null || currentChat.getRef_user_id().equalsIgnoreCase(prviousMsg.getRef_user_id()))) {
                    showRightViewElements(mViewHolder);
                    if (noOfDays == 0) {
                        mViewHolder.currentDateRight.setText(GroupChatActivity.this.getResources().getString(R.string.today));
                    } else if (noOfDays == 1) {
                        mViewHolder.currentDateRight.setText(GroupChatActivity.this.getResources().getString(R.string.yesterday));
                    } else {
                        mViewHolder.currentDateRight.setText(dateTime);
                    }
                    mViewHolder.senderNameRight.setVisibility(View.INVISIBLE);

                    Glide.with(this.context).load(url).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(mViewHolder.profile_imgRight) {
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyBaseAdapter.this.context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mViewHolder.profile_imgRight.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else {
                    mViewHolder.currentDateRight.setText("");
                    hideRightViewElements(mViewHolder);
                }
            } else {
                mViewHolder.rlReceviceMessageRight.setVisibility(View.GONE);
                mViewHolder.rlTimeDatelayoutRight.setVisibility(View.GONE);
                mViewHolder.rlImagesRight.setVisibility(View.GONE);
                mViewHolder.rlReceviceMessage.setVisibility(View.VISIBLE);
                mViewHolder.rlTimeDatelayout.setVisibility(View.VISIBLE);
                mViewHolder.rlImages.setVisibility(View.VISIBLE);
                mViewHolder.message.setText(currentChat.getMessage());
                mViewHolder.currentTime.setText(GroupChatActivity.this.msgTime);
                noOfDays = GroupChatActivity.this.getDateDifference(currentChat.getCreateDate());
                if (position == 0 || !(prviousMsg == null || currentChat.getRef_user_id().equalsIgnoreCase(prviousMsg.getRef_user_id()))) {
                    showLeftViewElements(mViewHolder);
                    if (noOfDays == 0) {
                        mViewHolder.currentDate.setText(GroupChatActivity.this.getResources().getString(R.string.today));
                    } else if (noOfDays == 1) {
                        mViewHolder.currentDate.setText(GroupChatActivity.this.getResources().getString(R.string.yesterday));
                    } else {
                        mViewHolder.currentDate.setText(dateTime);
                    }
                    mViewHolder.senderName.setText(AppUtils.UpperCaseWords(currentChat.getFirstName() + " " + currentChat.getLastName()));
                    Glide.with(this.context).load(url).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(mViewHolder.profile_img) {
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyBaseAdapter.this.context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mViewHolder.profile_img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else {
                    mViewHolder.currentDate.setText("");
                    hideLeftViewElements(mViewHolder);
                }
            }
            String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            convertView.setOnClickListener(new C06463());
            return convertView;
        }

        private void hideLeftViewElements(MyViewHolder viewHolder) {
            viewHolder.rlImages.setVisibility(View.INVISIBLE);
            viewHolder.senderName.setVisibility(View.GONE);
            viewHolder.currentDate.setVisibility(View.GONE);
            viewHolder.profile_img.setVisibility(View.INVISIBLE);
        }

        private void showLeftViewElements(MyViewHolder viewHolder) {
            viewHolder.rlImages.setVisibility(View.VISIBLE);
            viewHolder.senderName.setVisibility(View.VISIBLE);
            viewHolder.currentDate.setVisibility(View.VISIBLE);
            viewHolder.profile_img.setVisibility(View.VISIBLE);
        }

        private void hideRightViewElements(MyViewHolder viewHolder) {
            viewHolder.rlImagesRight.setVisibility(View.INVISIBLE);
            viewHolder.currentDateRight.setVisibility(View.GONE);
            viewHolder.profile_imgRight.setVisibility(View.INVISIBLE);
        }

        private void showRightViewElements(MyViewHolder viewHolder) {
            viewHolder.rlImagesRight.setVisibility(View.VISIBLE);
            viewHolder.currentDateRight.setVisibility(View.VISIBLE);
            viewHolder.profile_imgRight.setVisibility(View.VISIBLE);
        }
    }

    private class MyViewHolder {
        TextView currentDate;
        TextView currentDateRight;
        TextView currentTime;
        TextView currentTimeRight;
        LinearLayout linMsgLeft;
        LinearLayout linMsgRight;
        TextView message;
        TextView messageRight;
        ImageView profile_img;
        ImageView profile_imgRight;
        RelativeLayout rlImages;
        RelativeLayout rlImagesRight;
        RelativeLayout rlReceviceMessage;
        RelativeLayout rlReceviceMessageRight;
        RelativeLayout rlTimeDatelayout;
        RelativeLayout rlTimeDatelayoutRight;
        TextView senderName;
        TextView senderNameRight;

        public MyViewHolder(View item) {
            this.senderName = (TextView) item.findViewById(R.id.txt_sendernm);
            this.message = (TextView) item.findViewById(R.id.showingreceive_msg);
            this.currentDate = (TextView) item.findViewById(R.id.showingrecive_msg_date);
            this.currentTime = (TextView) item.findViewById(R.id.showingrecive_msg_time);
            this.senderNameRight = (TextView) item.findViewById(R.id.txt_sendernmRight);
            this.messageRight = (TextView) item.findViewById(R.id.showingreceive_msgRight);
            this.currentDateRight = (TextView) item.findViewById(R.id.showingrecive_msg_dateRight);
            this.currentTimeRight = (TextView) item.findViewById(R.id.showingrecive_msg_timeRight);
            this.rlTimeDatelayout = (RelativeLayout) GroupChatActivity.this.findViewById(R.id.rlTimeDateLayout);
            this.rlTimeDatelayoutRight = (RelativeLayout) GroupChatActivity.this.findViewById(R.id.rlTimeDateLayoutRight);
            this.rlReceviceMessage = (RelativeLayout) GroupChatActivity.this.findViewById(R.id.show_recivemsg);
            this.rlReceviceMessageRight = (RelativeLayout) GroupChatActivity.this.findViewById(R.id.show_recivemsgRight);
        }
    }

    protected void onResume() {
        super.onResume();
        isInFront = true;
        Log.d("GroupchatActivity", "onResume()-----" + isInFront);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.SendBroadcast");
        registerReceiver(this.mybroadcast, filter);
    }

    protected void onPause() {
        super.onPause();
        isInFront = false;
        unregisterReceiver(this.mybroadcast);
        Log.d("GroupchatActivity", "onPause()-----" + isInFront);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        updateviewMethod();
    }

    private void updateviewMethod() {
        setContentView(R.layout.activity_group_chat);
        this.header.setTitle(getString(R.string.menu_group_chat));
        this.header.hideRightBtn();
        this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
        this.header.setLeftBtnClickListener(new C06331());
        getViews();
        this.pageNo = 1;
        Log.d("GroupchatActivity", "onCreate()-----" + isInFront);
        user_id = Preference.getSharedPref(Constants.PREF_USER_ID, Constants.USER_ROLE_EMPLOYEE);
        companyId = Preference.getSharedPref(Constants.PREF_COMPANY_ID, Constants.USER_ROLE_EMPLOYEE);
        chatList = new ArrayList();
        helper = new UserHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        helper.apigetMessages(companyId, this.pageNo, this.getMeassageApiCallBack);
        this.imgSendMsg.setOnClickListener(this.imgSendMsgClickListener);
        try {
            AppUtils.hideKeyBoard(this);
            chatAdapter = new MyBaseAdapter(this.context, chatList);
            this.lstMessages.setAdapter(chatAdapter);
            this.lstMessages.smoothScrollToPosition(0);
            this.lstMessages.setOnScrollListener(new C06342());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        this.header.setTitle(getString(R.string.menu_group_chat));
        this.header.hideRightBtn();
        this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
        this.header.setLeftBtnClickListener(new C06353());
        getViews();
        Log.d("GroupchatActivity", "onCreate()-----" + isInFront);
        user_id = Preference.getSharedPref(Constants.PREF_USER_ID, Constants.USER_ROLE_EMPLOYEE);
        companyId = Preference.getSharedPref(Constants.PREF_COMPANY_ID, Constants.USER_ROLE_EMPLOYEE);
        chatList = new ArrayList();
        helper = new UserHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        helper.apigetMessages(companyId, this.pageNo, this.getMeassageApiCallBack);
        this.imgSendMsg.setOnClickListener(this.imgSendMsgClickListener);
        try {
            AppUtils.hideKeyBoard(this);
            chatAdapter = new MyBaseAdapter(this.context, chatList);
            this.lstMessages.setAdapter(chatAdapter);
            this.lstMessages.smoothScrollToPosition(0);
            this.lstMessages.setOnScrollListener(new C06364());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.imgSendMsg = (ImageView) findViewById(R.id.imgMsgSend);
        this.edtMeassage = (ETechEditText) findViewById(R.id.edtMeassage);
        this.lstMessages = (ListView) findViewById(R.id.lstMessages);
    }

    public void addNewItems(List<GroupChat> items) {
        final int positionToSave = this.lstMessages.getFirstVisiblePosition();
        chatList.addAll(0, items);
        this.lstMessages.post(new Runnable() {
            public void run() {
                GroupChatActivity.this.lstMessages.setSelection(positionToSave);
            }
        });
        this.lstMessages.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                if (GroupChatActivity.this.lstMessages.getFirstVisiblePosition() != positionToSave) {
                    return false;
                }
                GroupChatActivity.this.lstMessages.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public static void getLatestMessage(Context context) {
        if (isInFront) {
            helper = new UserHelper(context);
            helper.apigetLatestMessages(companyId, -1, chat.getId(), getMeassageApiCallBack1);
        }
    }

    private long getDateDifference(String mDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return (df.parse(df.format(c.getTime())).getTime() - df.parse(mDate).getTime()) / DateUtils.MILLIS_PER_DAY;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
