package com.example.bsproperty.net;

/**
 * Created by yezi on 2018/1/27.
 */

public class ApiManager {

    private static final String HTTP = "http://";
    private static final String IP = "192.168.55.103";
    private static final String PROT = ":8080";
    private static final String HOST = HTTP + IP + PROT;
    private static final String API = "/api";
    private static final String USER = "/user";
    private static final String WORK = "/work";
    private static final String POINT = "/point";


    public static final String REGISTER = HOST + API + USER + "/register";
    public static final String LOGIN = HOST + API + USER + "/login";
    public static final String USER_CHECK = HOST + API + USER + "/check";
    public static final String USER_LIST = HOST + API + USER + "/list";

    public static final String WORK_LIST = HOST + API + WORK + "/list";

    public static final String POINT_ADD = HOST + API + POINT + "/add";
    public static final String POINT_LIST = HOST + API + POINT + "/list";
    public static final String POINT_CHANGE = HOST + API + POINT + "/change";
}
