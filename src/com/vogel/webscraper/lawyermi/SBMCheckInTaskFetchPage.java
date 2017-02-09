
package com.vogel.webscraper.lawyermi;

import com.vogel.common.util.WebpageGrabber;
import java.util.Hashtable;

/**
 *
 * @author VOGEL
 */
public class SBMCheckInTaskFetchPage implements Runnable {

    private int iPage = 0;
    private String htmlPage = "";
    public boolean bTimedOut = true;

    public SBMCheckInTaskFetchPage(int iPage) {
        this.iPage = iPage;
    }

    public String getPage() {
        return htmlPage;
    }

    public void run() {
        try {
            WebpageGrabber grabber = new WebpageGrabber();

            Hashtable headers = grabber.makeHeaders("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n"
                    + "Accept-Encoding: gzip, deflate, sdch\n"
                    + "Accept-Language: en-US,en;q=0.8\n"
                    + "Connection: keep-alive\n"
                    + "Cookie: .ASPXANONYMOUS=I5x0cOUc0QEkAAAAM2Q1Zjc1MDEtZDE4Zi00NTc5LTg5YzYtMTE3YTVmZmM0YzRh0; ASP.NET_SessionId=nebfuw3tdvq3wnsulzpemjdn; RequestVerificationToken=t4uKdI1N_-AjDnVXpK8Twsfw7np6Ns0kvcOEaVcxovnj2S4SKJeNN1J9U8j9nnv6OU6PJd_rFwyGaj_VEuz1T_dynik8Lp4jkQ4q7Z9zqNbdZxs5GerM9Uj_-pQ1; visid_incap_374624=uIvPPzJTS2OqQnYyShWRrAhX6FUAAAAAQUIPAAAAAAAkrK56ud1HkietIh31Ddk/; incap_ses_261_374624=acG7AIOPHG6Sq1iyvkKfA3Gj8VUAAAAA84HO+L8NZBRhovfGBUqv9A==; gat=1; ga=GA1.2.1159196228.1441289994; linkedin_oauth_75ckz69jrb4t16_crc=null; __zlcmid=WXevTFqoK6iGnd; dnn_IsMobile=False; language=en-US\n"
                    + "Host: www.zeekbeek.com\n"
                    + "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
            try {
                String url = "http://www.zeekbeek.com/Activity-Feed/userId/" + iPage;
                htmlPage = grabber.postGrab(url, headers,"");
                //System.out.println(htmlPage);
                //System.exit(0);
//                if (htmlPage.contains("<META NAME=\"robots\"")){
//                    url = grabber.redirectURL;
//                    htmlPage = grabber.postGrab(url,headers,"");
//                    System.out.println("Hit the linked in Page");
//                }
 //               if (htmlPage.contains("<META NAME=\"robots\"")){
 //                   url = grabber.redirectURL;
 //                   htmlPage = grabber.postGrab(url,headers,"");
 //                   System.out.println("Hit the linked in Page");
 //               }
                if (grabber.TheResponseCode == 302 ||  grabber.TheResponseCode== 301) {
                    htmlPage = "";
                    if(grabber.TheResponseCode == 301){
                        System.out.println(url);
                    }
                }
                
                bTimedOut = false;
            } catch (Exception e) {
                e.printStackTrace();
                htmlPage = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            htmlPage = "";
        }
    }
}
