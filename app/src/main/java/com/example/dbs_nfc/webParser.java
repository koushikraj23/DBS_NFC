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
                            .data("j_password", "Kou@23021995")
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

                    mapLoginPageCookies3.put(mapLoginPageCookies4.keySet().toArray()[0].toString(),mapLoginPageCookies4.get(mapLoginPageCookies4.keySet().toArray()[0]));
                    System.out.println(mapLoginPageCookies3);
                    Connection.Response loginForm3 = Jsoup.connect("https://books.dbs.ie/Shibboleth.sso/SAML2/POST")
                            //referrer will be the login page's URL
                            .referrer("https://webauth.dbs.ie/idp/profile/SAML2/Redirect/SSO?execution=e1s2")
                            //user agent
                            .userAgent("Mozilla")
                            //connect and read time out
                            .timeout(10 * 1000)
                            //post parameters
                            .data("RelayState","ss:mem:7d02e79494ffb3d25c5d1e8603ba8e6c3a6b862c3cbcbd6bf7c2040199169bf9" )
                            .data("SAMLResponse","PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHNhbWwycDpSZXNwb25zZSBEZXN0aW5hdGlvbj0iaHR0cHM6Ly9ib29rcy5kYnMuaWUvU2hpYmJvbGV0aC5zc28vU0FNTDIvUE9TVCIgSUQ9Il80Y2U4NTkyMTc5NjlkMTNlM2E1NmYzZTVjZDkzYTM1OSIgSW5SZXNwb25zZVRvPSJfMWFhYzA4ODNkZTRmYWFkYTM1MzZjYzYwNTlkNGIwNzYiIElzc3VlSW5zdGFudD0iMjAxOS0wNy0xNlQxMDoyMDozNy4xMzFaIiBWZXJzaW9uPSIyLjAiIHhtbG5zOnNhbWwycD0idXJuOm9hc2lzOm5hbWVzOnRjOlNBTUw6Mi4wOnByb3RvY29sIj48c2FtbDI6SXNzdWVyIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj5odHRwczovL3dlYmF1dGguZGJzLmllL2lkcC9zaGliYm9sZXRoPC9zYW1sMjpJc3N1ZXI+PGRzOlNpZ25hdHVyZSB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+CjxkczpTaWduZWRJbmZvPgo8ZHM6Q2Fub25pY2FsaXphdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8ZHM6U2lnbmF0dXJlTWV0aG9kIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxkc2lnLW1vcmUjcnNhLXNoYTUxMiIvPgo8ZHM6UmVmZXJlbmNlIFVSST0iI180Y2U4NTkyMTc5NjlkMTNlM2E1NmYzZTVjZDkzYTM1OSI+CjxkczpUcmFuc2Zvcm1zPgo8ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnI2VudmVsb3BlZC1zaWduYXR1cmUiLz4KPGRzOlRyYW5zZm9ybSBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMTAveG1sLWV4Yy1jMTRuIyIvPgo8L2RzOlRyYW5zZm9ybXM+CjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGE1MTIiLz4KPGRzOkRpZ2VzdFZhbHVlPlJhbTVJamdnSFVDelJ4SkJweUwwUEVESFkxV21LVW9menptb01ZK2cyeCtubnhlL3dCaXNvUjFrSlVsQ3pnK1hIRUJ1QVZ3eElYUEsKUERWTlFldWJlZz09PC9kczpEaWdlc3RWYWx1ZT4KPC9kczpSZWZlcmVuY2U+CjwvZHM6U2lnbmVkSW5mbz4KPGRzOlNpZ25hdHVyZVZhbHVlPgpldTk4UGpUMHpjR05UeldMMCs5dlYzbDZzaWpXTXBnakVnWG9wK2tDNHJMRXQ2WGl6bzhnWkRLVlk3bWxHVWs1cTZWTzQ5SkNpVmNyCjlCTTVRTmY3bkxTd1pmdjU0Vm55QTFZemdPOXRSaHdrTmFCU1RWZE56ekoxektCOThNemRZRDl3MW0yMnNQWmt4SlhjMzNKZUVUc0wKZkl2SnNFbjBzeFQ0Um9rSHJVSHdVVGpYMFRMN3o5Q09YQ1lnOTFhSHhpM0JvMm4rOTMzWmsybk5TS0pwOVgrM1Mvdm52cHRPakNFYQoyTTNxa0hGWWs5Z1pyZWtVQWxub2x4U0NuYStSaDlqaG5jZzBNdWRNMjMweHRTamRVUnRyY05RZlZEY3dXamtxQWxid0ZQVWYwQmpECjlnY3BXcHVBcXRhSnAwVW1HOU84Wm1SR2FoTEIwUUNUUTk1bm9RPT0KPC9kczpTaWduYXR1cmVWYWx1ZT4KPGRzOktleUluZm8+PGRzOlg1MDlEYXRhPjxkczpYNTA5Q2VydGlmaWNhdGU+TUlJREpEQ0NBZ3lnQXdJQkFnSVZBSVpCaS9OVTljK3o1SHoveHJLbDM3NksxWkdDTUEwR0NTcUdTSWIzRFFFQkN3VUFNQmt4RnpBVgpCZ05WQkFNTURuZGxZbUYxZEdndVpHSnpMbWxsTUI0WERURTJNRFl3TkRFM05UZzFORm9YRFRNMk1EWXdOREUzTlRnMU5Gb3dHVEVYCk1CVUdBMVVFQXd3T2QyVmlZWFYwYUM1a1luTXVhV1V3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQ1IKcXlHemFjRWVKQVhMVkt2Y2NyRnhPNjBYTURHaG91WDZPSlIwd0g3VGJMTUZjeHVFb3AvYnFqZGFUQzRSdE5XWmVmODRxbEVOQ3BPSAp0WU5hckRKak9wQjNWUEJMSi9SZGVEMlZTalEweDdVTFd6eXBPRkVlYU04QUZCZTl3eXJQUHZRakhuV3VKWlJQWE9iaHZ3YlUrMjJKClNSVnA5aTE3b0VlUGc2dkZ6MHBFdVhsd1BaVmJyZDNBMDlLUmplNkpjZk5ER1U5QjJ5SVpTTjFOdjNFTnZoWm1HT0ZwdThpaHMzSFUKU0lDVjlxU0t4d2JrYmJFNDAwRXpCb0RFTHhoalRORHlkUG96NzhPcHBxWEFsN0xOdHE4bzlENHRUYmQ0QSt0MElHL2IybE5zVVZxTwp2YSs0dEU1V0h0STBLSXV2ODVqcHE4cDcydHJYYnVsM2RKWkZBZ01CQUFHall6QmhNQjBHQTFVZERnUVdCQlNRckFTWHJlRU55cjg3CnV4b2hlV2xxUU9RcHBUQkFCZ05WSFJFRU9UQTNnZzUzWldKaGRYUm9MbVJpY3k1cFpZWWxhSFIwY0hNNkx5OTNaV0poZFhSb0xtUmkKY3k1cFpTOXBaSEF2YzJocFltSnZiR1YwYURBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQWMxZHRTalM5NHdNeVpRYzJEQnRkd2FLTAp0UFRrbzN2Q0xVbFowOXRTZHNnVU9nT0c3TkgvMDBOLytXWkV2YVNqTTk2a053VEhxdEluR0xLTkJGdXNiaDl1b1ZGTExaRjA2bGJmCk8vMFlqMCtUV2xMOHJ5L1EvYkRhZUFhYTZidmV6N2taZ1E3Q0NDNHBDRHBlS3A4N25TemgvcGV5MHJaS29lTWg2Q0RzLzBlN3RwSkUKdmtveVpyOTNXNEl6YURDNWFSTVp1aDVZWHV2czc2UDRYY0t6MlBnQUZ4dmM2bWNWaFJSZXJ1THE4c2pqcEFoYkJndmFpWnZ1WlJGeQpjYkZWUWhNdlBMODFGc0dWdU1pSlRlQ2hiV2RmSnRrSVhaTGFldFpJVlFvcTg2TGFxMEM5U2ErQ0hqOVZQcnltRHBwVkhQaklXSWNVClJjS1ArV3Jxa3Z6bkJ3PT08L2RzOlg1MDlDZXJ0aWZpY2F0ZT48L2RzOlg1MDlEYXRhPjwvZHM6S2V5SW5mbz48L2RzOlNpZ25hdHVyZT48c2FtbDJwOlN0YXR1cz48c2FtbDJwOlN0YXR1c0NvZGUgVmFsdWU9InVybjpvYXNpczpuYW1lczp0YzpTQU1MOjIuMDpzdGF0dXM6U3VjY2VzcyIvPjwvc2FtbDJwOlN0YXR1cz48c2FtbDI6RW5jcnlwdGVkQXNzZXJ0aW9uIHhtbG5zOnNhbWwyPSJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YXNzZXJ0aW9uIj48eGVuYzpFbmNyeXB0ZWREYXRhIElkPSJfMjU2M2UwODk3MTI0NzYzZTU4MWFkZWZlZGQyMzg3ZTYiIFR5cGU9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI0VsZW1lbnQiIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6RW5jcnlwdGlvbk1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjYWVzMTI4LWdjbSIgeG1sbnM6eGVuYz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS8wNC94bWxlbmMjIi8+PGRzOktleUluZm8geG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiPjx4ZW5jOkVuY3J5cHRlZEtleSBJZD0iX2E1YTlhZmQ1OGY3Zjg2NzBjZDllZGNhYmU5YzFjY2QzIiBSZWNpcGllbnQ9Imh0dHBzOi8vYm9va3MuZGJzLmllIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkVuY3J5cHRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDA5L3htbGVuYzExI3JzYS1vYWVwIiB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjc2hhMSIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiLz48eGVuYzExOk1HRiBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDkveG1sZW5jMTEjbWdmMXNoYTEiIHhtbG5zOnhlbmMxMT0iaHR0cDovL3d3dy53My5vcmcvMjAwOS94bWxlbmMxMSMiLz48L3hlbmM6RW5jcnlwdGlvbk1ldGhvZD48ZHM6S2V5SW5mbz48ZHM6WDUwOURhdGE+PGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlDM3pDQ0FjZWdBd0lCQWdJSkFNZUhmS3dkU2R5U01BMEdDU3FHU0liM0RRRUJCUVVBTUJNeEVUQVBCZ05WQkFNVENHUmljeTFzCmFYWmxNQjRYRFRFM01ERXlOekE1TWpZek9Gb1hEVEkzTURFeU5UQTVNall6T0Zvd0V6RVJNQThHQTFVRUF4TUlaR0p6TFd4cGRtVXcKZ2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRQzM3QVo1TG0xcGh3WFZ1V0ljekFQQ2R5VFBocUpaQy9oaApNKzJWbjZCRERaRGR3SmdDdngxaGFaN3FjZ3VrcFFrM3UzOGZ1QUxpSGFJckdlMDZTMmR0ZzRYdCtLUzF3R21wZ0k3WVJWVFFONDZZCmVJMmE4c1pZdTNMeDhzWUtnTWpmZFUvSGxBQmNNbFdlZHFWa0ZMOGFpeGlUZ1lBU0ZpeHF2dXU0S3F2SUhPQnhhTVNUWkU1eXBiaVEKenJEeFdMOXZPSFJSWWpzcnREK1cwV3N1TVBvQTF2MFpLL2JibmRvclNqNExGK251NjRWV0NBZE9yME8rZWZtRmlqMUp0dXZDY3pCWgozSVVaOEp1N2tZL2xUSkZWTk5uWjlvdFBON0krR1JkNG91R29WSlRuRHhvQ2xyMlUrSXIydVBNNTRuTTdlQ254SmJxdnIyVVFiNjlJCkdtOTdBZ01CQUFHak5qQTBNQk1HQTFVZEVRUU1NQXFDQ0dSaWN5MXNhWFpsTUIwR0ExVWREZ1FXQkJUSTdJN016U1lHZVNEdmcrY1EKRVJJb3NORHlrakFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBQTBnT1o0bk0vcHFob0JCUHZ0RTNid1lkTTRQbWV3MW4zOUFhUUF4VwpWdHBmVEROaytGc0tCMVNJY0VJQ0NuTFd5T1NZS0I0S0VvR0xIczFxbTVOWjArcEI0WlhTczdRbEtEd1hBK3JoditoOHBrSitka1EzCjJPTStQbjROWmNRb0VJTXI0OVN5bTVKZVRkaW9SKzM2dEVRRU4vVXExMmhySFZhSTl3Y1E0ZExCQzVDUFlJQmFDSEpGZzhTUUEwQzEKOXI4d09KSitWeVVSVHJhWGpFcVBWYkdyZERkUTNQVGhJQzZlNXpwQzJIVGUvekkzTExCWnJvRTRQVGE2QUxLblFkYTV2T0craFBlVgpkQWRUZHlidTVWUXNSVTROTm9tNS9Ya0VqNXVLd2ZsZVQ3a1ZYbDlqRmFITmpVMjdGWlphalNRZnRhM3RSTWNiNElxc0VLc0lDdz09PC9kczpYNTA5Q2VydGlmaWNhdGU+PC9kczpYNTA5RGF0YT48L2RzOktleUluZm8+PHhlbmM6Q2lwaGVyRGF0YSB4bWxuczp4ZW5jPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyMiPjx4ZW5jOkNpcGhlclZhbHVlPmxTcUZ2UDNGL3h3b080OWV1bThVOXFqejlwUXdqakRFODhQeEcwbUpzeVROS0c3VTVOK0dpTXNVZGIyYTM2dnhPMWkySkd0MHd2NUoKdXV1c04wMHRtVXA4aGMyTGJDbWF1YmlmVFlUc3l0aWZHZFQwNDErbi9OajF5SkFWdUJvSThrbHI1RklWbTFMcnZRajFFWGdHdjZoKwpCV3NyZmh2Q1NpWW84RXQ4SkRjUVBUT3F0Q1hDTDY1RUEvYStXYmFGaC9ITkJZOTd0QkIzU05xclQ2QzZZMVg0ZWEwWXlDLzRJK2JRClRSeWIxNGF1cHZ3UlBRcDU0cnh0b2JKWHJJYmt1UXRScmUxQW4vT2VsejQycFdNbEtod213cU1aVmM0bjlZd3FRTTlQV0Z1aENQRHQKaFA1TWVVYURLbUdFVG1DOWJvNzF5enpjaWhqRi9oWmtFZVU3Y2c9PTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lwaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkS2V5PjwvZHM6S2V5SW5mbz48eGVuYzpDaXBoZXJEYXRhIHhtbG5zOnhlbmM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jIyI+PHhlbmM6Q2lwaGVyVmFsdWU+TTBNSHJZeVpRUE5mSXRZM2VIUkZkOFhtOGlvYU5FMytaRHhnNVJQVW5ScGIxNnEwSnBOVkF4b0k1Um5Oa3REMHRRUjJSNTJNVHF5dwpIWHN1dGhLdEw0YVR3S0o0RUVTbHFUZnE1ZFUyL09ZNThGOXNWVVE1aVFRZUNtTFpYR3Qranl4eGNkaGdtT211MENvVTFGRnB5NHRNCmxpcjg2TTVwMzZMNHBtRVgydW5TOS9sUVZEUWNqN3k2ckt3cHY1VkJKNmxXdm1lK3VuaSs2VUtoMGVmbnpDcThvRitUa0JtV0xzbUUKTEhuTU5BSWIzWjVTM2tFOXRSdGNKZiszRUtjSGhBWkVieXV4NnVDZVVhSU9nL2o2Y0s4Nnl3Nk5ORVZqdUQ0RWV6VExVZnhUT09nTwp1M0ExQ3JDVlcxbUdheEt5QmNuMjl0Y2pIMldMQ3VZYkVQTTBxYUpHa2lIR09ZZll0K1Q2MTdJRmtJbFA4NGhleUVhWWdKdUlzU000CnFVcUF6Ujg4RjZjbFFnRVlkWmlIZ2F3MFlRdEM3a05DNXZYMWNibjdveWlhKzhUbzlHNm5YdFFiR0Z4VVo5SWlrQlcxS21OMzgyWGIKc1BJd2ZOWjQxOG1BakpzSzkrWVdCMkJ5UzNrKzljNUUyeUNZa1JaWjRKa3NsLzByOUNZajYvZUpwQ2RHZjkvSm1FcXI1NSt6QjIrdQpGamtiMlZRMG95UXNJbk5WZzFRR1VmVUJkWE1pdWJLUjhJRUFGYUlJOXFUcHRPSXAzem9BT1U5Y3hRUEZSNHJoc3JUTlFrdm5uSlNzCmtqYTdKY2NYWmVZNXl0SEprb2pqVUtZUW5CZnJMOVZDVVNiODUyL214Zzd4V2ZHd0IyTk9PVjZHcEpheU9vdjZpSU9nbnBTL3JaZnEKMGR3Rjg0Y0J0SWR5Vkk0VlUzYmJYNEJWM2pZclAwN1A0d3VFdnBuUFRUblNndlVkb21qQ1EwREpyOUc3S3djTHp2QzlZSUd4c1RMSQpXckIzMjdpYWFwb1paRGU0YTZRZjNFOGdvOHdJaURoaHZzM0pSSWVUUXl4S3k0bUZLdVR1WHRhVkdnYUtrUmhDdHFkL1ZZNlNabWU0CnE4RExqSTBNUTVXeFhYU1FzZGZpQ1IycGZZUlVDSThhTU1sNWlIemZYUUZib1M2MCtuRzZ0NTh5Z2pTUC9pbHd6SGt5RmdIRld6bFEKS3lVZWhzeE1OMmJUak1lSDNJL21uMnY5NU5RbUhSMThYMVpXRXM3NWJVOFBHV1VKVkVJM2dlNXdUNVdHMEFZRlBoN3Y4ekgwRWltWApEQXhMc1dhYWNYTWt2Q3dZNTNRY1VzbTlockM5YWNrRWdvMEp0T0dqS3k1ckZHemUxQ1hIL0luNFNTSUlwLzhvY0RIVjZRRkQyVHZ4CkNoeS8zOUJmc3U3ekFsZ3FUZVdrTjg4QUdzUTZqSXIrdXNOdS9FRVljNVYwM3ZvNzYwTnlyc00yN0Z5cnFDYi9mWjNrbW5hSUsrQ2sKN2toVi84Z21GWUVhb0JNWmkvdGY2MUZ0cnhRQjhSb2hmdWQvK1hJSk9aVU9YVW9ucS8ybFBvRm5TOXNpU2RzbEM1c3l4dmoveXMrdgpIbXF6KzVUWlVvd09ZdlNadHB2Mlg0bnIxcXN2RHpZMGRHdXNiRWlSMVhCekFwb2gveE9yRUxTdFV3UDR5NmgwbGtWVDhmUm9aVkI1CklsRE9NOEprQzdZc3hLK3ZPeXdQOC9ON21HZDJVc2pWaHFTQXRkNUVSdElGeUl0bXNSd0dVWDk3YytselBNK2EySndWVzBwV2wyNk0KZTV6a3NBYmFUcllKY2tLRFhGVXYvNWlzeXlTMjl6TG11SkNMTkE1TkJwNFV5d1FiNUhQVEdROS9TQmlBRmtqYld0c01wUGZISi9SNgpDQ3FTQ1FiSVpHaUJGN05FbWVnVFBQWXRaQjUyVU1UYlVZTldLUXlEZ1l3RmVWZXVUdTNXTWtMVFB4THR0azdZUmhJdHR0cVNEZm9KCjRJeG54aitmb1d1QSs3STRHbnNFL3NFZEhzR3NiOXNhZEZXVkh2NUdJeEdFWWNNWUoveStpU2ltbE1kRnljbW5iSm5oMk1IL1FFTGYKYjkvNXl0dFJQZEVPMWQ5VzM5eDJDTFBIaEJlVzNQVXg0bldtYWgwWjhFTTZyY0twUFZQNzFVTDhDbDkzaEIxaUc4NFh1blRKREg3bwo4NUNxdkxPOVVpZ2M4aW9jRmlSdHB3QTZ5VXZ6MzJyRnZhNnFCNlZvd3ltMm4vVnY1bGVHUDNpVnMwb3lhV2VhWlA0NjV0cndBSlRlCnVsSlNsMjRqaElXUjVFUnhGZUtGVGc3OTJ1NFFRWXdoemVndmpsNCtqYmgwQ2NySnRRLytKYnRyTDl4NUZwYTlEQ1dQU09YQmhOckUKOXVJZzFUbVhJNmh2YWkrQTZINXhLY2h0eFYrNm9NeTYwUEVNNmk4TndKMDNwb0VINFlzSkpXeFZKV2N1K2ZvdDJZbGpxbW4xNnhObgpiMVNyNXZYaVJkM1RXeGNpL0pyZERDL01jejB1OWJTVSs0VVdqT0pia3h2YjFuTk5KNHhFSGVGQnM1Zk1ZRFF2VzdxQy95aXBHVGN4ClRrNjRnb05CV2J4UVdNYVNTcm92dlp2OWRTQ0l6U1lHSm1yeUhZQmFRK3VQOExtaVV2VXlFRFdJOHhLdVB2aGNRd2F3QUpFdXNWVGcKL1RqdC9pZStiTU1kNGFUSjMzbjMzdGJnU0d3T2daV29CNDM5NWdjalhkeGVoNk1SRTlJTFhaeDM4QXFiMmxXZlFqWEcvQVgyV2hzMApNT01mNHJPS1pQMXNLbVR6QTRtSkVjcE15WDkyODdKRjkxODZ6ZWRMWGJlUUd3dzVuM0gxMHRJYW5qUEpPd1h0cU10bWkwbWZHSWQzClRycER3Nkg5UWZocFY5OTlLV3BncStKNkhPWm10TjJxNkdqOW5GRUhMdGZsR3ZVdVd2OGw2anM4MHJPUkt3WGVidzF1TGNrdGM1czYKckRKMnZJMXhXV3gyNHlUSFd1MEdYUzZCUEwweVF0bmVyQWkyZ1VFeEVrZ3Q0ZGRBY3JHYkxPNEc3Nk9UM1NDOTdjTXo5c1EzQUZSWApWZmtxQ3ZLM0pxMFRNQVhrd25UWm4yQllVOEJGbEVoTnE0VFZTQVB4L1pIVTdUZmcxVHd1bW1uSUpwOWkwZmV2cFA1NnNHOGJxbzV0CnBlOVROc0ZYeWFuSjE0M01Zd0xvWThBUXJ0a0tNY1ZYOU5PRzVnTnkxQTNyVmRNbW9uaGx5Qy9PcGpqdE5DYkJQQWhEWTlkNDNJRFUKbjNNUGlOV1JsNVlNcWNZZ21lM0VodEh3OERDOEJad080OG5tZHJ6RHZxRzBvSktmK3ZjTW55TTk3Rm9pZlIwcDZJMmtWVGhpZzVueQpWNEkvTlF3Yk9UUzFyUEJkNklaZEN5R05lWjBwY2lDV0tUV0hHWVdhbFRyK0RkK0gxRW51cHBTeld6S3pMTGt5YWRvSnlHbThkRmtqCjhnQ2llS1BWdlk4VEV1bmt4MGI3RlJLN0FYeTBqVWpOdEYvOS9peXljVGNvdlkzMkJjTmphbXVoRWFZWEFPRWRaUWtnaytzOWF2ZXQKTi9nOGU1YkNHK1ZubEJxTy9lNGFLaGFLK0tyRktybGxKZU5aaGFteTJNaHQ0Q3BmVFNIM0ZhcjNBczRwOUZEQW9Jb2U4UDBpZXAzegovbW44SlkxWXRZSUN6RVlOb0NZVFlBMUNNQUgySmIrbVJMMnpxTHNqS2ZIbTczRHI4STF3d0x3blpaY3lWZ1hNU0w4MDRiVXJVUnl0CnhGNEU4UFZiaWxKZkFiOFlKSXlZUkIwPTwveGVuYzpDaXBoZXJWYWx1ZT48L3hlbmM6Q2lwaGVyRGF0YT48L3hlbmM6RW5jcnlwdGVkRGF0YT48L3NhbWwyOkVuY3J5cHRlZEFzc2VydGlvbj48L3NhbWwycDpSZXNwb25zZT4=")
                            .cookies(mapLoginPageCookies3)
                            //many websites redirects the user after login, so follow them
                            .followRedirects(true)
                            .validateTLSCertificates(false)
                            .execute();

                    Document document = loginForm2.parse();
                    System.out.println(document);




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
