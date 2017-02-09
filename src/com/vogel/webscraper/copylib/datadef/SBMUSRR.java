/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.vogel.webscraper.copylib.datadef;

import java.io.*;

public class SBMUSRR extends Object {

public SBMUSRR() {
initAttributes();
}

public SBMUSRR(String s) {
initAttributes();
String aToken="";
int iIndex=0;
StringBuffer rString=new StringBuffer(s);

// extract oid
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setOid(Integer.parseInt(aToken));
} catch (Exception e){}

// extract userid
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setUserid(Integer.parseInt(aToken));
} catch (Exception e){}

// extract source
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setSource(aToken);
} catch (Exception e){}

// extract username
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setUsername(aToken);
} catch (Exception e){}

// extract statelic1
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setStatelic1(aToken);
} catch (Exception e){}

// extract statelic2
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setStatelic2(aToken);
} catch (Exception e){}

// extract licadmisdate1
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLicadmisdate1(aToken);
} catch (Exception e){}

// extract licadmisdate2
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLicadmisdate2(aToken);
} catch (Exception e){}

// extract licnum1
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLicnum1(aToken);
} catch (Exception e){}

// extract licnum2
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLicnum2(aToken);
} catch (Exception e){}

// extract phonenum
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setPhonenum(aToken);
} catch (Exception e){}

// extract faxnum
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setFaxnum(aToken);
} catch (Exception e){}

// extract email
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setEmail(aToken);
} catch (Exception e){}

// extract occupation
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setOccupation(aToken);
} catch (Exception e){}

// extract status
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setStatus(aToken);
} catch (Exception e){}

// extract employer
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setEmployer(aToken);
} catch (Exception e){}

// extract jobtitle
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setJobtitle(aToken);
} catch (Exception e){}

// extract addrline1
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setAddrline1(aToken);
} catch (Exception e){}

// extract county
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setCounty(aToken);
} catch (Exception e){}

// extract city
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setCity(aToken);
} catch (Exception e){}

// extract state
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setState(aToken);
} catch (Exception e){}

// extract postcode
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setPostcode(aToken);
} catch (Exception e){}

// extract website
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setWebsite(aToken);
} catch (Exception e){}

// extract practareas
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setPractareas(aToken);
} catch (Exception e){}

// extract languages
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLanguages(aToken);
} catch (Exception e){}

// extract sections
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setSections(aToken);
} catch (Exception e){}

// extract universities
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setUniversities(aToken);
} catch (Exception e){}

// extract degrees
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setDegrees(aToken);
} catch (Exception e){}

// extract yeargrad
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setYeargrad(aToken);
} catch (Exception e){}

// extract barassocname
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setBarassocname(aToken);
} catch (Exception e){}

// extract barassocdate
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setBarassocdate(aToken);
} catch (Exception e){}

// extract barassocrole
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setBarassocrole(aToken);
} catch (Exception e){}

// extract lat
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLat(aToken);
} catch (Exception e){}

// extract lon
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setLon(aToken);
} catch (Exception e){}

// extract fdate
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setFdate(aToken);
} catch (Exception e){}

}

public void initAttributes() {
oid=0;
userid=0;
source="";
username="";
statelic1="";
statelic2="";
licadmisdate1="0000-00-00";
licadmisdate2="0000-00-00";
licnum1="";
licnum2="";
phonenum="";
faxnum="";
email="";
occupation="";
status="";
employer="";
jobtitle="";
addrline1="";
county="";
city="";
state="";
postcode="";
website="";
practareas="";
languages="";
sections="";
universities="";
degrees="";
yeargrad="";
barassocname="";
barassocdate="";
barassocrole="";
lat="";
lon="";
fdate="0000-00-00";
}

public String serialized() {
StringBuffer sb=new StringBuffer();
sb.append(this.oid);
sb.append("\t");
sb.append(this.userid);
sb.append("\t");
sb.append(this.source);
sb.append("\t");
sb.append(this.username);
sb.append("\t");
sb.append(this.statelic1);
sb.append("\t");
sb.append(this.statelic2);
sb.append("\t");
sb.append(this.licadmisdate1);
sb.append("\t");
sb.append(this.licadmisdate2);
sb.append("\t");
sb.append(this.licnum1);
sb.append("\t");
sb.append(this.licnum2);
sb.append("\t");
sb.append(this.phonenum);
sb.append("\t");
sb.append(this.faxnum);
sb.append("\t");
sb.append(this.email);
sb.append("\t");
sb.append(this.occupation);
sb.append("\t");
sb.append(this.status);
sb.append("\t");
sb.append(this.employer);
sb.append("\t");
sb.append(this.jobtitle);
sb.append("\t");
sb.append(this.addrline1);
sb.append("\t");
sb.append(this.county);
sb.append("\t");
sb.append(this.city);
sb.append("\t");
sb.append(this.state);
sb.append("\t");
sb.append(this.postcode);
sb.append("\t");
sb.append(this.website);
sb.append("\t");
sb.append(this.practareas);
sb.append("\t");
sb.append(this.languages);
sb.append("\t");
sb.append(this.sections);
sb.append("\t");
sb.append(this.universities);
sb.append("\t");
sb.append(this.degrees);
sb.append("\t");
sb.append(this.yeargrad);
sb.append("\t");
sb.append(this.barassocname);
sb.append("\t");
sb.append(this.barassocdate);
sb.append("\t");
sb.append(this.barassocrole);
sb.append("\t");
sb.append(this.lat);
sb.append("\t");
sb.append(this.lon);
sb.append("\t");
sb.append(this.fdate);
sb.append("\t");
return sb.toString();
}

/**
* Holds value of property oid.
*/
private int oid;

/**
* Holds value of property userid.
*/
private int userid;

/**
* Holds value of property source.
*/
private String source;

/**
* Holds value of property username.
*/
private String username;

/**
* Holds value of property statelic1.
*/
private String statelic1;

/**
* Holds value of property statelic2.
*/
private String statelic2;

/**
* Holds value of property licadmisdate1.
*/
private String licadmisdate1;

/**
* Holds value of property licadmisdate2.
*/
private String licadmisdate2;

/**
* Holds value of property licnum1.
*/
private String licnum1;

/**
* Holds value of property licnum2.
*/
private String licnum2;

/**
* Holds value of property phonenum.
*/
private String phonenum;

/**
* Holds value of property faxnum.
*/
private String faxnum;

/**
* Holds value of property email.
*/
private String email;

/**
* Holds value of property occupation.
*/
private String occupation;

/**
* Holds value of property status.
*/
private String status;

/**
* Holds value of property employer.
*/
private String employer;

/**
* Holds value of property jobtitle.
*/
private String jobtitle;

/**
* Holds value of property addrline1.
*/
private String addrline1;

/**
* Holds value of property county.
*/
private String county;

/**
* Holds value of property city.
*/
private String city;

/**
* Holds value of property state.
*/
private String state;

/**
* Holds value of property postcode.
*/
private String postcode;

/**
* Holds value of property website.
*/
private String website;

/**
* Holds value of property practareas.
*/
private String practareas;

/**
* Holds value of property languages.
*/
private String languages;

/**
* Holds value of property sections.
*/
private String sections;

/**
* Holds value of property universities.
*/
private String universities;

/**
* Holds value of property degrees.
*/
private String degrees;

/**
* Holds value of property yeargrad.
*/
private String yeargrad;

/**
* Holds value of property barassocname.
*/
private String barassocname;

/**
* Holds value of property barassocdate.
*/
private String barassocdate;

/**
* Holds value of property barassocrole.
*/
private String barassocrole;

/**
* Holds value of property lat.
*/
private String lat;

/**
* Holds value of property lon.
*/
private String lon;

/**
* Holds value of property fdate.
*/
private String fdate;



/**
* Setter for property oid.
*/
public void setOid(int oid) {
this.oid = oid;
}

/**
* Setter for property userid.
*/
public void setUserid(int userid) {
this.userid = userid;
}

/**
* Setter for property source.
*/
public void setSource(String source) {
this.source = source;
}

/**
* Setter for property username.
*/
public void setUsername(String username) {
this.username = username;
}

/**
* Setter for property statelic1.
*/
public void setStatelic1(String statelic1) {
this.statelic1 = statelic1;
}

/**
* Setter for property statelic2.
*/
public void setStatelic2(String statelic2) {
this.statelic2 = statelic2;
}

/**
* Setter for property licadmisdate1.
*/
public void setLicadmisdate1(String licadmisdate1) {
this.licadmisdate1 = licadmisdate1;
}

/**
* Setter for property licadmisdate2.
*/
public void setLicadmisdate2(String licadmisdate2) {
this.licadmisdate2 = licadmisdate2;
}

/**
* Setter for property licnum1.
*/
public void setLicnum1(String licnum1) {
this.licnum1 = licnum1;
}

/**
* Setter for property licnum2.
*/
public void setLicnum2(String licnum2) {
this.licnum2 = licnum2;
}

/**
* Setter for property phonenum.
*/
public void setPhonenum(String phonenum) {
this.phonenum = phonenum;
}

/**
* Setter for property faxnum.
*/
public void setFaxnum(String faxnum) {
this.faxnum = faxnum;
}

/**
* Setter for property email.
*/
public void setEmail(String email) {
this.email = email;
}

/**
* Setter for property occupation.
*/
public void setOccupation(String occupation) {
this.occupation = occupation;
}

/**
* Setter for property status.
*/
public void setStatus(String status) {
this.status = status;
}

/**
* Setter for property employer.
*/
public void setEmployer(String employer) {
this.employer = employer;
}

/**
* Setter for property jobtitle.
*/
public void setJobtitle(String jobtitle) {
this.jobtitle = jobtitle;
}

/**
* Setter for property addrline1.
*/
public void setAddrline1(String addrline1) {
this.addrline1 = addrline1;
}

/**
* Setter for property county.
*/
public void setCounty(String county) {
this.county = county;
}

/**
* Setter for property city.
*/
public void setCity(String city) {
this.city = city;
}

/**
* Setter for property state.
*/
public void setState(String state) {
this.state = state;
}

/**
* Setter for property postcode.
*/
public void setPostcode(String postcode) {
this.postcode = postcode;
}

/**
* Setter for property website.
*/
public void setWebsite(String website) {
this.website = website;
}

/**
* Setter for property practareas.
*/
public void setPractareas(String practareas) {
this.practareas = practareas;
}

/**
* Setter for property languages.
*/
public void setLanguages(String languages) {
this.languages = languages;
}

/**
* Setter for property sections.
*/
public void setSections(String sections) {
this.sections = sections;
}

/**
* Setter for property universities.
*/
public void setUniversities(String universities) {
this.universities = universities;
}

/**
* Setter for property degrees.
*/
public void setDegrees(String degrees) {
this.degrees = degrees;
}

/**
* Setter for property yeargrad.
*/
public void setYeargrad(String yeargrad) {
this.yeargrad = yeargrad;
}

/**
* Setter for property barassocname.
*/
public void setBarassocname(String barassocname) {
this.barassocname = barassocname;
}

/**
* Setter for property barassocdate.
*/
public void setBarassocdate(String barassocdate) {
this.barassocdate = barassocdate;
}

/**
* Setter for property barassocrole.
*/
public void setBarassocrole(String barassocrole) {
this.barassocrole = barassocrole;
}

/**
* Setter for property lat.
*/
public void setLat(String lat) {
this.lat = lat;
}

/**
* Setter for property lon.
*/
public void setLon(String lon) {
this.lon = lon;
}

/**
* Setter for property fdate.
*/
public void setFdate(String fdate) {
this.fdate = fdate;
}



/**
* Getter for property oid.
*/
public int getOid() {
return this.oid;
}

/**
* Getter for property userid.
*/
public int getUserid() {
return this.userid;
}

/**
* Getter for property source.
*/
public String getSource() {
return this.source;
}

/**
* Getter for property username.
*/
public String getUsername() {
return this.username;
}

/**
* Getter for property statelic1.
*/
public String getStatelic1() {
return this.statelic1;
}

/**
* Getter for property statelic2.
*/
public String getStatelic2() {
return this.statelic2;
}

/**
* Getter for property licadmisdate1.
*/
public String getLicadmisdate1() {
return this.licadmisdate1;
}

/**
* Getter for property licadmisdate2.
*/
public String getLicadmisdate2() {
return this.licadmisdate2;
}

/**
* Getter for property licnum1.
*/
public String getLicnum1() {
return this.licnum1;
}

/**
* Getter for property licnum2.
*/
public String getLicnum2() {
return this.licnum2;
}

/**
* Getter for property phonenum.
*/
public String getPhonenum() {
return this.phonenum;
}

/**
* Getter for property faxnum.
*/
public String getFaxnum() {
return this.faxnum;
}

/**
* Getter for property email.
*/
public String getEmail() {
return this.email;
}

/**
* Getter for property occupation.
*/
public String getOccupation() {
return this.occupation;
}

/**
* Getter for property status.
*/
public String getStatus() {
return this.status;
}

/**
* Getter for property employer.
*/
public String getEmployer() {
return this.employer;
}

/**
* Getter for property jobtitle.
*/
public String getJobtitle() {
return this.jobtitle;
}

/**
* Getter for property addrline1.
*/
public String getAddrline1() {
return this.addrline1;
}

/**
* Getter for property county.
*/
public String getCounty() {
return this.county;
}

/**
* Getter for property city.
*/
public String getCity() {
return this.city;
}

/**
* Getter for property state.
*/
public String getState() {
return this.state;
}

/**
* Getter for property postcode.
*/
public String getPostcode() {
return this.postcode;
}

/**
* Getter for property website.
*/
public String getWebsite() {
return this.website;
}

/**
* Getter for property practareas.
*/
public String getPractareas() {
return this.practareas;
}

/**
* Getter for property languages.
*/
public String getLanguages() {
return this.languages;
}

/**
* Getter for property sections.
*/
public String getSections() {
return this.sections;
}

/**
* Getter for property universities.
*/
public String getUniversities() {
return this.universities;
}

/**
* Getter for property degrees.
*/
public String getDegrees() {
return this.degrees;
}

/**
* Getter for property yeargrad.
*/
public String getYeargrad() {
return this.yeargrad;
}

/**
* Getter for property barassocname.
*/
public String getBarassocname() {
return this.barassocname;
}

/**
* Getter for property barassocdate.
*/
public String getBarassocdate() {
return this.barassocdate;
}

/**
* Getter for property barassocrole.
*/
public String getBarassocrole() {
return this.barassocrole;
}

/**
* Getter for property lat.
*/
public String getLat() {
return this.lat;
}

/**
* Getter for property lon.
*/
public String getLon() {
return this.lon;
}

/**
* Getter for property fdate.
*/
public String getFdate() {
return this.fdate;
}



public String printClass() {
StringBuffer sb=new StringBuffer();
sb.append("oid: ");
sb.append(this.oid);
sb.append("\n");
sb.append("userid: ");
sb.append(this.userid);
sb.append("\n");
sb.append("source: ");
sb.append(this.source);
sb.append("\n");
sb.append("username: ");
sb.append(this.username);
sb.append("\n");
sb.append("statelic1: ");
sb.append(this.statelic1);
sb.append("\n");
sb.append("statelic2: ");
sb.append(this.statelic2);
sb.append("\n");
sb.append("licadmisdate1: ");
sb.append(this.licadmisdate1);
sb.append("\n");
sb.append("licadmisdate2: ");
sb.append(this.licadmisdate2);
sb.append("\n");
sb.append("licnum1: ");
sb.append(this.licnum1);
sb.append("\n");
sb.append("licnum2: ");
sb.append(this.licnum2);
sb.append("\n");
sb.append("phonenum: ");
sb.append(this.phonenum);
sb.append("\n");
sb.append("faxnum: ");
sb.append(this.faxnum);
sb.append("\n");
sb.append("email: ");
sb.append(this.email);
sb.append("\n");
sb.append("occupation: ");
sb.append(this.occupation);
sb.append("\n");
sb.append("status: ");
sb.append(this.status);
sb.append("\n");
sb.append("employer: ");
sb.append(this.employer);
sb.append("\n");
sb.append("jobtitle: ");
sb.append(this.jobtitle);
sb.append("\n");
sb.append("addrline1: ");
sb.append(this.addrline1);
sb.append("\n");
sb.append("county: ");
sb.append(this.county);
sb.append("\n");
sb.append("city: ");
sb.append(this.city);
sb.append("\n");
sb.append("state: ");
sb.append(this.state);
sb.append("\n");
sb.append("postcode: ");
sb.append(this.postcode);
sb.append("\n");
sb.append("website: ");
sb.append(this.website);
sb.append("\n");
sb.append("practareas: ");
sb.append(this.practareas);
sb.append("\n");
sb.append("languages: ");
sb.append(this.languages);
sb.append("\n");
sb.append("sections: ");
sb.append(this.sections);
sb.append("\n");
sb.append("universities: ");
sb.append(this.universities);
sb.append("\n");
sb.append("degrees: ");
sb.append(this.degrees);
sb.append("\n");
sb.append("yeargrad: ");
sb.append(this.yeargrad);
sb.append("\n");
sb.append("barassocname: ");
sb.append(this.barassocname);
sb.append("\n");
sb.append("barassocdate: ");
sb.append(this.barassocdate);
sb.append("\n");
sb.append("barassocrole: ");
sb.append(this.barassocrole);
sb.append("\n");
sb.append("lat: ");
sb.append(this.lat);
sb.append("\n");
sb.append("lon: ");
sb.append(this.lon);
sb.append("\n");
sb.append("fdate: ");
sb.append(this.fdate);
sb.append("\n");
return sb.toString();
}

public static void main(String args[]) {
System.out.println("start.");
SBMUSRR obj1=new SBMUSRR();
obj1.setOid(12);
obj1.setUserid(13);
obj1.setSource("14");
obj1.setUsername("15");
obj1.setStatelic1("16");
obj1.setStatelic2("17");
obj1.setLicadmisdate1("18-00-00");
obj1.setLicadmisdate2("19-00-00");
obj1.setLicnum1("20");
obj1.setLicnum2("21");
obj1.setPhonenum("22");
obj1.setFaxnum("23");
obj1.setEmail("24");
obj1.setOccupation("25");
obj1.setStatus("26");
obj1.setEmployer("27");
obj1.setJobtitle("28");
obj1.setAddrline1("29");
obj1.setCounty("30");
obj1.setCity("31");
obj1.setState("32");
obj1.setPostcode("33");
obj1.setWebsite("34");
obj1.setPractareas("35");
obj1.setLanguages("36");
obj1.setSections("37");
obj1.setUniversities("38");
obj1.setDegrees("39");
obj1.setYeargrad("40");
obj1.setBarassocname("41");
obj1.setBarassocdate("42");
obj1.setBarassocrole("43");
obj1.setLat("44");
obj1.setLon("45");
obj1.setFdate("46-00-00");

try {
System.out.println(".... ready to serialize SBMUSRR object?");
BufferedWriter bw=new BufferedWriter(new FileWriter("/DATA/DEPLOYMENT/TEMP//TEST_SBMUSRR.TXT"));
bw.write(obj1.serialized());
bw.close();
System.out.println("... done!!");

System.out.println(".... ready to build SBMUSRR object from serialization?");
BufferedReader br=new BufferedReader(new FileReader("/DATA/DEPLOYMENT/TEMP//TEST_SBMUSRR.TXT"));
String rString=br.readLine();
br.close();

SBMUSRR obj2=new SBMUSRR(rString);
System.out.println("original value: "+obj1.getOid()+" <--- "+obj2.getOid()+".... are they the same? ==>"+((obj1.getOid()==obj2.getOid())?"YES":"NO"));
System.out.println("original value: "+obj1.getUserid()+" <--- "+obj2.getUserid()+".... are they the same? ==>"+((obj1.getUserid()==obj2.getUserid())?"YES":"NO"));
System.out.println("original value: "+obj1.getSource()+" <--- "+obj2.getSource()+".... are they the same? ==>"+((obj1.getSource().equals(obj2.getSource()))?"YES":"NO"));
System.out.println("original value: "+obj1.getUsername()+" <--- "+obj2.getUsername()+".... are they the same? ==>"+((obj1.getUsername().equals(obj2.getUsername()))?"YES":"NO"));
System.out.println("original value: "+obj1.getStatelic1()+" <--- "+obj2.getStatelic1()+".... are they the same? ==>"+((obj1.getStatelic1().equals(obj2.getStatelic1()))?"YES":"NO"));
System.out.println("original value: "+obj1.getStatelic2()+" <--- "+obj2.getStatelic2()+".... are they the same? ==>"+((obj1.getStatelic2().equals(obj2.getStatelic2()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLicadmisdate1()+" <--- "+obj2.getLicadmisdate1()+".... are they the same? ==>"+((obj1.getLicadmisdate1().equals(obj2.getLicadmisdate1()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLicadmisdate2()+" <--- "+obj2.getLicadmisdate2()+".... are they the same? ==>"+((obj1.getLicadmisdate2().equals(obj2.getLicadmisdate2()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLicnum1()+" <--- "+obj2.getLicnum1()+".... are they the same? ==>"+((obj1.getLicnum1().equals(obj2.getLicnum1()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLicnum2()+" <--- "+obj2.getLicnum2()+".... are they the same? ==>"+((obj1.getLicnum2().equals(obj2.getLicnum2()))?"YES":"NO"));
System.out.println("original value: "+obj1.getPhonenum()+" <--- "+obj2.getPhonenum()+".... are they the same? ==>"+((obj1.getPhonenum().equals(obj2.getPhonenum()))?"YES":"NO"));
System.out.println("original value: "+obj1.getFaxnum()+" <--- "+obj2.getFaxnum()+".... are they the same? ==>"+((obj1.getFaxnum().equals(obj2.getFaxnum()))?"YES":"NO"));
System.out.println("original value: "+obj1.getEmail()+" <--- "+obj2.getEmail()+".... are they the same? ==>"+((obj1.getEmail().equals(obj2.getEmail()))?"YES":"NO"));
System.out.println("original value: "+obj1.getOccupation()+" <--- "+obj2.getOccupation()+".... are they the same? ==>"+((obj1.getOccupation().equals(obj2.getOccupation()))?"YES":"NO"));
System.out.println("original value: "+obj1.getStatus()+" <--- "+obj2.getStatus()+".... are they the same? ==>"+((obj1.getStatus().equals(obj2.getStatus()))?"YES":"NO"));
System.out.println("original value: "+obj1.getEmployer()+" <--- "+obj2.getEmployer()+".... are they the same? ==>"+((obj1.getEmployer().equals(obj2.getEmployer()))?"YES":"NO"));
System.out.println("original value: "+obj1.getJobtitle()+" <--- "+obj2.getJobtitle()+".... are they the same? ==>"+((obj1.getJobtitle().equals(obj2.getJobtitle()))?"YES":"NO"));
System.out.println("original value: "+obj1.getAddrline1()+" <--- "+obj2.getAddrline1()+".... are they the same? ==>"+((obj1.getAddrline1().equals(obj2.getAddrline1()))?"YES":"NO"));
System.out.println("original value: "+obj1.getCounty()+" <--- "+obj2.getCounty()+".... are they the same? ==>"+((obj1.getCounty().equals(obj2.getCounty()))?"YES":"NO"));
System.out.println("original value: "+obj1.getCity()+" <--- "+obj2.getCity()+".... are they the same? ==>"+((obj1.getCity().equals(obj2.getCity()))?"YES":"NO"));
System.out.println("original value: "+obj1.getState()+" <--- "+obj2.getState()+".... are they the same? ==>"+((obj1.getState().equals(obj2.getState()))?"YES":"NO"));
System.out.println("original value: "+obj1.getPostcode()+" <--- "+obj2.getPostcode()+".... are they the same? ==>"+((obj1.getPostcode().equals(obj2.getPostcode()))?"YES":"NO"));
System.out.println("original value: "+obj1.getWebsite()+" <--- "+obj2.getWebsite()+".... are they the same? ==>"+((obj1.getWebsite().equals(obj2.getWebsite()))?"YES":"NO"));
System.out.println("original value: "+obj1.getPractareas()+" <--- "+obj2.getPractareas()+".... are they the same? ==>"+((obj1.getPractareas().equals(obj2.getPractareas()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLanguages()+" <--- "+obj2.getLanguages()+".... are they the same? ==>"+((obj1.getLanguages().equals(obj2.getLanguages()))?"YES":"NO"));
System.out.println("original value: "+obj1.getSections()+" <--- "+obj2.getSections()+".... are they the same? ==>"+((obj1.getSections().equals(obj2.getSections()))?"YES":"NO"));
System.out.println("original value: "+obj1.getUniversities()+" <--- "+obj2.getUniversities()+".... are they the same? ==>"+((obj1.getUniversities().equals(obj2.getUniversities()))?"YES":"NO"));
System.out.println("original value: "+obj1.getDegrees()+" <--- "+obj2.getDegrees()+".... are they the same? ==>"+((obj1.getDegrees().equals(obj2.getDegrees()))?"YES":"NO"));
System.out.println("original value: "+obj1.getYeargrad()+" <--- "+obj2.getYeargrad()+".... are they the same? ==>"+((obj1.getYeargrad().equals(obj2.getYeargrad()))?"YES":"NO"));
System.out.println("original value: "+obj1.getBarassocname()+" <--- "+obj2.getBarassocname()+".... are they the same? ==>"+((obj1.getBarassocname().equals(obj2.getBarassocname()))?"YES":"NO"));
System.out.println("original value: "+obj1.getBarassocdate()+" <--- "+obj2.getBarassocdate()+".... are they the same? ==>"+((obj1.getBarassocdate().equals(obj2.getBarassocdate()))?"YES":"NO"));
System.out.println("original value: "+obj1.getBarassocrole()+" <--- "+obj2.getBarassocrole()+".... are they the same? ==>"+((obj1.getBarassocrole().equals(obj2.getBarassocrole()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLat()+" <--- "+obj2.getLat()+".... are they the same? ==>"+((obj1.getLat().equals(obj2.getLat()))?"YES":"NO"));
System.out.println("original value: "+obj1.getLon()+" <--- "+obj2.getLon()+".... are they the same? ==>"+((obj1.getLon().equals(obj2.getLon()))?"YES":"NO"));
System.out.println("original value: "+obj1.getFdate()+" <--- "+obj2.getFdate()+".... are they the same? ==>"+((obj1.getFdate().equals(obj2.getFdate()))?"YES":"NO"));
} catch (Exception e) {
e.printStackTrace();
}
System.out.println("done.");
}

}
