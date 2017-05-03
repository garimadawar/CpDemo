package com.gd.cpdemo;

import android.net.Uri;

/**
 * Created by Rajesh Kumar Dawar on 11-04-2017.
 */

public class Util {
    //DATABASE
     public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Students.db";

    //TABLE
    public static final String TAB_NAME = "Student";
    public static final String COL_ID = "_ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_PHONE = "PHONE";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_GENDER = "GENDER";
    public static final String COL_CITY = "CITY";

    public static final String CREATE_TAB_QUERY = "create table Student(" +
            "_ID integer primary key autoincrement," +
            "NAME varchar(256)," +
            "PHONE varchar(20)," +
            "EMAIL varchar(256)," +
            "GENDER varchar(10)," +
            "CITY varchar(256)" +
            ")";

    // URI
    public static final Uri STUDENT_URI = Uri.parse("content://com.gd.cpdemo.studentpro/"+TAB_NAME);

    final static String URI = "http://garimadawar.esy.es/enc2017/";
    // URL
    public static final String INSERT_STUDENT_JSP = "http://garimadawar.esy.es/enc2017/insert.jsp";
    public static final String INSERT_STUDENT_PHP = "http://garimadawar.esy.es/enc2017/insert.php";

    public static final String RETRIEVE_STUDENT_PHP = "http://garimadawar.esy.es/enc2017/retrieve.php";
    public static final String UPDATE_STUDENT_PHP = "http://garimadawar.esy.es/enc2017/update.php";
    public static final String DELETE_STUDENT_PHP = "http://garimadawar.esy.es/enc2017/delete.php";
}
