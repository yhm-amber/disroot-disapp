package org.disroot.disrootapp;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StatusBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, StatusService.class);
        context.startService(myIntent);

    }
}
