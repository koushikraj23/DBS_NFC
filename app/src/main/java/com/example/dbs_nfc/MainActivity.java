package com.example.dbs_nfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {






    public interface readCallback{
        void onCallback(UserDetails userTemp);
    }

    public interface bookCallback{
        void onCallback(ArrayList<BookDetails> bookTemp);
    }
    ListView list;
    static dbHelper dbase;
    static private ArrayList<String> tags = new ArrayList<String>();
    static private int currentTagIndex = -1;
    private ProgressDialog progDailog;
    Activity activity;
    private NfcAdapter adapter = null;
    private TextView mTextView;
    private PendingIntent pendingIntent = null;
    private  static String tagId;
    private static final String TAG = dbHelper.class.getName();
    private static UserDetails user=new UserDetails();
    private EditText id;
    private EditText pswd;

    public BookDetails getBookDetails() {
        return bookDetails;
    }

    public void setBookDetails(BookDetails bookDetails) {
        this.bookDetails = bookDetails;
//        setContentView(R.layout.booklist);
    }

    private BookDetails bookDetails;
    public static ArrayList<BookDetails> booklist=new ArrayList<>();
    webParser w=new webParser();
    private String uuid;
public void Change(){
    setContentView(R.layout.booklist);

}


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            uuid = UUID.randomUUID().toString();
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putString("uuid", uuid);
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
        }


//        setContentView(R.layout.login);
        setContentView(R.layout.scantag);
//        setContentView(R.layout.activity_main);
       // setContentView(R.layout.booklist);
//        mTextView=findViewById(R.id.message);
        adapter = NfcAdapter.getDefaultAdapter(this);
        dbase =new dbHelper();
//        Button b=findViewById(R.id.button2);
        list=(ListView) findViewById(R.id.bookList);
//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
////                setContentView(R.layout.booklist);
////                setContentView(R.layout.activity_main);
//           //  booklist= w.getWebsite();
//                readBookDetail();
//                System.out.println("kosui");
////                for (BookDetails bk : booklist) {
////
////                    System.out.println("Main kosuhik:"+bk.getTitle()+"--"+bk.getAuthor()+"---"+bk.getDueDate()+"--"+bk.getrLink());
////                }
//            }
//        });
//        Button ba=findViewById(R.id.back);
//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                setContentView(R.layout.activity_main);
//            }
//        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("onNewIntent", "Discovered tag with intent " + intent);

      //  setContentView(R.layout.login);
      //  mTextView=findViewById(R.id.message);
//        Button b=findViewById(R.id.button2);
//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {

//                webParser w=new webParser();
//                w.getWebsite();
//
//            }
//        });




        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
         tagId = Utils.bytesToHex(tag.getId());
        tagId=tagId;
        Log.e(TAG,tagId);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        tags.add(tagId);
        currentTagIndex = tags.size() - 1;
        SharedPreferences sPrefs=PreferenceManager.getDefaultSharedPreferences(this);
        uuid=sPrefs.getString("uuid",null);
        System.out.println(uuid+"----------------------------------------------------------");
        readTagData();


    }



    public void readTagData  (){

        dbase.readTagDataAsyn (new readCallback() {
            @Override
            public void onCallback(UserDetails userTemp) {
                Log.d(TAG,user.getCardId()+"Interfae--'"+user.getName());
                user=userTemp;
                System.out.println(user.getName());
                if(user.getName()!=null){
//                    System.out.println("Activy view");
//                    setContentView(R.layout.activity_main);
//                    mTextView=findViewById(R.id.message);
//                    displayTag();
                    readBookDetail();

                }else{
                    System.out.println("Login View");
                    setContentView(R.layout.login);
                   // mTextView=findViewById(R.id.message);

                    storeData();
//                    readBookDetail();
                }
            }
        },tagId,uuid);
    }
    public void readBookDetail  (){
        booklist.clear();
        System.out.println("Main kosuhik:");
        w.getWebsite (new bookCallback() {
            @Override
            public void onCallback(ArrayList<BookDetails> bookTemp) {
                Log.d(TAG,user.getCardId()+"Interfae--'"+user.getName());
//
                booklist=bookTemp;
                System.out.println(user.getName());
                for (BookDetails bk : booklist) {

                    System.out.println("Main kosuhik:"+bk.getTitle()+"--"+bk.getAuthor()+"---"+bk.getDueDate()+"--"+bk.getrLink());
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
//                        setContentView(R.layout.booklist);
//                        Button ba=findViewById(R.id.back);
//                        ba.setOnClickListener(new View.OnClickListener() {
//                            public void onClick(View v) {
//
//                                setContentView(R.layout.activity_main);
//                            }
//                        });
                        System.out.println(0);

                        BookAdapter  adapter=new BookAdapter(MainActivity.this,R.layout.book,booklist);
//                        adapter.clear();

                        System.out.println("1");
                        View currentView = MainActivity.this.getWindow().getDecorView();

                        System.out.println(currentView);
                        System.out.println(MainActivity.this);
                        list.setAdapter(adapter);
                        System.out.println("2");

                    }
                });

//                readTagDataAsyn();
            }
        });
    }
        public void readTagDataAsyn (){
            setContentView(R.layout.login);
        }

//    public void readBookDetailAsyn (final bookCallback rCallback,String s){
//       w.getWebsite();
//    }

        public void storeData(){

        Button btn=findViewById(R.id.login);
        btn.setOnClickListener(
                new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        storeInDB();
                     //   openWeb(v);

                    }
                }
        );

    }


    public void storeInDB(){

         id =(EditText)findViewById(R.id.id);
         pswd =(EditText)findViewById(R.id.pswd);
       Utils.storeID(this,tagId,id.getText().toString(),pswd.getText().toString(),uuid);

    }

    @Override
    public void onResume() {
       // mTextView=findViewById(R.id.message);
        super.onResume();

        if (!adapter.isEnabled()) {
            Utils.showNfcSettingsDialog(this);
            return;
        }
//
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

           // mTextView.setText("Scan a tag");
        }

       // displayTag();

        adapter.enableForegroundDispatch(this, pendingIntent, null, null);
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
     //  mTextView.setText("Tag " + tagWrapper);
       // openWeb();
    }
    public void openWeb(View view) {

        final WebView lib_web=findViewById(R.id.webView);
        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        String url = "https://library.dbs.ie/my-library/reserve-and-renew";

        lib_web.loadUrl(url);

        lib_web.getSettings().setDomStorageEnabled(true);


        lib_web.getSettings().setJavaScriptEnabled(true);

        lib_web.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url){
                String js="javascript:document.getElementById('username').value='"+id.getText()+"';document.getElementById('password').value='"+pswd.getText()+"';";
                progDailog.dismiss();
                System.out.println("jabas");
                view.evaluateJavascript(js,null);

//                view.evaluateJavascript("javascript:document.getElementById('username').value="+id+";document.getElementById('password').value='"+pswd+"';",null);
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



    public class BookAdapter extends ArrayAdapter<BookDetails> {
        int resource;
        Context context;
        public BookAdapter(Context context, int resoure,ArrayList<BookDetails> bookDetails) {
            super(context,resoure, bookDetails);
            this.resource=resoure;
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            BookDetails bookDetails = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
//            if (convertView == null) {
//                convertView = LayoutInflater.from(getContext()).inflate(resource,null, false);
//            }
            System.out.println(bookDetails);
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView=inflater.inflate(R.layout.book, null,true);
            // Lookup view for data population

            TextView book = (TextView) convertView.findViewById(R.id.book);
            TextView author = (TextView) convertView.findViewById(R.id.author);
            Button renew=(Button)convertView.findViewById(R.id.renew);
            renew.setText(bookDetails.getDueDate());
            final String s=bookDetails.getrLink();
            renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    LayoutInflater inflater = LayoutInflater.from(context);
//                    View convertView=inflater.inflate(R.layout.book, null,true);
                    System.out.println(s);
//                    Button renew=(Button)convertView.findViewById(R.id.renew);
//                    renew.setText(s);

                    readBookDetail();
                }
            });
            // Populate the data into the template view using the data object
            book.setText(bookDetails.getTitle());
            author.setText(bookDetails.getAuthor());
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
