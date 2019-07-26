package com.example.dbs_nfc;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import android.widget.EditText;

import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    dbHelper dbase;
    static private ArrayList<String> tags = new ArrayList<String>();
    static private int currentTagIndex = -1;
    private ProgressDialog progDailog;
    Activity activity;
    private NfcAdapter adapter = null;
    private TextView mTextView;
    private PendingIntent pendingIntent = null;
    private String tagId;
    private static final String TAG = dbHelper.class.getName();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login);
        setContentView(R.layout.activity_main);
        mTextView=findViewById(R.id.message);
        adapter = NfcAdapter.getDefaultAdapter(this);
        dbase =new dbHelper();
        Button b=findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webParser w=new webParser();
                w.getWebsite();

            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Discovered tag with intent " + intent);


        Button b=findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                webParser w=new webParser();
                w.getWebsite();

            }
        });




        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
         tagId = Utils.bytesToHex(tag.getId());
        tagId=tagId;
        Log.e(TAG,tagId);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        tags.add(tagId);
        currentTagIndex = tags.size() - 1;

        displayTag();
        storeData();

    }


    public void storeData(){

        Button btn=findViewById(R.id.button2);
        btn.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                       st();
                    }
                }
        );




    }


    public void st(){
        EditText id =(EditText)findViewById(R.id.id);
        EditText pswd =(EditText)findViewById(R.id.pswd);
        Utils.storeID(this,tagId,id.getText().toString(),pswd.getText().toString());
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

//        displayTag();

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
       // openWeb();
    }
    public void openWeb(View view) {

        final WebView lib_web=findViewById(R.id.webView);
        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        String url = "https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO;jsessionid=193exgnish94l1vpx4ob4oh57c?execution=e1s1";
        lib_web.loadUrl(url);

        lib_web.getSettings().setDomStorageEnabled(true);


        lib_web.getSettings().setJavaScriptEnabled(true);

        lib_web.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){

                progDailog.dismiss();
             //   view.evaluateJavascript("javascript:document.getElementById('username').value='dbs';document.getElementById('password').value='23021995';",null);
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
