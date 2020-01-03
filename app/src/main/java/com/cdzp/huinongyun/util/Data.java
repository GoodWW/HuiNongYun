package com.cdzp.huinongyun.util;

import com.cdzp.huinongyun.bean.UserLoginResult;
import com.cdzp.huinongyun.bean.VideoTypeResponse;

import java.util.List;

public class Data {
    //        public static String url = "192.168.101.200:9013";
    public static String url = "101.200.193.60:9098";//正式
    //    public static String url = "192.168.101.200:8085";//测试
    //    public static String url = "39.97.228.247:8082";//中农富兴
    //    public static String url = "39.106.173.112:8087";
    public static UserLoginResult userResult;//用户登录返回
    public static VideoTypeResponse videoResponse;//视频播放信息
    public static String GhType;//当前选择的终端
    //pGhList,cGhList;[0]:终端ID,[1]:终端名,[2]:DisplayNo,[3]:ConnectType,[4]:DisplayIP,[5]:DisplayPort,[6]:Status
    public static List<String[]> pGhList;
    public static List<List<String[]>> cGhList;
    public static List<List<String[]>> locList;
    public static List<List<List<String[]>>> locList1;

    public static String SERVER_IP = "101.200.193.60";//正式
    //    public static String SERVER_IP = "192.168.101.200";//测试
    //    public static String SERVER_IP = "39.97.228.247";//中农富兴
    public static int SERVER_PORT = 50050;
//    public static int SERVER_PORT = 50050;
//    public static String SERVER_IP = "39.106.173.112";
//    public static int SERVER_PORT = 50050;

    public static void clear() {
        userResult = null;
        videoResponse = null;
        GhType = null;
        pGhList = null;
        cGhList = null;
        locList = null;
        locList1 = null;
    }
}
