package com.example.dbs_nfc;

import android.net.http.SslError;
import android.util.JsonReader;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

public class webParser {
    private static final String TAG = webParser.class.getName();
    public void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
//                    Document doc = Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl") .timeout(60000).validateTLSCertificates(false).get();
//                    String title = doc.title();
//                    Elements links = doc.select("a[href]");
//
//                    builder.append(title).append("\n");
//
//                    for (Element link : links) {
//                        builder.append("\n").append("Link : ").append(link.attr("href"))
//                                .append("\n").append("Text : ").append(link.text());
//                    }
//                    Log.e(TAG, "run: "+title );




                    Connection.Response loginPageResponse =
                            Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl")
                                    .timeout(10 * 1000)
                                    .userAgent("Mozilla/5.0")
                                    .followRedirects(true)
                                    .validateTLSCertificates(false)
                                    .execute();
                    System.out.println("Fetched login page");
                    Map<String, String> mapLoginPageCookies = loginPageResponse.cookies();
                    System.out.println(mapLoginPageCookies);
//                    Connection.Response loginForm = Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl")
//                            .referrer("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO;jsessionid=192vt1n6ng9dwsyf2ck6hlh1v?execution=e1s1")
//                            .method(Connection.Method.POST)
//                            .validateTLSCertificates(false).execute();

//                    Connection.Response loginForm1  = Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl")
//                            .cookies(mapLoginPageCookies)
//                            .data("j_username", "10506917")
//                            .data("j_password", "Kou@23021995")
//                            .validateTLSCertificates(false)
//                            .cookies(mapLoginPageCookies)
//                            //many websites redirects the user after login, so follow them
//                            .followRedirects(true)
//                            .execute();

                    Connection.Response loginForm1 = Jsoup.connect("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s1")
                            //referrer will be the login page's URL
                            .referrer("https://books.dbs.ie/Shibboleth.sso/Login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl")
                            //user agent
                            .userAgent("Mozilla")

                            //connect and read time out
                            .timeout(10 * 1000)
                            //post parameters
                            .data("j_username", "10506917")
                            .data("j_password", "")
                            .data("_eventId_proceed", "")
//                            .data("j_password", "Kou@23021995")
                            //cookies received from login page
                            .cookies(mapLoginPageCookies)
                            //many websites redirects the user after login, so follow them
                            .followRedirects(true)
                            .validateTLSCertificates(false)
                            .execute();

                    System.out.println("HTTP Status Code1: " + loginForm1.statusCode());
                    Map<String, String> mapLoginPageCookies2=loginForm1.cookies();
                    Map<String, String> mapLoginPageCookies3=mapLoginPageCookies;
                    mapLoginPageCookies3.put(mapLoginPageCookies2.keySet().toArray()[0].toString(),mapLoginPageCookies2.get(mapLoginPageCookies2.keySet().toArray()[0]));


                    System.out.println(mapLoginPageCookies3);
                    //System.out.println(document);
                    Connection.Response loginForm2 = Jsoup.connect("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s2")
                            //referrer will be the login page's URL
                            .referrer("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s2")
                            //user agent
                            .userAgent("Mozilla")
                            //connect and read time out
                            .timeout(10 * 1000)
                            //post parameters
                            .data("_shib_idp_consentIds","givenName" )
                            .data("_shib_idp_consentIds","sn")
                            .data("_shib_idp_consentIds","cn")
                            .data("_shib_idp_consentOptions","_shib_idp_rememberConsent")
                            .data("_eventId_proceed","Accept")
//
                            .cookies(mapLoginPageCookies3)
                            //many websites redirects the user after login, so follow them
                            .followRedirects(true)
                            .validateTLSCertificates(false)
                            .execute();
                    mapLoginPageCookies3=loginForm2.cookies();
                    Document document = loginForm2.parse();
                    System.out.println(mapLoginPageCookies3);




                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }
                String x=builder.toString();
                Log.e(TAG, "run: "+x );

            }
        }).start();
    }
    class SSLTolerentWebViewClient extends WebViewClient {

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }}
}
