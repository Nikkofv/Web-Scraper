/*
 * -----------------------------------------------------------------------
 * Concept, design and deployment (c) Copyright 2015, VOGEL
 *                        All rights reserved.
 * -----------------------------------------------------------------------
 */

package com.vogel.common.util;

import java.sql.*;
import java.util.*;
import java.io.*;

/** A wrapper around accessing a database in Java.
 *  Note: mysql_com.jar (JDBC driver-file for MySQL) must be in your classpath for
 *        MySQL to work!
 *
 *  Example:
 *  <CODE><BR><BR>
    String driverName = "org.gjt.mm.mysql.Driver";<BR>
    String dbURL = "jdbc:mysql://192.168.1.1/test"; // test is name of database on server 192.168.1.1<BR><BR>

    try {<BR>
        DatabaseWrapper dw = new DatabaseWrapper(); // instantiate DatabaseWrapper<BR>
        dw.registerDriver( driverName ); // register driver<BR>
        java.sql.Connection conn = dw.getConnection( dbURL, "username", "pazzWord" ); // get a database-connection<BR>
        java.sql.ResultSet rs; // will contain the results from an SQL-query<BR><BR>

        rs = executeQuery( conn, "select * from PCTable" ); // do the SQL-query<BR><BR>

        dw.printResultSet( rs ); // print results in readable form to standard out<BR>
    }<BR>
    catch (Exception e) {<BR>
        e.printStackTrace();<BR>
    }<BR><BR>
    </CODE>
 *
 * @author  Frans Berkelaar
 * @version 
 */
public class DBW extends Object {
      
    /** */
    public static final String SAMPLEDRIVER = "org.gjt.mm.mysql.Driver";
    /** */
//    public static final String SAMPLEDBURL  = "jdbc:mysql://192.168.1.10/OGDB_A1";
    public static final String SAMPLEDBURL  = "jdbc:mysql://127.0.0.1/OGDB_A1";
    
    /** Registers a database-driver. Example: <CODE>registerDriver( "org.gjt.mm.mysql.Driver" );</CODE> 
     *  @param driverName Name of the driver, e.g. <CODE>org.gjt.mm.mysql.Driver</CODE>
     */
    public static void registerDriver( String driverName )
    throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class.forName( driverName ).newInstance();
    }
    
    /** The querystring may not contain single-quotes. This method will format a String
     * for you that is suitable as a querystring for executeQuery.
     * @param sqlString The string that needs to be formatted.
     * @return The formatted string.
     */
    public static String formatQuery( String sqlString ) {
        StringTokenizer st= new StringTokenizer( sqlString, "'" );
        StringWriter sw = new StringWriter();
        
        while (st.hasMoreTokens()) {
            sw.write( st.nextToken() );
            if (st.hasMoreTokens())
                sw.write( "''" );
        }
        
        return sw.toString();
    }
    
    /** Returns a connection to the database.
     * @param dbUrl The url-string of the database you want to have a connection to.
     *    E.g. <CODE>org.gjt.mm.mysql.Driver</CODE>
     * @param dbUserName Login of the database-user.
     * @param dbPassword Password of the database-user.
     */
    public static Connection getConnection( String dbUrl, String dbUserName,
    String dbPassword ) throws SQLException {
        return DriverManager.getConnection( dbUrl, dbUserName, dbPassword );
    }
    
    /** Only suitable for executing queries, i.e. SELECT-statements. Executes an
     *  SQL SELECT-statement.
     * @param conn An instance of java.sql.Connection. This is a connection to a database.
     * @param sqlString The SQL-statement that needs to be executed.
     * @return A java.sql.ResultSet containing the results of the executed SQL-statement.
     */
    public static ResultSet executeQuery( Connection conn, String sqlString )
    throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery( sqlString );
        return rs;
    }
    
    /** Only suitable for executing updates, e.g. INSERT-, UPDATE- and DELETE-statements.
     * Executes an SQL statement that changes data in the database.
     * @param conn An instance of java.sql.Connection. This is a connection to a database.
     * @param sqlString The SQL-statement that needs to be executed.
     * @return Number of rows that were effected by the SQL-statement.
     */
    public static int executeUpdate( Connection conn, String sqlString )
    throws SQLException {
        Statement stmt = conn.createStatement();
        int res = stmt.executeUpdate( sqlString );
        return res;
    }
    
    /** Prints a java.url.ResultSet in a readable way to the standard out.
     * @param rs The instance of java.url.ResultSet that you want to be printed.
     */
    public static void printResultSet( ResultSet rs ) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount;
        int labelLength;
        int k;
        int i;
        String line  = "";
        String label = null;
        
        columnCount = rsmd.getColumnCount();
        for (k=1; k<=columnCount; k++) {
            label = rsmd.getColumnLabel( k );
            System.out.print( label + '\t' );
            
            // construct line with exact same length as the label
            labelLength = label.length();
            label = "";
            for (i=0; i<labelLength; i++)
                label += '=';
            line += label + '\t';
        }
        
        System.out.println();
        System.out.println(line);
        
        while ( rs.next() ) {
            for (k=1; k<=columnCount; k++) {
                System.out.print( rs.getString( k ) + '\t' );
            }
            
            System.out.println();
        }
        
        System.out.println();
    }
    
    /** writes a java.url.ResultSet in a readable way to a vector.
     * @param rs The instance of java.url.ResultSet that you want to be printed.
     */
    public static Vector getResultSet( ResultSet rs ) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        Vector vResult=new Vector();
        Vector vRow;
        String rString;
        // stuur meta informatie mee met iedere vRow
        
        /** tel de attributen die daadwerkelijk een
         *  waarde hebben, dus <> leeg
         */
        int CntValuedAttr;
      
        while (rs.next()) {
            vRow=new Vector();
            CntValuedAttr=0;
            for (int k=1; k<=columnCount; k++) {
                rString=(String)rs.getString(k);
                if (rString.length()!=0)
                    CntValuedAttr++;
                vRow.add(rString);
            }
            vRow.add(Integer.toString(CntValuedAttr));
            vResult.add((Vector)vRow);
        }
        
        return vResult;
    }

}
