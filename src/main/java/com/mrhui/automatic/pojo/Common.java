package com.mrhui.automatic.pojo;

public interface Common {

    //对应userType   ----- start
    public static int TYPE_STUDENT = 1;// 学生
    public static int TYPE_ADMIN = 0;// 管理员
    public static int TYPE_HARDWARE = 2;// 设备
    //对应userType   ----- end

    //websocket接收命令
    public static int WS_BROADCAST = 100; //客户端广播数据
    public static int WS_GET_INFO = 200;//管理员获取信息
    public static int WS_SEND_MESSAGE = 300;//发送消息
    public static int WS_SEND_COMMEND = 400; //发送CMD

    //websocket 服务器主动广播回复命令 接收者为管理员
    public static int WS_LOGOUT = 2; //有用户登出
    public static int WS_UPDATE_INFO = 1; //有用户登录
    public static int WS_ERROR_GET_INFO_FAIL = 4005;
    public static int WS_ERROR_USER_UNAUTHORIZED = 4001;
    public static int WS_SERVER_ERROR = 4006;
    public static int WS_OCCUPY = 4002;


    //LoggingType 日志类型
    public static int LOG_USER_LOGIN = 1000; // 用户登录
    public static int LOG_ERROR = 1100; // 错误级别日志
    public static int LOG_WARNING = 1101;// 警告级别日志
    public static int LOG_USER_LOGOUT = 1001;// 用户退出登录
    public static int LOG_FACE_RECOGNITION = 1002;// 人脸识别
}
