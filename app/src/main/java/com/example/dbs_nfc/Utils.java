package com.example.dbs_nfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

final class Utils {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public final static String bytesToHex(byte[] bytes) {
        if (bytes == null) return null;

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String tag="0x" + new String(hexChars);

        return "0x" + new String(hexChars);
    }


    public static void showNfcSettingsDialog(final Activity app) {
        new AlertDialog.Builder(app)
                .setTitle("NFC is disabled")
                .setMessage("You must enable NFC to use this app.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        app.startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        app.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static  void storeID(Context context, String tagId, String dbsID,String pswd,String uuid){
        dbHelper dbase=new dbHelper();
        dbase.insert(tagId,dbsID,pswd,uuid);
    }


}
