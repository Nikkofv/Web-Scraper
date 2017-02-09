/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */
package com.vogel.common.util;

import java.util.*;
import java.text.*;

/**
 *
 * @author VOGEL
 * @version
 */
public class COMMONdate extends java.lang.Object {

    private static COMMONdate myInstance = null;
    private static String RunDay = "";
    private static final String[] days3 = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String[] days = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private static final String[] months3 = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static synchronized COMMONdate getInstance() {
        if (myInstance == null) {
            myInstance = new COMMONdate();
        }
        return myInstance;
    }

    /**
     * Creates new Datum
     */
    public COMMONdate() {
        // set RunDay to default ToDay
        RunDay = getToday();
    }

    public String getMonthFor(int iMonth) {
        if (iMonth >= 1 && iMonth <= 12) {
            return months[iMonth - 1];
        } else {
            return "";
        }
    }

    public int getThisYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR);
    }

    public String getToday() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String Today = Integer.toString(cal.get(cal.YEAR)) + df.format(new Integer(cal.get(cal.MONTH) + 1)) + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return Today;
    }

    public String getTodayDashed() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String Today = Integer.toString(cal.get(cal.YEAR)) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return Today;
    }

    public String getDateTime() {
        return getTodayDashed() + " " + getTimeOfDay();
    }

    public String getTodayPlus(int dagen) {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        cal.add(cal.DAY_OF_MONTH, dagen);
        String Today = Integer.toString(cal.get(cal.YEAR)) + df.format(new Integer(cal.get(cal.MONTH) + 1)) + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return Today;
    }

    public String getTodayPlusDashed(int dagen) {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        cal.add(cal.DAY_OF_MONTH, dagen);
        String Today = Integer.toString(cal.get(cal.YEAR)) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return Today;
    }

    public String getDatePlusDashed(String date, int days) {
        Calendar cal = getDateObjectFor(date);
        DecimalFormat df = new DecimalFormat("00");
        cal.add(cal.DAY_OF_MONTH, days);
        String Today = Integer.toString(cal.get(cal.YEAR)) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return Today;
    }

    Date addMinutesToDate(Date date, int minutes) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        calendarDate.add(Calendar.MINUTE, minutes);
        return calendarDate.getTime();
    }

    public String addMinutesToDateTime(String datetime, int minutes) {
        String Today = "0000-00-00 00:00:00";
        try {
            Calendar cal = getDateObjectFor(datetime);
            cal.add(Calendar.MINUTE, minutes);
            DecimalFormat df2 = new DecimalFormat("00");
            Today = Integer.toString(cal.get(cal.YEAR)) + '-' + df2.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df2.format(new Integer(cal.get(cal.DAY_OF_MONTH)))
                    + ' ' + df2.format(new Integer(cal.get(cal.HOUR_OF_DAY))) + ':' + df2.format(new Integer(cal.get(cal.MINUTE))) + ':' + df2.format(new Integer(cal.get(cal.SECOND)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Today;
    }
    
     public String addMinutesToDateTime(String datetime, int hours,int minutes,int seconds) {
        String Today = "0000-00-00 00:00:00";
        try {
            Calendar cal = getDateObjectFor(datetime);
            cal.add(Calendar.SECOND, seconds);
            cal.add(Calendar.MINUTE, minutes);
            cal.add(Calendar.HOUR, hours);
            DecimalFormat df2 = new DecimalFormat("00");
            Today = Integer.toString(cal.get(cal.YEAR)) + '-' + df2.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df2.format(new Integer(cal.get(cal.DAY_OF_MONTH)))
                    + ' ' + df2.format(new Integer(cal.get(cal.HOUR_OF_DAY))) + ':' + df2.format(new Integer(cal.get(cal.MINUTE))) + ':' + df2.format(new Integer(cal.get(cal.SECOND)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Today;
    }

    public String adjustDatum(String datum, int iNum) {
        // adjust date
        DecimalFormat df = new DecimalFormat("00");
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(cal.YEAR, Integer.parseInt(datum.substring(0, 4)));
            cal.set(cal.MONTH, Integer.parseInt(datum.substring(5, 7)) - 1);
            cal.set(cal.DAY_OF_MONTH, Integer.parseInt(datum.substring(8, 10)));
            cal.add(cal.DAY_OF_MONTH, iNum);
        } catch (Exception e) {
        }

        datum = Integer.toString(cal.get(cal.YEAR)) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
        return datum;
    }

    public String getDatumAddedWith(String datum, int months) {

        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat df2 = new DecimalFormat("0000");
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(cal.YEAR, Integer.parseInt(datum.substring(0, 4)));
            cal.set(cal.MONTH, Integer.parseInt(datum.substring(5, 7)) - 1);
            cal.set(cal.DAY_OF_MONTH, Integer.parseInt(datum.substring(8, 10)));
            cal.add(cal.MONTH, months);
        } catch (Exception e) {
        }
        return ((df2.format(new Integer(cal.get(cal.YEAR)))) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH))));
    }

    public String getYesterday(String datum) {

        DecimalFormat df = new DecimalFormat("00");
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(cal.YEAR, Integer.parseInt(datum.substring(0, 4)));
            cal.set(cal.MONTH, Integer.parseInt(datum.substring(5, 7)) - 1);
            cal.set(cal.DAY_OF_MONTH, Integer.parseInt(datum.substring(8, 10)));
            cal.add(cal.DAY_OF_MONTH, -1);
        } catch (Exception e) {
        }
        return (Integer.toString(cal.get(cal.YEAR)) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH))));
    }

    public String getTimeOfDay() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String TimeOfDay = df.format(new Integer(cal.get(cal.HOUR_OF_DAY))) + ':' + df.format(new Integer(cal.get(cal.MINUTE))) + ':' + df.format(new Integer(cal.get(cal.SECOND)));
        return TimeOfDay;
    }

    public String getTimeDayForDisplay() {

        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        int day = cal.get(cal.DAY_OF_WEEK);
        int month = cal.get(cal.MONTH);

        String Today = days[day] + ", " + df.format(new Integer(cal.get(cal.DAY_OF_MONTH))) + ' ' + months[month] + ' ' + Integer.toString(cal.get(cal.YEAR));
        return Today + ", " + getTimeOfDay();

    }

    public String getToDayForDisplay() {

        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        int day = cal.get(cal.DAY_OF_WEEK);
        int month = cal.get(cal.MONTH);
        int year = cal.get(cal.YEAR) / 100;

        String Today = days3[day] + " " + df.format(new Integer(cal.get(cal.DAY_OF_MONTH))) + ' ' + months3[month] + ' ' + Integer.toString(year);
        return Today;
    }

    public String formatDateForDisplay(String date) {
        Calendar cal = getDateObjectFor(date);
        DecimalFormat df = new DecimalFormat("00");
        int day = cal.get(cal.DAY_OF_WEEK);
        int month = cal.get(cal.MONTH);
        int year = cal.get(cal.YEAR) / 100;

        String newDate = days3[day] + " " + df.format(new Integer(cal.get(cal.DAY_OF_MONTH))) + ' ' + months3[month] + ' ' + Integer.toString(year);
        return newDate;
    }

    public String getDayName() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(cal.DAY_OF_WEEK);
        return days[day];
    }

    public String getDayname(String date) {
        Calendar cal = getDateObjectFor(date);
        int day = cal.get(cal.DAY_OF_WEEK);
        return days[day];
    }
    
    public int getDayNumber(String date) {
        Calendar cal = getDateObjectFor(date);
        int day = cal.get(cal.DAY_OF_WEEK);
        return day;
    }

    public int getTheWeekFrom(String date) {
        Calendar cal = getDateObjectFor(date);
        return cal.get(cal.WEEK_OF_YEAR);
    }

    public int getTheYear(String date) {
        Calendar cal = getDateObjectFor(date);
        return cal.get(cal.YEAR);
    }

    public String getDateForDisplay() {

        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        int day = cal.get(cal.DAY_OF_WEEK);
        int month = cal.get(cal.MONTH);

        String Today = df.format(new Integer(cal.get(cal.DAY_OF_MONTH))) + ' ' + months[month] + ' ' + Integer.toString(cal.get(cal.YEAR));
        return Today;

    }

    public String getTimeOfDayDashed() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String TimeOfDay = Integer.toString(cal.get(cal.HOUR_OF_DAY)) + '-' + df.format(new Integer(cal.get(cal.MINUTE))) + '-' + df.format(new Integer(cal.get(cal.SECOND))) + '-' + df.format(new Integer(cal.get(cal.MILLISECOND)));
        return TimeOfDay;
    }

    public String getDayForDisplay() {

        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        int day = cal.get(cal.DAY_OF_WEEK);
        int month = cal.get(cal.MONTH);

        String Today = df.format(new Integer(cal.get(cal.DAY_OF_MONTH))) + ' ' + months[month] + ' ' + Integer.toString(cal.get(cal.YEAR));
        return Today;

    }

    public Calendar getDateObjectFor(String datum) {
        // 0123456789012345678
        // 2014-12-12 23:00:23
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(cal.YEAR, Integer.parseInt(datum.substring(0, 4)));
            cal.set(cal.MONTH, Integer.parseInt(datum.substring(5, 7)) - 1);
            cal.set(cal.DAY_OF_MONTH, Integer.parseInt(datum.substring(8, 10)));
            if (datum.indexOf(' ') > -1) {
                cal.set(cal.HOUR_OF_DAY, Integer.parseInt(datum.substring(11, 13)));
                cal.set(cal.MINUTE, Integer.parseInt(datum.substring(14, 16)));
                cal.set(cal.SECOND, Integer.parseInt(datum.substring(17, 19)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            cal = null;
        }
        
        return cal;
    }

    public String isDatum(String datum) {

        if (datum.indexOf("0000-") > -1 || datum.indexOf("-00") > -1) {
            return "0000-00-00";
        }
        DecimalFormat df = new DecimalFormat("00");
        DecimalFormat df2 = new DecimalFormat("0000");
        Calendar cal = Calendar.getInstance();
        try {
            if (datum.indexOf('-') == -1) {
                datum = datum.substring(0, 4) + "-" + datum.substring(4, 6) + "-" + datum.substring(6);
            }
            if (datum.length() == 10) {
                int iYear = Integer.parseInt(datum.substring(0, 4));
                if (iYear >= 0001 && iYear <= 9999) {
                    cal.set(cal.YEAR, iYear);
                    int iMonth = Integer.parseInt(datum.substring(5, 7)) - 1;
                    if (iMonth >= 0 && iMonth <= 12) {
                        cal.set(cal.MONTH, iMonth);
                        int iDay = Integer.parseInt(datum.substring(8, 10));
                        if (iDay >= 1 && iDay <= 31) {
                            cal.set(cal.DAY_OF_MONTH, iDay);
                            datum = (df2.format(new Integer(cal.get(cal.YEAR)))) + '-' + df.format(new Integer(cal.get(cal.MONTH) + 1)) + '-' + df.format(new Integer(cal.get(cal.DAY_OF_MONTH)));
                        } else {
                            throw new Exception();
                        }
                    } else {
                        throw new Exception();
                    }
                } else {
                    throw new Exception();
                }
            } else {
                datum = "0000-00-00";
            }
        } catch (Exception e) {
            datum = "0000-00-00";
        }
        return datum;
    }

    public String stripTime(String date) {
        int iIndex = date.indexOf(' ');
        if (iIndex > -1) {
            date = date.substring(0, iIndex);
        }
        return date;
    }

    public String stripDate(String time) {
        int iIndex = time.indexOf(' ');
        if (iIndex > -1) {
            time = time.substring(iIndex + 1);
        }
        return time;
    }

    public int getDifferenceInDays(String sGreaterDate, String sLesserDate) {
        int iDays = 0;
        // deal with datetime strings
        sGreaterDate = COMMONtoolkit.getInstance().reformatDate(stripTime(sGreaterDate));
        sLesserDate = COMMONtoolkit.getInstance().reformatDate(stripTime(sLesserDate));
        try {
            StringTokenizer st = new StringTokenizer(sGreaterDate, "-");
            String y2, m2, d2;
            String y1, m1, d1;

            y2 = st.nextToken();
            m2 = st.nextToken();
            d2 = st.nextToken();

            st = new StringTokenizer(sLesserDate, "-");

            y1 = st.nextToken();
            m1 = st.nextToken();
            d1 = st.nextToken();

            int iY2, iY1, iM2, iM1, iD2, iD1;

            iY2 = Integer.parseInt(y2);
            iY1 = Integer.parseInt(y1);
            iM2 = Integer.parseInt(m2);
            iM1 = Integer.parseInt(m1);
            iD2 = Integer.parseInt(d2);
            iD1 = Integer.parseInt(d1);
            iDays = (iY2 * 365 + (iM2 - 1) * 30 + iD2) - (iY1 * 365 + (iM1 - 1) * 30 + iD1);
        } catch (Exception e) {
            System.out.println("NOT A DATE: " + sGreaterDate + " and/or " + sLesserDate);
            return 0;
        }

        return iDays;
    }

    public String getRunDay() {
        return RunDay;
    }

    public void setRunDay(String Runday) {
        this.RunDay = Runday;
    }

    public static void main(String[] args) {
        System.out.println(COMMONdate.getInstance().addMinutesToDateTime("2015-04-22 08:48:56", -00,-06,-57));
    }
}
