/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.vogel.record.copylib.datadef;

import java.io.*;

public class CHCKPR extends Object {

public CHCKPR() {
initAttributes();
}

public CHCKPR(String s) {
initAttributes();
String aToken="";
int iIndex=0;
StringBuffer rString=new StringBuffer(s);

// extract process
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setProcess(aToken);
} catch (Exception e){}

// extract file
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setFile(aToken);
} catch (Exception e){}

// extract thread
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setThread(Integer.parseInt(aToken));
} catch (Exception e){}

// extract rinfo
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setRinfo(aToken);
} catch (Exception e){}

// extract uow
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setUow(Integer.parseInt(aToken));
} catch (Exception e){}

// extract sdate
iIndex=rString.indexOf("\t");
if (iIndex==-1) {
aToken=rString.toString();
rString.delete(0,rString.length());
} else {
aToken=rString.substring(0,iIndex);
rString.delete(0,iIndex+1);
}
try {
setSdate(aToken);
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
setStatus(Integer.parseInt(aToken));
} catch (Exception e){}

}

public void initAttributes() {
process="";
file="";
thread=0;
rinfo="";
uow=0;
sdate="0000-00-00 00:00:00";
fdate="0000-00-00 00:00:00";
status=0;
}

public String serialized() {
StringBuffer sb=new StringBuffer();
sb.append(this.process);
sb.append("\t");
sb.append(this.file);
sb.append("\t");
sb.append(this.thread);
sb.append("\t");
sb.append(this.rinfo);
sb.append("\t");
sb.append(this.uow);
sb.append("\t");
sb.append(this.sdate);
sb.append("\t");
sb.append(this.fdate);
sb.append("\t");
sb.append(this.status);
sb.append("\t");
return sb.toString();
}

/**
* Holds value of property process.
*/
private String process;

/**
* Holds value of property file.
*/
private String file;

/**
* Holds value of property thread.
*/
private int thread;

/**
* Holds value of property rinfo.
*/
private String rinfo;

/**
* Holds value of property uow.
*/
private int uow;

/**
* Holds value of property sdate.
*/
private String sdate;

/**
* Holds value of property fdate.
*/
private String fdate;

/**
* Holds value of property status.
*/
private int status;



/**
* Setter for property process.
*/
public void setProcess(String process) {
this.process = process;
}

/**
* Setter for property file.
*/
public void setFile(String file) {
this.file = file;
}

/**
* Setter for property thread.
*/
public void setThread(int thread) {
this.thread = thread;
}

/**
* Setter for property rinfo.
*/
public void setRinfo(String rinfo) {
this.rinfo = rinfo;
}

/**
* Setter for property uow.
*/
public void setUow(int uow) {
this.uow = uow;
}

/**
* Setter for property sdate.
*/
public void setSdate(String sdate) {
this.sdate = sdate;
}

/**
* Setter for property fdate.
*/
public void setFdate(String fdate) {
this.fdate = fdate;
}

/**
* Setter for property status.
*/
public void setStatus(int status) {
this.status = status;
}



/**
* Getter for property process.
*/
public String getProcess() {
return this.process;
}

/**
* Getter for property file.
*/
public String getFile() {
return this.file;
}

/**
* Getter for property thread.
*/
public int getThread() {
return this.thread;
}

/**
* Getter for property rinfo.
*/
public String getRinfo() {
return this.rinfo;
}

/**
* Getter for property uow.
*/
public int getUow() {
return this.uow;
}

/**
* Getter for property sdate.
*/
public String getSdate() {
return this.sdate;
}

/**
* Getter for property fdate.
*/
public String getFdate() {
return this.fdate;
}

/**
* Getter for property status.
*/
public int getStatus() {
return this.status;
}



public String printClass() {
StringBuffer sb=new StringBuffer();
sb.append("process: ");
sb.append(this.process);
sb.append("\n");
sb.append("file: ");
sb.append(this.file);
sb.append("\n");
sb.append("thread: ");
sb.append(this.thread);
sb.append("\n");
sb.append("rinfo: ");
sb.append(this.rinfo);
sb.append("\n");
sb.append("uow: ");
sb.append(this.uow);
sb.append("\n");
sb.append("sdate: ");
sb.append(this.sdate);
sb.append("\n");
sb.append("fdate: ");
sb.append(this.fdate);
sb.append("\n");
sb.append("status: ");
sb.append(this.status);
sb.append("\n");
return sb.toString();
}

public static void main(String args[]) {
System.out.println("start.");
CHCKPR obj1=new CHCKPR();
obj1.setProcess("12");
obj1.setFile("13");
obj1.setThread(14);
obj1.setRinfo("15");
obj1.setUow(16);
obj1.setSdate("17-00-00 00:00:00");
obj1.setFdate("18-00-00 00:00:00");
obj1.setStatus(19);

try {
System.out.println(".... ready to serialize CHCKPR object?");
BufferedWriter bw=new BufferedWriter(new FileWriter("c:/KNOWBLET/DEVELOPMENT/TEMP//TEST_CHCKPR.TXT"));
bw.write(obj1.serialized());
bw.close();
System.out.println("... done!!");

System.out.println(".... ready to build CHCKPR object from serialization?");
BufferedReader br=new BufferedReader(new FileReader("c:/KNOWBLET/DEVELOPMENT/TEMP//TEST_CHCKPR.TXT"));
String rString=br.readLine();
br.close();

CHCKPR obj2=new CHCKPR(rString);
System.out.println("original value: "+obj1.getProcess()+" <--- "+obj2.getProcess()+".... are they the same? ==>"+((obj1.getProcess().equals(obj2.getProcess()))?"YES":"NO"));
System.out.println("original value: "+obj1.getFile()+" <--- "+obj2.getFile()+".... are they the same? ==>"+((obj1.getFile().equals(obj2.getFile()))?"YES":"NO"));
System.out.println("original value: "+obj1.getThread()+" <--- "+obj2.getThread()+".... are they the same? ==>"+((obj1.getThread()==obj2.getThread())?"YES":"NO"));
System.out.println("original value: "+obj1.getRinfo()+" <--- "+obj2.getRinfo()+".... are they the same? ==>"+((obj1.getRinfo().equals(obj2.getRinfo()))?"YES":"NO"));
System.out.println("original value: "+obj1.getUow()+" <--- "+obj2.getUow()+".... are they the same? ==>"+((obj1.getUow()==obj2.getUow())?"YES":"NO"));
System.out.println("original value: "+obj1.getSdate()+" <--- "+obj2.getSdate()+".... are they the same? ==>"+((obj1.getSdate().equals(obj2.getSdate()))?"YES":"NO"));
System.out.println("original value: "+obj1.getFdate()+" <--- "+obj2.getFdate()+".... are they the same? ==>"+((obj1.getFdate().equals(obj2.getFdate()))?"YES":"NO"));
System.out.println("original value: "+obj1.getStatus()+" <--- "+obj2.getStatus()+".... are they the same? ==>"+((obj1.getStatus()==obj2.getStatus())?"YES":"NO"));
} catch (Exception e) {
e.printStackTrace();
}
System.out.println("done.");
}

}
