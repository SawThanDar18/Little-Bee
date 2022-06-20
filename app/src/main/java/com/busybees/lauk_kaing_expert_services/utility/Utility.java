package com.busybees.lauk_kaing_expert_services.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Utility {

    public static boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean hostReachable = false;

        if (cm != null && cm.getActiveNetworkInfo() != null)
            hostReachable = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        return hostReachable;
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        if (group != null) {
            TextView tvMessage = (TextView) group.getChildAt(0);
            tvMessage.setText(message);
            tvMessage.setGravity(Gravity.CENTER);
            toast.show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5F);
    }
}
