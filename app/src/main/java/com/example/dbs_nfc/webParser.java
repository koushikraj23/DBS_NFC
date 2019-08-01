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
                            .referrer("https://books.dbs.ie/Shibboleth.sso/login?target=https://books.dbs.ie/cgi-bin/koha/opac-user.pl")
                            //user agent
                            .userAgent("Mozilla")

                            //connect and read time out
                            .timeout(10 * 1000)
                            //post parameters
                            .data("j_username", "10515229")
                            .data("j_password", "pop+cornsms1")
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
                    Map<String,String>  mapLoginPageCookies4=loginForm2.cookies();
                    System.out.println(mapLoginPageCookies4);
                    mapLoginPageCookies3.put(mapLoginPageCookies4.keySet().toArray()[0].toString(),mapLoginPageCookies4.get(mapLoginPageCookies4.keySet().toArray()[0]));
                    mapLoginPageCookies3.put(mapLoginPageCookies4.keySet().toArray()[1].toString(),mapLoginPageCookies4.get(mapLoginPageCookies4.keySet().toArray()[1]));

                    System.out.println(mapLoginPageCookies3);
                    Document document1 = loginForm2.parse();
                    System.out.println(document1);


                    Element input = document1.select("input[name=SAMLResponse]").first();

                    String auth_token = input.attr("value");

                    Connection.Response loginForm3 = Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/SAML2/POST")
                            //referrer will be the login page's URL
                            .referrer("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s2")
                            //user agent
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                            //connect and read time out
                            .method(Connection.Method.POST)
                            .timeout(10 * 1000)
                            //post parameters
                            .data("RelayState","ss:mem:7d02e79494ffb3d25c5d1e8603ba8e6c3a6b862c3cbcbd6bf7c2040199169bf9" )
                            .data("SAMLResponse",auth_token)
//                            .data("SAMLResponse","PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBEZXN0aW5hdGlvbj0iaHR0cHM6Ly9ib29rcy5kYnMuaWUvU2hpYmJvbGV0aC5zc28vU0FNTDIvUE9TVCIgSUQ9Il83NjgwZDE4MTE2OGIwMTI1NTUyZmYxYTI5MTk4ZGUyNiIgSW5SZXNwb25zZVRvPSJfNTZlMDA3MmFlZjQzZjgxZjkzZjRjZDg1NTg3Mzc5YTkiIElzc3VlSW5zdGFudD0iMjAxOS0wNy0xN1QwMzowMToyOS43NjNaIiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj48c2FtbDI6SXNzdWVyIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwczovL3dlYmF1dGguZGJzLmllL2lkcC9zaGliYm9sZXRoPC9zYW1sMjpJc3N1ZXI+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+CjxkczpTaWduZWRJbmZvPgo8ZHM6Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8ZHM6U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjcnNhLXNoYTUxMiIvPgo8ZHM6UmVmZXJlbmNlIFVSST0iI183NjgwZDE4MTE2OGIwMTI1NTUyZmYxYTI5MTk4ZGUyNiI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGE1MTIiLz4KPGRzOkRpZ2VzdFZhbHVlPklSU0JCL0FMY0YwOUVVR01FMTBwc3RQT2lLTFJSRUgzNkluVmltbDNrbXRLQ2JINkpTOGJzQ0hrZnVyK3JLbXFVRG1ZcU9maU8wSU0KWWVxWkxaVmhwUT09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpPcHhqZ2hSZ3JlQndQb3EySXRpV1hBMjFza3hjSVBSQzNqUFlsSVc0aXA3ak5WNlYwdzNuRlpyRWhGZVlGNll2RXd2Q2Y1S2J6bG9xCjJGOFZmdFpPRGpvWlNrWk9tRFhaRjBkM2pianhhM0JQd3NQL25CMFpxbUpLNmxTSzVIdGZJeUI4QjhlV1Z5eEQ3V2ZPSCtGZmlPRmsKaVEvU1BhTUNyZ1JYWHRobTlzMnVGcTNXSVRMRzcrci9SV011RkJjRzJOS3RTYjdEWlNTcTBxMXNNd0lRTUJYMmFBdFZaUDJLYUZzSQpqR0o2Z0d2dWZMRU13MXZWVWQyWmhxV1ZSZ24yU2hnb1FLWkZVN3ZGb3NQMDZUNXFsQ1VuYUN2T2dhL3lvV09vblhiVnNMcEQ5ZFdiClRHSlUzeXgwM2RCbS9Na2NOWVlvSkZ5cVUvNGdxaW1tcnpSemxBPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPGRzOktleUluZm8+PGRzOlg1MDlEYXRhPjxkczpYNTA5Q2VydGlmaWNhdGU+TUlJREpEQ0NBZ3lnQXdJQkFnSVZBSVpCaS9OVTljK3o1SHoveHJLbDM3NksxWkdDTUEwR0NTcUdTSWIzRFFFQkN3VUFNQmt4RnpBVgpCZ05WQkFNTURuZGxZbUYxZEdndVpHSnpMbWxsTUI0WERURTJNRFl3TkRFM05UZzFORm9YRFRNMk1EWXdOREUzTlRnMU5Gb3dHVEVYCk1CVUdBMVVFQXd3T2QyVmlZWFYwYUM1a1luTXVhV1V3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ1IKcXlHemFjRWVKQVhMVkt2Y2NyRnhPNjBYTURHaG91WDZPSlIwd0g3VGJMTUZjeHVFb3AvYnFqZGFUQzRSdE5XWmVmODRxbEVOQ3BPSAp0WU5hckRKak9wQjNWUEJMSi9SZGVEMlZTalEweDdVTFd6eXBPRkVlYU04QUZCZTl3eXJQUHZRakhuV3VKWlJQWE9iaHZ3YlUrMjJKClNSVnA5aTE3b0VlUGc2dkZ6MHBFdVhsd1BaVmJyZDNBMDlLUmplNkpjZk5ER1U5QjJ5SVpTTjFOdjNFTnZoWm1HT0ZwdThpaHMzSFUKU0lDVjlxU0t4d2JrYmJFNDAwRXpCb0RFTHhoalRORHlkUG96NzhPcHBxWEFsN0xOdHE4bzlENHRUYmQ0QSt0MElHL2IybE5zVVZxTwp2YSs0dEU1V0h0STBLSXV2ODVqcHE4cDcydHJYYnVsM2RKWkZBZ01CQUFHall6QmhNQjBHQTFVZERnUVdCQlNRckFTWHJlRU55cjg3CnV4b2hlV2xxUU9RcHBUQkFCZ05WSFJFRU9UQTNnZzUzWldKaGRYUm9MbVJpY3k1cFpZWWxhSFIwY0hNNkx5OTNaV0poZFhSb0xtUmkKY3k1cFpTOXBaSEF2YzJocFltSnZiR1YwYURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQWMxZHRTalM5NHdNeVpRYzJEQnRkd2FLTAp0UFRrbzN2Q0xVbFowOXRTZHNnVU9nT0c3TkgvMDBOLytXWkV2YVNqTTk2a053VEhxdEluR0xLTkJGdXNiaDl1b1ZGTExaRjA2bGJmCk8vMFlqMCtUV2xMOHJ5L1EvYkRhZUFhYTZidmV6N2taZ1E3Q0NDNHBDRHBlS3A4N25TemgvcGV5MHJaS29lTWg2Q0RzLzBlN3RwSkUKdmtveVpyOTNXNEl6YURDNWFSTVp1aDVZWHV2czc2UDRYY0t6MlBnQUZ4dmM2bWNWaFJSZXJ1THE4c2pqcEFoYkJndmFpWnZ1WlJGeQpjYkZWUWhNdlBMODFGc0dWdU1pSlRlQ2hiV2RmSnRrSVhaTGFldFpJVlFvcTg2TGFxMEM5U2ErQ0hqOVZQcnltRHBwVkhQaklXSWNVClJjS1ArV3Jxa3Z6bkJ3PT08L2RzOlg1MDlDZXJ0aWZpY2F0ZT48L2RzOlg1MDlEYXRhPjwvZHM6S2V5SW5mbz48L2RzOlNpZ25hdHVyZT48c2FtbDJwOlN0YXR1cz48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6RW5jcnlwdGVkQXNzZXJ0aW9uIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48eGVuYzpFbmNyeXB0ZWREYXRhIElkPSJfNjI4NTUwMDA5NjdiZjE3ZjM3NTBjNTc0YmEwMzNiYjciIFR5cGU9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI0VsZW1lbnQiIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6RW5jcnlwdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjYWVzMTI4LWdjbSIgeG1sbnM6eGVuYz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjIi8+PGRzOktleUluZm8geG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjx4ZW5jOkVuY3J5cHRlZEtleSBJZD0iX2VkYTg0NDJiMjg5YmEyNTE5YmZiNjJjYjMyMzEwMWRjIiBSZWNpcGllbnQ9Imh0dHBzOi8vYm9va3MuZGJzLmllIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkVuY3J5cHRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA5L3htbGVuYzExI3JzYS1vYWVwIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiLz48eGVuYzExOk1HRiBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjbWdmMXNoYTEiIHhtbG5zOnhlbmMxMT0iaHR0cDovL3d3dy53My5vcmcvMjAwOS94bWxlbmMxMSMiLz48L3hlbmM6RW5jcnlwdGlvbk1ldGhvZD48ZHM6S2V5SW5mbz48ZHM6WDUwOURhdGE+PGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlDM3pDQ0FjZWdBd0lCQWdJSkFNZUhmS3dkU2R5U01BMEdDU3FHU0liM0RRRUJCUVVBTUJNeEVUQVBCZ05WQkFNVENHUmljeTFzCmFYWmxNQjRYRFRFM01ERXlOekE1TWpZek9Gb1hEVEkzTURFeU5UQTVNall6T0Zvd0V6RVJNQThHQTFVRUF4TUlaR0p6TFd4cGRtVXcKZ2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQzM3QVo1TG0xcGh3WFZ1V0ljekFQQ2R5VFBocUpaQy9oaApNKzJWbjZCRERaRGR3SmdDdngxaGFaN3FjZ3VrcFFrM3UzOGZ1QUxpSGFJckdlMDZTMmR0ZzRYdCtLUzF3R21wZ0k3WVJWVFFONDZZCmVJMmE4c1pZdTNMeDhzWUtnTWpmZFUvSGxBQmNNbFdlZHFWa0ZMOGFpeGlUZ1lBU0ZpeHF2dXU0S3F2SUhPQnhhTVNUWkU1eXBiaVEKenJEeFdMOXZPSFJSWWpzcnREK1cwV3N1TVBvQTF2MFpLL2JibmRvclNqNExGK251NjRWV0NBZE9yME8rZWZtRmlqMUp0dXZDY3pCWgozSVVaOEp1N2tZL2xUSkZWTk5uWjlvdFBON0krR1JkNG91R29WSlRuRHhvQ2xyMlUrSXIydVBNNTRuTTdlQ254SmJxdnIyVVFiNjlJCkdtOTdBZ01CQUFHak5qQTBNQk1HQTFVZEVRUU1NQXFDQ0dSaWN5MXNhWFpsTUIwR0ExVWREZ1FXQkJUSTdJN016U1lHZVNEdmcrY1EKRVJJb3NORHlrakFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBQTBnT1o0bk0vcHFob0JCUHZ0RTNid1lkTTRQbWV3MW4zOUFhUUF4VwpWdHBmVEROaytGc0tCMVNJY0VJQ0NuTFd5T1NZS0I0S0VvR0xIczFxbTVOWjArcEI0WlhTczdRbEtEd1hBK3JoditoOHBrSitka1EzCjJPTStQbjROWmNRb0VJTXI0OVN5bTVKZVRkaW9SKzM2dEVRRU4vVXExMmhySFZhSTl3Y1E0ZExCQzVDUFlJQmFDSEpGZzhTUUEwQzEKOXI4d09KSitWeVVSVHJhWGpFcVBWYkdyZERkUTNQVGhJQzZlNXpwQzJIVGUvekkzTExCWnJvRTRQVGE2QUxLblFkYTV2T0craFBlVgpkQWRUZHlidTVWUXNSVTROTm9tNS9Ya0VqNXVLd2ZsZVQ3a1ZYbDlqRmFITmpVMjdGWlphalNRZnRhM3RSTWNiNElxc0VLc0lDdz09PC9kczpYNTA5Q2VydGlmaWNhdGU+PC9kczpYNTA5RGF0YT48L2RzOktleUluZm8+PHhlbmM6Q2lwaGVyRGF0YSB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkNpcGhlclZhbHVlPlZXS1Nqa3Z0bGl0ZnM5OWJPVWR4OHhoQkNidE9nSFZRckJ4SlJubGZ4M1lWeWk0alovUDB1TWYvZ3ZXU2M1aVdSWEpWK3Vmek5mYWEKbmlkdmxFa05sSFNXa0l6UHBtQ254SGFYUktCTmswT2dTem0ybnBvem9KZktCTHJPeXhsL0NVSW0rQmJkaDhUeDhCdjJNY2FiUEpCWAp5WUJmdEVjc0ZKWTlMTkpuYTE3VGV2Nmp0bEplMjVhVE9uS0N6MTVIaVcxcy9BRE42ZkZVc3BUVERhRVFJMUgxUktNNHRab0ZvdkRWCjhxOEhaNWpDL295VEpJTjIydTVYaEtSN0owWmtUL1RneGZUdVJMTHlWeUZBQUF0OGhIVTFUeE5DUnhHWXdFYll3Rk5XWHFTcHI0VkcKVjViV0g2RFRZM0NxMndBSGFzZmVJZjhRM3dLUEhibGExMGM1U3c9PTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lwaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkS2V5PjwvZHM6S2V5SW5mbz48eGVuYzpDaXBoZXJEYXRhIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6Q2lwaGVyVmFsdWU+SUVRQ3RYN2pGRUhza1lWUm1zOWF6alV1V0NRQUEraWEzejZXdzhRZlpxRmtmMVM1bytSVGVuaGRQTXlnRzlERDhuSVk5dXRjQ2lIOApNbitLZVdvbFBtZEpJdGJEcG5kYWNkSVdaSFZwWUNBZmIreG5pWGVNakYvYTJ0QWdMVWUxUTJoaGZYbUNIUjE2NUZpaHFubE1pT0ozCnRtbGRjL0pRMytMSVFUc2xHVDdna1hWS0hET1BHMGNBVGNGVWY3cGpxWlJNVkJpdEVWcnp2Y3NSNC8rUVA5UklKbTgvSmtvdngza2wKUHlrWGRpMStBMXFnbWVmQWRmbnV5aEovbXQ0QWtEZUZFSUJWQTR6b29kWXR1S2wvOG5BVVNuTG5RdjJncVBYQ284dmwyb1NBQjlTZgpQeFB3Y2RsaDJLZXptcHR2cGg5dDZSci8yZU53T2VPN1lnbU1vWDl2RFRFSG9KVU9VWHZsWU1sVTZ5bmlMaHhQd2pEVnhRSDNHS2xQCjdkam84ZmtiSEFNSXZuMXRxSjJleElleWNQU2dZcUphNUdjMmxpSEVjYmpYS2xlTFhyNllHS0JCWE9UdWR6a094aCs2K1FFQ1paOVYKTWJ6THlLcE5BVDEveDRMc29qNUZEWWtxeHFISmliTEIycjkxMXlYak1OL3NYOXhnOFRzR0k0aElRemlxZS9qNGczZU02Y1NNYlNnTgo3QlZVSlFmeVV2eExPK214dWM2RkY1R2hoNktZZ0NtbnZxc044OTBtRFZuUzZ1bUdOOXJLajFVUy9tWnNSeDFHdDdEZjJOVFM0K0Q3CmQvdWVLTCtkUkxFUnk1MFJuZUpRSGd2TC9mK01SSGVrOTZQV2oyek84Y3pycnlRRVBReUpVOEwzMGJQWEpWczhKaHJ5SldDcmFUU2YKd2dsaTVLVTRNa1JJNmNYaDQrcjY2TzIxL0E2Ry9JS2VGUmlIVXBudFcra3ZML1I3NEo1M2ZFT2FYSXlaVnJlMGZVZjF5dklVMTVPMgppNkx5SVl0bW9KbmRpR3MzV05ZQW0vbnZuR0JCektFSXBqaEVLUzIydXE0VWU5N0lXNmRhNG1iYXczd25PUUNtS0tDNXdKcGdNWHZsCnVVMzJhdEZ6cEJoMXVuVlRTV1VNdWdyUHhWRjFTelloMTZndHozSVZ0RVBGMWdvdFArUlNKc09BV0hjU0tnakpTb0FSeG40aXdVYzkKZnV1SzBna1I3V2s1cS9aajZvaEg5Q2oyeSt2NnNlbmpnK1FSbnQxcFo0Uy82TGtzc2x3VWsyYk5CSnBiam9SZ1Z3akNHMk01S245aApoemV0OXA5NnROQjNRZ1RybWhXOXdrQnNFNGlnUVNsRUxXNGJua0llQjlsSlFUK1dhVFR2L3JMUXBzUDhsVlZQZ1BnWTZ4QnhlK3dqCkwyS0VtZnNTS2dNMDE2M3Z6djVnL096UEZ0L21DRnB4aURTSlRsOWh4dEtjZXEzN3l5VDFkM2p2d0FZcVFxSEcrWktITjBwWVZpR2oKcTdqeEVOMExsQUJkWVhWejZHUXU3U2hicFlxeFJPV3JocFg3RkhJQWJ1VkJZNlFvTUVUdHZCM1hYZ1FDMkdhSGduOTRuOXlEQ0RvNApnWlRhVDMzSjJyWUNYR2lPbzN1aUcrczd3Q255dTR2ZHhaUTlCUG8vUG00R3BibmM5UG40OFJDbDR2Ny9GclpTNEpzc094YnhYaW5pCjZpVDJDbjVKSFJjVlFzZE5NQ0hDZTZOcmZMWUg5YTFWOGU4TWEvdHVRbTIrV0NPSXFWa3E5T2lZdUo3MG05Y1Q5c0xXdHJhN3FxQ2wKbGtId0FDVCt5UE05U25tSENvWFIvbWNNSzRjMld2KzMyWGdoWFQ1UzdsSWpoUk56djdUVEVoeVlzYWlsbVVXRVgyUUdqV1NVWEVnVAp4Yk1zdGFJcmlleXNMdFdkbk9YdWJ0d092TkpQZi9NTEVhWmNGamc0NGJCQUhId05jc3k1amRnYm5rV3g0bGtOWmQyRExabWRjam5sCmVHZkl1QXFzalRnN1BsUWRjLzAyRlZtTGZnZit3bE44SkZvdnZNVFdhRzRyQ2ZOdjhlYzBoMStDbkVBNjNsV3Ntb1dJaWY4dGtHV2sKTWlNWDF3UzRxZkhhTDA1ZzVEYTZYOFl2Y1BOb0ZUMU9TYS9JYmx2blpKT0wxZDMxNFR3d2RqZHNQdDJCcEIydGFQRUxxVlRhdWJMTwpXY01xUkovVldNWWRUbmcveGlYbllraHJzUnB5NUpES2Y0dFo3NXJSMUdKTWJwSHh5QnB4TktQSmdNc2ZvQVpxeC9mMU1paXJCSTE4CmRhLzFqYndnaTI2QzJEU2xSdTNVMXZhaGh4THJWeXA2VDVzQWlhbmpuV2ZWeVkzYXljN2NhNWtDU3ZSNHVLRWZwUjdCcDdGZUZZTkgKaGV0cVhaMXV5MWpaRUM0WWFnQ2NrelA5end3K0RCNkdaQlc3Rm94VlZuR0k2THFRYXZTQ213ZjZyQmY4cTY0UFN5TmdJeHVndDhzVwp3dWpOK0FUSEM3TExmMjZyMkhlZjR1eUUzM29tVityeitpZmZacWVqOE1MYTZINWNod21xS1VOTDMrMGlZY3hxZU4vM3p6MlNDMHpMCnozdkFNYmdZRkFSN1B3NXp0SU5Wd2xwT1RUTk9pbUh2MWpiQll6WXc5ZGZyeUt0RGRiMFAyUEpGM2toc1huOWxUZVFIdUVqeldyQzIKRFZwdXVUV1hTR04vejdQOGVjUTUzNDA4MUNJbnR4MEJlUUFIQlpHeEZWQ25uVlJkRDlHc1RHaFhBMTVaRTdUOS9rRUVYK3dTZjRSTQp3ZUZyTElNTlAvdXZ4dVZUbzlyeC9xTXliR2VpWGVkbzVsM3hEV09rUUdJd2lmalF6cFl2YndUQ0JpbC9sWmorV2xSQXE2MTFHUUtIClU3V3NEVXU0SWpSdS8zL0E3Rm1SU005QUFINVl5MzBQN1NGbHFnZ0VuWXZBZEFpUS9CY0gySTg1VDU4QXNtdW96ZzdGMDJsVFh5M1gKK2srRjcwdEo1NS9DamMrZ21zNk1CZE1JREVDRjB2Skl4R0JpaGVRK1g4VWphQXVxTk9NWEN6MkY2NFBzaTQ4WUxiM1RYTnh2UHlRRApvUjBaOUN6dDNSTXBPdVl6TEpPaVhITWpSamtMNG83ZnplcW44cDFSbTNIN2E0YTQrS3RseXdxZkx6dmZlL081UFNZYnRuS29BeGMzClBidm9CRENKTVVCVUlGZVVhMmpoeStTMVFFZ0RJTmtoRHBLUlJBcTNjdUw3Z0xoRW1XbTI0cS9Kd1gxejJ0WU1iNnNwc1FwSzJSdkkKV2grMXlKZk82bDJCaVFnMXNjaFozSENFVTF3NEJNR295QzdnYlowTTloWVQ1QkNUY3lBckpiQ1VCT09JOVl2VFErWUxRbFBDN1pDNApwczhySm9URW8vdGpwS29CN2l1YkkvU1RoQWhEQXY1TzEwR1dpZzFNdkRMeEF3eU1lZ2d2MnB2NnJwOEdnYUxuWFY2a3kvemVINmVFCkN5Z2NGYXFOS1RFTjhkQlVPYmJwWlRlV0kwUnREb1VuOVh1eXhSSUVwa01pTFBvekZXNEhnRDdJNXhyN1hxZ0I4WmpZc0g0aFo3bXkKb0RvdmN3QVcwbmdQc0xZWWF5VFN3aU1lUkdSQzFqOXNaekViNVVIb0hTb0RSMzB1dHpuM2F0TlhweitzS2h0aWNQRFE0R1JXUXFZaQpabjZKdVdvWUNGcGt4MkNuSHd3TlJxaVRpNk9PaGF1Nk1Hb1Z1T2lZQXhMdW5SR3d3d25mME5sMUR4c3h1SHk0S1QxT2xabmxLV2w3CkJDWDAwVlpWalVSUmUxUXltN09XaDFRMy9Wbko8L3hlbmM6Q2lwaGVyVmFsdWU+PC94ZW5jOkNpcGhlckRhdGE+PC94ZW5jOkVuY3J5cHRlZERhdGE+PC9zYW1sMjpFbmNyeXB0ZWRBc3NlcnRpb24+PC9zYW1sMnA6UmVzcG9uc2U+")
                            //many websites redirects the user after login, so follow them /cgi-bin/koha/opac-user.pl#opac-user-checkouts
                            .followRedirects(true)
                            .validateTLSCertificates(false)
                            .execute();
                    Map<String,String>  mapLoginPageCookies5=loginForm3.cookies();
                    Document document = loginForm3.parse();
                    System.out.println(document);
                    Element masthead = document.select(".user_checkouts_count").first();
                    Element masthead2 = document.select(".user_fines_count").first();


                    System.out.println(masthead.text()+"---"+masthead2.text());

                    Connection.Response loginForm4 = Jsoup.connect("https://books.dbs.ie/cgi-bin/koha/opac-user.pl#opac-user-checkouts")
                            //referrer will be the login page's URL
                            .referrer("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s2")
                            //user agent
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                            //connect and read time out
//                            .data("RelayState","ss:mem:7d02e79494ffb3d25c5d1e8603ba8e6c3a6b862c3cbcbd6bf7c2040199169bf9" )
//                            .data("SAMLResponse",auth_token)
                            .timeout(10 * 1000)
                            //post parameters
                            .cookies(mapLoginPageCookies5)
//                            .data("SAMLResponse","PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBEZXN0aW5hdGlvbj0iaHR0cHM6Ly9ib29rcy5kYnMuaWUvU2hpYmJvbGV0aC5zc28vU0FNTDIvUE9TVCIgSUQ9Il83NjgwZDE4MTE2OGIwMTI1NTUyZmYxYTI5MTk4ZGUyNiIgSW5SZXNwb25zZVRvPSJfNTZlMDA3MmFlZjQzZjgxZjkzZjRjZDg1NTg3Mzc5YTkiIElzc3VlSW5zdGFudD0iMjAxOS0wNy0xN1QwMzowMToyOS43NjNaIiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj48c2FtbDI6SXNzdWVyIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwczovL3dlYmF1dGguZGJzLmllL2lkcC9zaGliYm9sZXRoPC9zYW1sMjpJc3N1ZXI+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+CjxkczpTaWduZWRJbmZvPgo8ZHM6Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8ZHM6U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjcnNhLXNoYTUxMiIvPgo8ZHM6UmVmZXJlbmNlIFVSST0iI183NjgwZDE4MTE2OGIwMTI1NTUyZmYxYTI5MTk4ZGUyNiI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGE1MTIiLz4KPGRzOkRpZ2VzdFZhbHVlPklSU0JCL0FMY0YwOUVVR01FMTBwc3RQT2lLTFJSRUgzNkluVmltbDNrbXRLQ2JINkpTOGJzQ0hrZnVyK3JLbXFVRG1ZcU9maU8wSU0KWWVxWkxaVmhwUT09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpPcHhqZ2hSZ3JlQndQb3EySXRpV1hBMjFza3hjSVBSQzNqUFlsSVc0aXA3ak5WNlYwdzNuRlpyRWhGZVlGNll2RXd2Q2Y1S2J6bG9xCjJGOFZmdFpPRGpvWlNrWk9tRFhaRjBkM2pianhhM0JQd3NQL25CMFpxbUpLNmxTSzVIdGZJeUI4QjhlV1Z5eEQ3V2ZPSCtGZmlPRmsKaVEvU1BhTUNyZ1JYWHRobTlzMnVGcTNXSVRMRzcrci9SV011RkJjRzJOS3RTYjdEWlNTcTBxMXNNd0lRTUJYMmFBdFZaUDJLYUZzSQpqR0o2Z0d2dWZMRU13MXZWVWQyWmhxV1ZSZ24yU2hnb1FLWkZVN3ZGb3NQMDZUNXFsQ1VuYUN2T2dhL3lvV09vblhiVnNMcEQ5ZFdiClRHSlUzeXgwM2RCbS9Na2NOWVlvSkZ5cVUvNGdxaW1tcnpSemxBPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPGRzOktleUluZm8+PGRzOlg1MDlEYXRhPjxkczpYNTA5Q2VydGlmaWNhdGU+TUlJREpEQ0NBZ3lnQXdJQkFnSVZBSVpCaS9OVTljK3o1SHoveHJLbDM3NksxWkdDTUEwR0NTcUdTSWIzRFFFQkN3VUFNQmt4RnpBVgpCZ05WQkFNTURuZGxZbUYxZEdndVpHSnpMbWxsTUI0WERURTJNRFl3TkRFM05UZzFORm9YRFRNMk1EWXdOREUzTlRnMU5Gb3dHVEVYCk1CVUdBMVVFQXd3T2QyVmlZWFYwYUM1a1luTXVhV1V3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ1IKcXlHemFjRWVKQVhMVkt2Y2NyRnhPNjBYTURHaG91WDZPSlIwd0g3VGJMTUZjeHVFb3AvYnFqZGFUQzRSdE5XWmVmODRxbEVOQ3BPSAp0WU5hckRKak9wQjNWUEJMSi9SZGVEMlZTalEweDdVTFd6eXBPRkVlYU04QUZCZTl3eXJQUHZRakhuV3VKWlJQWE9iaHZ3YlUrMjJKClNSVnA5aTE3b0VlUGc2dkZ6MHBFdVhsd1BaVmJyZDNBMDlLUmplNkpjZk5ER1U5QjJ5SVpTTjFOdjNFTnZoWm1HT0ZwdThpaHMzSFUKU0lDVjlxU0t4d2JrYmJFNDAwRXpCb0RFTHhoalRORHlkUG96NzhPcHBxWEFsN0xOdHE4bzlENHRUYmQ0QSt0MElHL2IybE5zVVZxTwp2YSs0dEU1V0h0STBLSXV2ODVqcHE4cDcydHJYYnVsM2RKWkZBZ01CQUFHall6QmhNQjBHQTFVZERnUVdCQlNRckFTWHJlRU55cjg3CnV4b2hlV2xxUU9RcHBUQkFCZ05WSFJFRU9UQTNnZzUzWldKaGRYUm9MbVJpY3k1cFpZWWxhSFIwY0hNNkx5OTNaV0poZFhSb0xtUmkKY3k1cFpTOXBaSEF2YzJocFltSnZiR1YwYURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQWMxZHRTalM5NHdNeVpRYzJEQnRkd2FLTAp0UFRrbzN2Q0xVbFowOXRTZHNnVU9nT0c3TkgvMDBOLytXWkV2YVNqTTk2a053VEhxdEluR0xLTkJGdXNiaDl1b1ZGTExaRjA2bGJmCk8vMFlqMCtUV2xMOHJ5L1EvYkRhZUFhYTZidmV6N2taZ1E3Q0NDNHBDRHBlS3A4N25TemgvcGV5MHJaS29lTWg2Q0RzLzBlN3RwSkUKdmtveVpyOTNXNEl6YURDNWFSTVp1aDVZWHV2czc2UDRYY0t6MlBnQUZ4dmM2bWNWaFJSZXJ1THE4c2pqcEFoYkJndmFpWnZ1WlJGeQpjYkZWUWhNdlBMODFGc0dWdU1pSlRlQ2hiV2RmSnRrSVhaTGFldFpJVlFvcTg2TGFxMEM5U2ErQ0hqOVZQcnltRHBwVkhQaklXSWNVClJjS1ArV3Jxa3Z6bkJ3PT08L2RzOlg1MDlDZXJ0aWZpY2F0ZT48L2RzOlg1MDlEYXRhPjwvZHM6S2V5SW5mbz48L2RzOlNpZ25hdHVyZT48c2FtbDJwOlN0YXR1cz48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6RW5jcnlwdGVkQXNzZXJ0aW9uIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48eGVuYzpFbmNyeXB0ZWREYXRhIElkPSJfNjI4NTUwMDA5NjdiZjE3ZjM3NTBjNTc0YmEwMzNiYjciIFR5cGU9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI0VsZW1lbnQiIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6RW5jcnlwdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjYWVzMTI4LWdjbSIgeG1sbnM6eGVuYz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjIi8+PGRzOktleUluZm8geG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjx4ZW5jOkVuY3J5cHRlZEtleSBJZD0iX2VkYTg0NDJiMjg5YmEyNTE5YmZiNjJjYjMyMzEwMWRjIiBSZWNpcGllbnQ9Imh0dHBzOi8vYm9va3MuZGJzLmllIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkVuY3J5cHRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA5L3htbGVuYzExI3JzYS1vYWVwIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiLz48eGVuYzExOk1HRiBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjbWdmMXNoYTEiIHhtbG5zOnhlbmMxMT0iaHR0cDovL3d3dy53My5vcmcvMjAwOS94bWxlbmMxMSMiLz48L3hlbmM6RW5jcnlwdGlvbk1ldGhvZD48ZHM6S2V5SW5mbz48ZHM6WDUwOURhdGE+PGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlDM3pDQ0FjZWdBd0lCQWdJSkFNZUhmS3dkU2R5U01BMEdDU3FHU0liM0RRRUJCUVVBTUJNeEVUQVBCZ05WQkFNVENHUmljeTFzCmFYWmxNQjRYRFRFM01ERXlOekE1TWpZek9Gb1hEVEkzTURFeU5UQTVNall6T0Zvd0V6RVJNQThHQTFVRUF4TUlaR0p6TFd4cGRtVXcKZ2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQzM3QVo1TG0xcGh3WFZ1V0ljekFQQ2R5VFBocUpaQy9oaApNKzJWbjZCRERaRGR3SmdDdngxaGFaN3FjZ3VrcFFrM3UzOGZ1QUxpSGFJckdlMDZTMmR0ZzRYdCtLUzF3R21wZ0k3WVJWVFFONDZZCmVJMmE4c1pZdTNMeDhzWUtnTWpmZFUvSGxBQmNNbFdlZHFWa0ZMOGFpeGlUZ1lBU0ZpeHF2dXU0S3F2SUhPQnhhTVNUWkU1eXBiaVEKenJEeFdMOXZPSFJSWWpzcnREK1cwV3N1TVBvQTF2MFpLL2JibmRvclNqNExGK251NjRWV0NBZE9yME8rZWZtRmlqMUp0dXZDY3pCWgozSVVaOEp1N2tZL2xUSkZWTk5uWjlvdFBON0krR1JkNG91R29WSlRuRHhvQ2xyMlUrSXIydVBNNTRuTTdlQ254SmJxdnIyVVFiNjlJCkdtOTdBZ01CQUFHak5qQTBNQk1HQTFVZEVRUU1NQXFDQ0dSaWN5MXNhWFpsTUIwR0ExVWREZ1FXQkJUSTdJN016U1lHZVNEdmcrY1EKRVJJb3NORHlrakFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBQTBnT1o0bk0vcHFob0JCUHZ0RTNid1lkTTRQbWV3MW4zOUFhUUF4VwpWdHBmVEROaytGc0tCMVNJY0VJQ0NuTFd5T1NZS0I0S0VvR0xIczFxbTVOWjArcEI0WlhTczdRbEtEd1hBK3JoditoOHBrSitka1EzCjJPTStQbjROWmNRb0VJTXI0OVN5bTVKZVRkaW9SKzM2dEVRRU4vVXExMmhySFZhSTl3Y1E0ZExCQzVDUFlJQmFDSEpGZzhTUUEwQzEKOXI4d09KSitWeVVSVHJhWGpFcVBWYkdyZERkUTNQVGhJQzZlNXpwQzJIVGUvekkzTExCWnJvRTRQVGE2QUxLblFkYTV2T0craFBlVgpkQWRUZHlidTVWUXNSVTROTm9tNS9Ya0VqNXVLd2ZsZVQ3a1ZYbDlqRmFITmpVMjdGWlphalNRZnRhM3RSTWNiNElxc0VLc0lDdz09PC9kczpYNTA5Q2VydGlmaWNhdGU+PC9kczpYNTA5RGF0YT48L2RzOktleUluZm8+PHhlbmM6Q2lwaGVyRGF0YSB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkNpcGhlclZhbHVlPlZXS1Nqa3Z0bGl0ZnM5OWJPVWR4OHhoQkNidE9nSFZRckJ4SlJubGZ4M1lWeWk0alovUDB1TWYvZ3ZXU2M1aVdSWEpWK3Vmek5mYWEKbmlkdmxFa05sSFNXa0l6UHBtQ254SGFYUktCTmswT2dTem0ybnBvem9KZktCTHJPeXhsL0NVSW0rQmJkaDhUeDhCdjJNY2FiUEpCWAp5WUJmdEVjc0ZKWTlMTkpuYTE3VGV2Nmp0bEplMjVhVE9uS0N6MTVIaVcxcy9BRE42ZkZVc3BUVERhRVFJMUgxUktNNHRab0ZvdkRWCjhxOEhaNWpDL295VEpJTjIydTVYaEtSN0owWmtUL1RneGZUdVJMTHlWeUZBQUF0OGhIVTFUeE5DUnhHWXdFYll3Rk5XWHFTcHI0VkcKVjViV0g2RFRZM0NxMndBSGFzZmVJZjhRM3dLUEhibGExMGM1U3c9PTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lwaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkS2V5PjwvZHM6S2V5SW5mbz48eGVuYzpDaXBoZXJEYXRhIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6Q2lwaGVyVmFsdWU+SUVRQ3RYN2pGRUhza1lWUm1zOWF6alV1V0NRQUEraWEzejZXdzhRZlpxRmtmMVM1bytSVGVuaGRQTXlnRzlERDhuSVk5dXRjQ2lIOApNbitLZVdvbFBtZEpJdGJEcG5kYWNkSVdaSFZwWUNBZmIreG5pWGVNakYvYTJ0QWdMVWUxUTJoaGZYbUNIUjE2NUZpaHFubE1pT0ozCnRtbGRjL0pRMytMSVFUc2xHVDdna1hWS0hET1BHMGNBVGNGVWY3cGpxWlJNVkJpdEVWcnp2Y3NSNC8rUVA5UklKbTgvSmtvdngza2wKUHlrWGRpMStBMXFnbWVmQWRmbnV5aEovbXQ0QWtEZUZFSUJWQTR6b29kWXR1S2wvOG5BVVNuTG5RdjJncVBYQ284dmwyb1NBQjlTZgpQeFB3Y2RsaDJLZXptcHR2cGg5dDZSci8yZU53T2VPN1lnbU1vWDl2RFRFSG9KVU9VWHZsWU1sVTZ5bmlMaHhQd2pEVnhRSDNHS2xQCjdkam84ZmtiSEFNSXZuMXRxSjJleElleWNQU2dZcUphNUdjMmxpSEVjYmpYS2xlTFhyNllHS0JCWE9UdWR6a094aCs2K1FFQ1paOVYKTWJ6THlLcE5BVDEveDRMc29qNUZEWWtxeHFISmliTEIycjkxMXlYak1OL3NYOXhnOFRzR0k0aElRemlxZS9qNGczZU02Y1NNYlNnTgo3QlZVSlFmeVV2eExPK214dWM2RkY1R2hoNktZZ0NtbnZxc044OTBtRFZuUzZ1bUdOOXJLajFVUy9tWnNSeDFHdDdEZjJOVFM0K0Q3CmQvdWVLTCtkUkxFUnk1MFJuZUpRSGd2TC9mK01SSGVrOTZQV2oyek84Y3pycnlRRVBReUpVOEwzMGJQWEpWczhKaHJ5SldDcmFUU2YKd2dsaTVLVTRNa1JJNmNYaDQrcjY2TzIxL0E2Ry9JS2VGUmlIVXBudFcra3ZML1I3NEo1M2ZFT2FYSXlaVnJlMGZVZjF5dklVMTVPMgppNkx5SVl0bW9KbmRpR3MzV05ZQW0vbnZuR0JCektFSXBqaEVLUzIydXE0VWU5N0lXNmRhNG1iYXczd25PUUNtS0tDNXdKcGdNWHZsCnVVMzJhdEZ6cEJoMXVuVlRTV1VNdWdyUHhWRjFTelloMTZndHozSVZ0RVBGMWdvdFArUlNKc09BV0hjU0tnakpTb0FSeG40aXdVYzkKZnV1SzBna1I3V2s1cS9aajZvaEg5Q2oyeSt2NnNlbmpnK1FSbnQxcFo0Uy82TGtzc2x3VWsyYk5CSnBiam9SZ1Z3akNHMk01S245aApoemV0OXA5NnROQjNRZ1RybWhXOXdrQnNFNGlnUVNsRUxXNGJua0llQjlsSlFUK1dhVFR2L3JMUXBzUDhsVlZQZ1BnWTZ4QnhlK3dqCkwyS0VtZnNTS2dNMDE2M3Z6djVnL096UEZ0L21DRnB4aURTSlRsOWh4dEtjZXEzN3l5VDFkM2p2d0FZcVFxSEcrWktITjBwWVZpR2oKcTdqeEVOMExsQUJkWVhWejZHUXU3U2hicFlxeFJPV3JocFg3RkhJQWJ1VkJZNlFvTUVUdHZCM1hYZ1FDMkdhSGduOTRuOXlEQ0RvNApnWlRhVDMzSjJyWUNYR2lPbzN1aUcrczd3Q255dTR2ZHhaUTlCUG8vUG00R3BibmM5UG40OFJDbDR2Ny9GclpTNEpzc094YnhYaW5pCjZpVDJDbjVKSFJjVlFzZE5NQ0hDZTZOcmZMWUg5YTFWOGU4TWEvdHVRbTIrV0NPSXFWa3E5T2lZdUo3MG05Y1Q5c0xXdHJhN3FxQ2wKbGtId0FDVCt5UE05U25tSENvWFIvbWNNSzRjMld2KzMyWGdoWFQ1UzdsSWpoUk56djdUVEVoeVlzYWlsbVVXRVgyUUdqV1NVWEVnVAp4Yk1zdGFJcmlleXNMdFdkbk9YdWJ0d092TkpQZi9NTEVhWmNGamc0NGJCQUhId05jc3k1amRnYm5rV3g0bGtOWmQyRExabWRjam5sCmVHZkl1QXFzalRnN1BsUWRjLzAyRlZtTGZnZit3bE44SkZvdnZNVFdhRzRyQ2ZOdjhlYzBoMStDbkVBNjNsV3Ntb1dJaWY4dGtHV2sKTWlNWDF3UzRxZkhhTDA1ZzVEYTZYOFl2Y1BOb0ZUMU9TYS9JYmx2blpKT0wxZDMxNFR3d2RqZHNQdDJCcEIydGFQRUxxVlRhdWJMTwpXY01xUkovVldNWWRUbmcveGlYbllraHJzUnB5NUpES2Y0dFo3NXJSMUdKTWJwSHh5QnB4TktQSmdNc2ZvQVpxeC9mMU1paXJCSTE4CmRhLzFqYndnaTI2QzJEU2xSdTNVMXZhaGh4THJWeXA2VDVzQWlhbmpuV2ZWeVkzYXljN2NhNWtDU3ZSNHVLRWZwUjdCcDdGZUZZTkgKaGV0cVhaMXV5MWpaRUM0WWFnQ2NrelA5end3K0RCNkdaQlc3Rm94VlZuR0k2THFRYXZTQ213ZjZyQmY4cTY0UFN5TmdJeHVndDhzVwp3dWpOK0FUSEM3TExmMjZyMkhlZjR1eUUzM29tVityeitpZmZacWVqOE1MYTZINWNod21xS1VOTDMrMGlZY3hxZU4vM3p6MlNDMHpMCnozdkFNYmdZRkFSN1B3NXp0SU5Wd2xwT1RUTk9pbUh2MWpiQll6WXc5ZGZyeUt0RGRiMFAyUEpGM2toc1huOWxUZVFIdUVqeldyQzIKRFZwdXVUV1hTR04vejdQOGVjUTUzNDA4MUNJbnR4MEJlUUFIQlpHeEZWQ25uVlJkRDlHc1RHaFhBMTVaRTdUOS9rRUVYK3dTZjRSTQp3ZUZyTElNTlAvdXZ4dVZUbzlyeC9xTXliR2VpWGVkbzVsM3hEV09rUUdJd2lmalF6cFl2YndUQ0JpbC9sWmorV2xSQXE2MTFHUUtIClU3V3NEVXU0SWpSdS8zL0E3Rm1SU005QUFINVl5MzBQN1NGbHFnZ0VuWXZBZEFpUS9CY0gySTg1VDU4QXNtdW96ZzdGMDJsVFh5M1gKK2srRjcwdEo1NS9DamMrZ21zNk1CZE1JREVDRjB2Skl4R0JpaGVRK1g4VWphQXVxTk9NWEN6MkY2NFBzaTQ4WUxiM1RYTnh2UHlRRApvUjBaOUN6dDNSTXBPdVl6TEpPaVhITWpSamtMNG83ZnplcW44cDFSbTNIN2E0YTQrS3RseXdxZkx6dmZlL081UFNZYnRuS29BeGMzClBidm9CRENKTVVCVUlGZVVhMmpoeStTMVFFZ0RJTmtoRHBLUlJBcTNjdUw3Z0xoRW1XbTI0cS9Kd1gxejJ0WU1iNnNwc1FwSzJSdkkKV2grMXlKZk82bDJCaVFnMXNjaFozSENFVTF3NEJNR295QzdnYlowTTloWVQ1QkNUY3lBckpiQ1VCT09JOVl2VFErWUxRbFBDN1pDNApwczhySm9URW8vdGpwS29CN2l1YkkvU1RoQWhEQXY1TzEwR1dpZzFNdkRMeEF3eU1lZ2d2MnB2NnJwOEdnYUxuWFY2a3kvemVINmVFCkN5Z2NGYXFOS1RFTjhkQlVPYmJwWlRlV0kwUnREb1VuOVh1eXhSSUVwa01pTFBvekZXNEhnRDdJNXhyN1hxZ0I4WmpZc0g0aFo3bXkKb0RvdmN3QVcwbmdQc0xZWWF5VFN3aU1lUkdSQzFqOXNaekViNVVIb0hTb0RSMzB1dHpuM2F0TlhweitzS2h0aWNQRFE0R1JXUXFZaQpabjZKdVdvWUNGcGt4MkNuSHd3TlJxaVRpNk9PaGF1Nk1Hb1Z1T2lZQXhMdW5SR3d3d25mME5sMUR4c3h1SHk0S1QxT2xabmxLV2w3CkJDWDAwVlpWalVSUmUxUXltN09XaDFRMy9Wbko8L3hlbmM6Q2lwaGVyVmFsdWU+PC94ZW5jOkNpcGhlckRhdGE+PC94ZW5jOkVuY3J5cHRlZERhdGE+PC9zYW1sMjpFbmNyeXB0ZWRBc3NlcnRpb24+PC9zYW1sMnA6UmVzcG9uc2U+")
                            //many websites redirects the user after login, so follow them /cgi-bin/koha/opac-user.pl#opac-user-checkouts
                            .followRedirects(true)
                            .validateTLSCertificates(false)
                            .execute();

                    Document document2 = loginForm4.parse();
                    System.out.println(document2);
                    System.out.println("Blog:");
//                    Element booklist = document2.getElementById("checkoutst");
//                    Elements booklist = document2.select(".author");
//                    Elements booklist = document2.select(".author");
//                    Elements booklist = document2.select(".title >a");
//                    Elements booklist = document2.select(".date_due > span");
                    Elements booklist = document2.select(".renew > a");
                    System.out.println("Blog:");
                    System.out.println("Blog:"+booklist.attr("abs:href"));
                    for (Element tr : booklist) {

                        System.out.println("Blog2:"+tr.attr("abs:href"));
                    }

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
