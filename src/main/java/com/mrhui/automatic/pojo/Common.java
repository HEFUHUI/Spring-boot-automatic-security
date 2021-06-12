package com.mrhui.automatic.pojo;

public interface Common {

    //对应userType   ----- start
    public static int TYPE_STUDENT = 1;//学生
    public static int TYPE_ADMIN = 0;//管理员
    public static int TYPE_HARDWARE = 2;//设备
    //对应userType   ----- end

    //websocket接收命令
    public static int WS_COMMAND = 120;//发送到设备上面
    public static int WS_LOGOUT = 2; // 登出
    public static int WS_GET_INFO = 100;// 管理员获取信息 对应回复代码 101
    public static int WS_SEND_MESSAGE = 110;// 管理员发送消息 对应回复代码 111


    //LoggingType 日志类型
    public static int LOG_USER_LOGIN = 1000; // 用户登录
    public static int LOG_ERROR = 1100; // 错误级别日志
    public static int LOG_WARNING = 1101;// 警告级别日志
    public static int LOG_USER_LOGOUT = 1001;// 用户退出登录
    public static int LOG_FACE_RECOGNITION = 1002;// 人脸识别
}
