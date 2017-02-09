/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vogel.webscraper.lawyermi;

import com.vogel.record.util.FILIOFactory;
import com.vogel.record.util.Realm;
import com.vogel.record.util.Task;
import com.vogel.common.util.COMMONdate;
import com.vogel.common.util.COMMONtoolkit;
//import com.vogel.netnav.copylib.datadef.BEERATR;
import com.vogel.webscraper.copylib.datadef.SBMUSRR;
import com.vogel.webscraper.copylib.tableio.SBMUSRRIO;
import com.vogel.webscraper.copylib.tableio.SBMUSRRIOX;
import com.vogel.netnav.util.Resolution;
import java.util.concurrent.ArrayBlockingQueue;
import net.htmlparser.jericho.CharacterReference;
import java.time.LocalDate;

/**
 *
 * @author VOGEL
 */
class SBMUSRFileParseRealm extends Realm {

    public String htmlPage = "";
    public boolean locstatus = true;

    public SBMUSRFileParseRealm() {
        super(0);
        super.bhalt = true;
    }

    public SBMUSRFileParseRealm(int i) {
        super(i);
    }
}

class VUPLOAD extends Object {

    public static synchronized Resolution addUser(SBMUSRR usr) {
        Resolution rRes = new Resolution();
        SBMUSRRIO iObj = new SBMUSRRIO("");
        SBMUSRRIOX sObj = new SBMUSRRIOX("");
        SBMUSRR old = sObj.selectUnique(usr);
        if (old == null) {
            rRes.iStatus = iObj.insert(usr);
        } else {

            rRes.iStatus = FILIOFactory.SKIP_UPDATE;

        }
        return rRes;
    }
}

public class SBMUSRFileParseTask extends Task {

    public static final String TASKNAME = "com.vogel.webscraper.lawyermi.SBMUSRFileParseTask";
    private ArrayBlockingQueue queue;
    private int iWorker = 0;
    private SBMUSRFileParseRealm params = null;
    boolean bCookieReset = false;
    boolean bContinue = false;
    String cookie = "";

    public SBMUSRFileParseTask(ArrayBlockingQueue queue, int iWorker) {
        this.queue = queue;
        this.iWorker = iWorker;
    }

    @Override
    public void run() {
        //Checks to see if the designated header that marks the start of a rating exists
        //final String aToken = "<div id=\"dnn_LeftPaneProfile\">";
        //                  aToken = "<div class=\"indiv_item center\">";

        try {

            while (true) {
                params = (SBMUSRFileParseRealm) queue.take();

                if (params != null) {
                    if (params.bhalt) {
                        break;
                    }
                    String htmlPage = params.htmlPage;
                    if (!htmlPage.isEmpty() || !params.rString.isEmpty()) {

                        //Empty tokens to be used for fetchItem calls
                        String bToken = "";
                        String cToken = "";
                        String dToken = "";
                        String eToken = "";

                        int dIndex = 0;

                        int aIndex = 0;

                        //Variables to store the fetched values, to be applied to the table
                        String rUserID = "";
                        String rSource = "";
                        String rUserName = "";                        
                        String rStateLic1 = "";
                        String rStateLic2 = "";
                        String rLicAdmisDate1 = "";
                        String rLicAdmisDate2 = "";
                        String rPhoneNum = "";
                        //String rCellNum = "";
                        String rFaxNum = "";
                        String rEmail = "";
                        String rOccupation = "";
                        String rStatus = "";
                        String rEmployer = "";
                        String rJobTitle = "";
                        String rAddrLine1 = "";
                        String rCity = "";
                        String rCounty = "";
                        String rState = "";
                        String rPostCode = "";
                        String rWebsite = "";
                        String rLicNum1 = "";
                        String rLicNum2 = "";
                        String rPractAreas = "";
                        String rLanguages = "";
                        String rSections = "";
                        String rUniversity = "";
                        String rDegree = "";
                        String rYearGrad = "";
                        String rBarAssocName = "";
                        String rBarAssocDate = "";
                        String rBarAssocRole = "";
                        String rFdate = "";
                        String rLat = "";
                        String rLon = "";

                        String sysDate = "";

                        if (params.locstatus) {
                            /**aIndex = htmlPage.indexOf(aToken);
                            if (aIndex > -1) {
                                //Cuts out some of the extra html code, so as to isolate the checkin-relevant text
                                htmlPage = htmlPage.substring(aIndex);
                            } else {
                                //Why did we pull in a page that, potentially, doesn't have a checkin?
                                System.out.println("Page Missing Distinct Rating Header: " + params.file + "aIndex:" + aIndex);
                                params.filio.TURF("03 PAGE MISSING RATING HEADER");
                                //System.out.println(htmlPage);
                                //System.exit(0);
                            }**/
                            if (!htmlPage.isEmpty()) {

                            //Test HtmlPage
                                //System.out.println(htmlPage);
                                //Begin Fetches of each table piece
                                //Fetch User ID. User ID is a unique ID number, per source, that is attached to each User
                                bToken = "/";
                                cToken = "_";
                                rUserID = fetchItem(params.file, bToken, cToken);
                                //System.out.println("User ID: " + rUserID);
                            
                                rSource = "SBM";
                            //Fetch User Name. User Name designates the name of the user in the entry.
                                //try {
                                bToken = "<meta itemprop=\"name\" content=\"";
                                cToken = "\" />";
                                rUserName = fetchItem(htmlPage, bToken, cToken);
                                if (rUserName.isEmpty()){
                                    bToken = "\"http://schema.org/ContactPoint\"><meta itemprop=\"Name\" content=\"";
                                    cToken = "\" />";
                                    rUserName = fetchItem(htmlPage, bToken, cToken);
                                    if (rUserName.isEmpty()){
                                        bToken = "<span itemprop=\"name\" class=\"thumb-info-inner\">";
                                        cToken = "</span>";
                                        rUserName = fetchItem(htmlPage, bToken, cToken);
                                    }
                                }
                                //System.out.println("User Name: " + rUserName);
                            //} catch (Exception e) {
                                //params.filio.TURF("13 User Name ISSUE" + params.file);
                                //}
                                
                                //Fetch User Type. User Type is the type of the venue, such as a Micro Brewery
                                bToken = "State Licensed</strong></li>";
                                cToken = "</li>";
                                String License1 = fetchItem(htmlPage, bToken, cToken);
                                
                                if (!License1.isEmpty()){
                                    bToken = "State of Admission: ";
                                    cToken = "<br />";
                                    rStateLic1 = fetchItem(License1, bToken, cToken);
                                    //System.out.println("State License: " + StateLic1);
                                    
                                    bToken = "#: ";
                                    cToken = "<br />";
                                    rLicNum1 = fetchItem(License1, bToken, cToken);
                                    
                                    bToken = "Date of Admission: ";
                                    cToken = "<br />";
                                    rLicAdmisDate1 = fetchItem(License1, bToken, cToken).replaceAll("/", "-");
                                    if (rLicAdmisDate1.length()==4){
                                        rLicAdmisDate1 = "01-01-"+rLicAdmisDate1;
                                    }
                                    
                                    bToken = "Additional State Licensed</strong></li>";
                                    cToken = "</li>";
                                    String License2 = fetchItem(htmlPage, bToken, cToken);
                                    if (!License2.isEmpty()){
                                        bToken = "State of Admission: ";
                                        cToken = "<br />";
                                        rStateLic2 = fetchItem(License2, bToken, cToken);
                                        //System.out.println("Additional State License: " + StateLic2);
                                        
                                        bToken = "#: ";
                                        cToken = "<br />";
                                        rLicNum2 = fetchItem(License2, bToken, cToken);

                                        bToken = "Date of Admission: ";
                                        cToken = "<br />";
                                        rLicAdmisDate2 = fetchItem(License2, bToken, cToken).replaceAll("/", "-");
                                        if (rLicAdmisDate2.length()==4){
                                            rLicAdmisDate2 = "01-01-"+rLicAdmisDate2;
                                        }
                                    }
                                }
                                
                                bToken = "href=\"tel:";
                                cToken = "\">";
                                rPhoneNum = COMMONtoolkit.getInstance().getDigits(fetchItem(htmlPage, bToken, cToken));
                                if (rPhoneNum.isEmpty()){
                                    bToken = "<meta itemprop=\"telephone\" content=\"";
                                    cToken = "\" />";
                                }
                                
                                
                                bToken = "<meta itemprop=\"faxNumber\" content=\"";
                                cToken = "\" />";
                                rFaxNum = COMMONtoolkit.getInstance().getDigits(fetchItem(htmlPage, bToken, cToken));
                                
                                bToken = "var address = \"";
                                cToken = "\";";
                                rEmail = fetchItem(htmlPage, bToken, cToken);
                                
                                
                                bToken = "<span class=\"thumb-info-type\"><!-- SBM -->";
                                cToken = "</span>";
                                rOccupation = fetchItem(htmlPage, bToken, cToken).replaceAll("( Active)|( Voluntary Inactive)|( Emeritus)","");
                                if (rOccupation.isEmpty()){
                                    bToken = "<meta itemprop=\"description\" content=\"";
                                    cToken = "\" />";
                                    rOccupation = fetchItem(htmlPage, bToken, cToken);
                                }
                                
                                
                                bToken = "<div class=\"alert alert-success\">";
                                cToken = "</div>";
                                rStatus = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<meta itemprop=\"legalName\" content=\"";
                                cToken = "\" />";
                                rEmployer = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<meta itemprop=\"streetAddress\" content=\"";
                                cToken = "\" />";
                                try{
                                    rAddrLine1 = fetchItem(htmlPage, bToken, cToken);
                                }catch(Exception e){
                                    System.out.println(e+","+rUserID);
                                    System.exit(0);
                                }
                                bToken = "<meta itemprop=\"addressLocality\" content=\"";
                                cToken = "\" />";
                                rCity = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<li class=\"list-group-item\">County: ";
                                cToken = "</li>";
                                rCounty = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<meta itemprop=\"addressRegion\" content=\"";
                                cToken = "\" />";
                                rState = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<meta itemprop=\"postalCode\" content=\"";
                                cToken = "\" />";
                                rPostCode = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "<li class=\"list-group-item web\"><a href=\"";
                                cToken = "\">";
                                rWebsite =  fetchItem(htmlPage, bToken, cToken).replaceAll("http://", "");
                                
                                bToken = "Practice Area</h3></div><div class=\"panel-body profile-pa\"><span class=\"label label-default\" title=\"\">";
                                cToken = "</span></div>";
                                String PracticeAreas = fetchItem(htmlPage, bToken, cToken);
                                if (!PracticeAreas.isEmpty()){
                                    dToken = "</span><span class=\"label label-default\" title=\"\">";
                                    rPractAreas = PracticeAreas.replaceAll(dToken,",");
                                }
                                
                                bToken = "Languages</h3></div><div class=\"panel-body\"><div itemprop=\"description\">";
                                cToken = "</div>";
                                rLanguages = fetchItem(htmlPage, bToken, cToken);
                                
                                bToken = "Sections</h3></div><div class=\"panel-body\"><ul><li itemprop=\"affiliation\" itemscope itemtype=\"http://schema.org/Organization\"><span itemprop=\"name\">";
                                cToken = "</span></li></ul>\n" + "</div>";
                                dToken = "</span></li><li itemprop=\"affiliation\" itemscope itemtype=\"http://schema.org/Organization\"><span itemprop=\"name\">";
                                rSections = fetchItem(htmlPage, bToken, cToken).replaceAll(dToken,",");
                                
                                bToken = "Education</h3></div><div class=\"panel-body\"><div itemprop=\"alumniOf\" itemscope itemtype=\"http://schema.org/EducationalOrganization\" style=\"margin-bottom:10px\">\n" +"<div class=\"block\" itemprop=\"name\">\n" +"<strong>";
                                cToken = "</div><!-- End_Module_3292 --></div>";
                                dToken = "<div itemprop=\"alumniOf\" itemscope itemtype=\"http://schema.org/EducationalOrganization\" style=\"margin-bottom:10px\">\n"+"<div class=\"block\" itemprop=\"name\">\n" + "<strong>";
                                eToken = "<div class=\"block\" itemprop=\"description\">";
                                try{
                                    String Education = fetchItem(htmlPage, bToken, cToken);
                                }catch(Exception e){
                                    System.out.println(e + ","+ rUserID);
                                    System.exit(0);
                                }
                                String Education = fetchItem(htmlPage, bToken, cToken);
                                //System.out.println(rUserID + "," +Education);
                                if (!fetchItem(htmlPage, bToken, cToken).isEmpty()){
                                    
                                    String[] Universities = fetchItem(htmlPage, bToken, cToken).split(dToken);
                                    //System.out.println(Universities[0]);
                                    if (Universities.length>0){
                                        for(String Univ:Universities){
                                            String[] Uvalues = Univ.split(eToken);
                                            //System.out.println(Uvalues[0]);
                                            //System.out.println(Uvalues[1]);
                                            //System.out.println(Uvalues[2]);
                                            if (Uvalues.length>2){
                                                rUniversity = rUniversity + fetchItem(Uvalues[0],"","</strong>") + ",";
                                                rYearGrad = rYearGrad + COMMONtoolkit.getInstance().getDigits(fetchItem(Uvalues[1],"Year Graduated: ","</div>")) + ",";
                                                rDegree = rDegree + fetchItem(Uvalues[2],"","</div>") +",";
                                            }
                                        }
                                        //System.out.println(rUniversity);
                                        rUniversity = rUniversity.substring(0,rUniversity.length() -1);
                                        rYearGrad = rYearGrad.substring(0,rYearGrad.length()-1);
                                        rDegree = rDegree.substring(0,rDegree.length()-1);
                                    }
                                }
                                
                                bToken = "Bar Associations</h3></div><div class=\"panel-body\"><div class=\"list-item\" itemprop=\"affiliation\" itemscope itemtype=\"http://schema.org/Organization\" style=\"margin-bottom:10px\">\n" + "<div class=\"block\" itemprop=\"name\"><strong>";
                                cToken = "</div><!-- End_Module_3294 --></div>";
                                dToken = "</div><div class=\"list-item\" itemprop=\"affiliation\" itemscope itemtype=\"http://schema.org/Organization\" style=\"margin-bottom:10px\">\n" + "<div class=\"block\" itemprop=\"name\"><strong>";
                                eToken = "<div class=\"block\" itemprop=\"description\">";
                                String barAssocs = fetchItem(htmlPage, bToken, cToken);
                                //System.out.println(barAssocs);
                                if(!barAssocs.isEmpty()){
                                    String[] barAssoclist = barAssocs.split(dToken);
                                    //System.out.println(barAssoclist[0]);
                                    if (barAssoclist.length>0){
                                        for (String barAs : barAssoclist){
                                            String[] BAvalues = barAs.split(eToken);
                                            //System.out.println(BAvalues[0]+","+BAvalues[1]+","+BAvalues[2]);
                                            if(BAvalues.length>2){
                                                rBarAssocName = rBarAssocName + fetchItem(BAvalues[0],"","</strong>")+",";
                                                rBarAssocDate = rBarAssocDate + COMMONtoolkit.getInstance().getDigits(fetchItem(BAvalues[1],"From:","</div>"))+",";
                                                rBarAssocRole = rBarAssocRole + fetchItem(BAvalues[2],"","</div>")+ ",";
                                            }
                                        }
                                        rBarAssocName = rBarAssocName.substring(0,rBarAssocName.length()-1);
                                        rBarAssocDate = rBarAssocDate.substring(0,rBarAssocDate.length()-1);
                                        rBarAssocRole = rBarAssocRole.substring(0,rBarAssocRole.length()-1);
                                        //System.out.println(rBarAssocName);
                                        //System.out.println(rBarAssocDate);
                                        //System.out.println(rBarAssocRole);
                                    }
                                }
                                
                                bToken = "<meta itemprop=\"geo\" content=\"";
                                cToken = "\" />";
                                String coord = fetchItem(htmlPage, bToken, cToken);
                                if(!coord.isEmpty()){
                                    rLat = coord.substring(0, coord.indexOf(","));
                                    rLon = coord.substring(coord.indexOf(",") + 1);
                                }
                                
                                rFdate = COMMONdate.getInstance().getToday().toString();
                                
                                //Set Table values to values collected
                                SBMUSRR lawyermi = new SBMUSRR();
                                //System.out.println(rUserID);
                                try{
                                    lawyermi.setUserid(Integer.parseInt(rUserID));
                                }catch(Exception e){
                                    System.out.println(rUserID + ","+e+"," + params.file);
                                    System.exit(0);
                                }
                                lawyermi.setSource(rSource);
                                lawyermi.setUsername(rUserName);                        
                                lawyermi.setStatelic1(rStateLic1);
                                lawyermi.setStatelic2(rStateLic2);
                                lawyermi.setLicadmisdate1(rLicAdmisDate1);
                                lawyermi.setLicadmisdate2(rLicAdmisDate2);
                                lawyermi.setPhonenum(rPhoneNum);
                                lawyermi.setFaxnum(rFaxNum);
                                lawyermi.setEmail(rEmail);
                                lawyermi.setOccupation(rOccupation);
                                lawyermi.setStatus(rStatus);
                                lawyermi.setEmployer(rEmployer);
                                lawyermi.setJobtitle(rJobTitle);
                                lawyermi.setAddrline1(rAddrLine1);
                                lawyermi.setCity(rCity);
                                lawyermi.setCounty(rCounty);
                                lawyermi.setState(rState);
                                lawyermi.setPostcode(rPostCode);
                                lawyermi.setWebsite(rWebsite);
                                lawyermi.setLicnum1(rLicNum1);
                                lawyermi.setLicnum2(rLicNum2);
                                lawyermi.setPractareas(rPractAreas);
                                lawyermi.setLanguages(rLanguages);
                                lawyermi.setSections(rSections);
                                lawyermi.setUniversities(rUniversity);
                                lawyermi.setDegrees(rDegree);
                                lawyermi.setYeargrad(rYearGrad);
                                lawyermi.setBarassocname(rBarAssocName);
                                lawyermi.setBarassocdate(rBarAssocDate);
                                lawyermi.setBarassocrole(rBarAssocRole);
                                lawyermi.setLat(rLat);
                                lawyermi.setLon(rLon);
                                lawyermi.setFdate(rFdate);
                                

                                params.output.put(params.iSeq, lawyermi.serialized() + "\n");

                            }
                        } else {

                            SBMUSRR lawyermi = new SBMUSRR(params.rString);

                            Resolution rRes = VUPLOAD.addUser(lawyermi);
                            params.filio.TURFDBUPDATE(rRes.iStatus, "SBMUSR");
                        }
                    } else {
                        params.filio.TURF("02 EMPTY  PAGE SKIPPED");
                    }
                    iLastCommittedUoW = params.iSeq;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchItem(String htmlPage, String aToken, String bToken) {
        String rString = "";
        int iIndex = htmlPage.indexOf(aToken);
        if (iIndex > -1) {
            rString = htmlPage.substring(iIndex + aToken.length(), htmlPage.indexOf(bToken, iIndex + aToken.length()));
        }
        return CharacterReference.decodeCollapseWhiteSpace(COMMONtoolkit.getInstance().removeDoubleSpacesTrim(rString));
    }
}
