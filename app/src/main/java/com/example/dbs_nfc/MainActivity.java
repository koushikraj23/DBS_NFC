package com.example.dbs_nfc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    dbHelper dbase;
    static private ArrayList<String> tags = new ArrayList<String>();
    static private int currentTagIndex = -1;
    private ProgressDialog progDailog;
    Activity activity;
    private NfcAdapter adapter = null;
    private TextView mTextView;
    private PendingIntent pendingIntent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.message);
        adapter = NfcAdapter.getDefaultAdapter(this);
        dbase =new dbHelper(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Discovered tag with intent " + intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String tagId = Utils.bytesToHex(tag.getId());
        tagId=tagId+Utils.now();

        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (tags.size() == 1) {
            Toast.makeText(this, "Swipe right to see previous tags", Toast.LENGTH_LONG).show();
        }
        tags.add(tagId);
        currentTagIndex = tags.size() - 1;
        displayTag();
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (!adapter.isEnabled()) {
//            Utils.showNfcSettingsDialog(this);
//            return;
//        }
//
//        if (pendingIntent == null) {
//            pendingIntent = PendingIntent.getActivity(this, 0,
//                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//            mTextView.setText("Scan a tag");
//        }
//
//        displayTag();
//
//        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
//
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter.disableForegroundDispatch(this);
    }


    private void displayTag() {
        if (tags.size() == 0) return;
        final String tagWrapper = tags.get(currentTagIndex);
        mTextView.setText("Tag " + tagWrapper);
        openWeb();
    }
    public void openWeb() {

        final WebView lib_web=findViewById(R.id.webView);
        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        String url = "https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl";
        lib_web.loadUrl(url);


        lib_web.getSettings().setJavaScriptEnabled(true);

        lib_web.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){

                progDailog.dismiss();

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }


            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }
        });




    }
    class SSLTolerentWebViewClient extends WebViewClient {

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }}
}
