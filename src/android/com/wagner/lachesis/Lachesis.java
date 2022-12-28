package com.wagner.lachesis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class Lachesis extends CordovaPlugin {
    private static final String TAG = Lachesis.class.getName();
    private ValueBroadcastReceiver valueBroadcastReceiver = null;
    CallbackContext callbackContext;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("start")) {
            startBarcodeBroadcastReceiver();
            return true;
        } else if (action.equals("stop")) {
            stopBarcodeBroadcastReceiver();
            return true;
        }
        return false;
    }


    /**
     * 停止接收广播
     */
    private void stopBarcodeBroadcastReceiver() {
        if (valueBroadcastReceiver != null) {
            this.cordova.getActivity().unregisterReceiver(valueBroadcastReceiver);
            valueBroadcastReceiver = null;
        }
    }

    /**
     * 开始接收广播
     */
    private void startBarcodeBroadcastReceiver() {
        if (valueBroadcastReceiver == null) {
            valueBroadcastReceiver = new ValueBroadcastReceiver();
        }
        Intent intent = new Intent("lachesis_barcode_value_notice_broadcast");
        intent.putExtra("lachesis_barcode_value_notice_broadcast_data_string","12345");
        this.cordova.getActivity().registerReceiver(valueBroadcastReceiver, new IntentFilter(
                "lachesis_barcode_value_notice_broadcast"));
        //this.cordova.getActivity().sendBroadcast(intent);
    }

    /**
     * 广播接收
     */
    private class ValueBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent
                    .getStringExtra("lachesis_barcode_value_notice_broadcast_data_string");

            JSONObject obj = new JSONObject();
            try {
                obj.put("text",value);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            callbackContext.success(obj);
            //Toast.makeText(context, "Scanned:"+ value, Toast.LENGTH_LONG).show();
            stopBarcodeBroadcastReceiver();
        }
    }
}
