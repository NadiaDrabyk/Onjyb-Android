package com.onjyb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.onjyb.layout.GroupChatActivity;

public class MyBroadcastMessageReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        GroupChatActivity.getLatestMessage(context);
    }
}
