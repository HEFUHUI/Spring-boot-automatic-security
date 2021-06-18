### 用户类型
#### 0、管理员
#### 1、普通用户 | 学生
#### 2、设备


### webSocket msg_code
#### 接收状态码
- WS_BROADCAST : 客户端广播数据 : 10x : 所有用户
  - 101 : ADMIN : 发送给所有管理员
  - 102 : STUDENT : 发送给所有学生
  - 103 : EQU : 发送给所有设备
  - 104 : ALL : 发送给所有在线用户
- WS_GET_INFO : 管理员获取信息 : 20x : 仅管理员
  - 201 : ADMIN : 获取所有在线管理员的信息  : LIST\<TUser>
    - 返回示例
    ```json
       {"code": 211,"data": [
      {"nickName": "溜溜没","userType": 0,"userId":"1401137287483359233"}
    ]}
    ```
  - 202 : STUDENT : 获取所有在线学生信息 : LIST\<TUser>
    - 返回示例
    ```json
       {"code": 212,"data": [
      {"nickName": "溜溜没","userType": 1,"userId":"1401137287483359233"}
    ]}
    ```
  - 203 : EQU : 获取所有在线设备信息 : LIST<TUser>
    - 返回示例
    ```json
       {"code": 213,"data": [
      {"nickName": "溜溜没","userType": 2,"userId":"1401137287483359233"}
    ]}
    ```
  - 204 : ALL : 获取所有在线用户信息 : LIST<TUser>
    - 返回示例
    ```json
       {"code": 214,"data": [
      {"nickName": "溜溜没","userType": 0,"userId":"1401137287483359233"},
      {"nickName": "溜溜没","userType": 1,"userId":"1401137287483359233"},
      {"nickName": "溜溜没","userType": 2,"userId":"1401137287483359233"}
    ]}
    ```
  - 205 : SINGLE : 获取单个用户信息 : TUser
    - data[destination] : 字段带目标用户ID String
    - 返回示例
    ```json
       {"code": 215,"data":{"nickName": "溜溜没","userType": 1,"userId":"1401137287483359233"}}
    ```
  - 206 : IS_ONLINE : 获得用户是否在线 : Boolean
    - 返回示例
      ```json
         {"code": 216,"data": true}
      ```
- WS_SEND_MESSAGE : 发送消息 : 30x
  - 301 发送给在线用户 com.mrhui.automatic.pojo.StandardResult
    - data[destination] 字段带目标用户ID String
- WS_SEND_COMMEND : 发送CMD : 40x
 - 401 : MOVE : 定向移动
 - 402 : HANDLE : 手动移动
 - 403 : RADIO : 发送语音
 - 404 : CUSTOM : 自定义命令

#### 返回状态码
- WS_ERROR : 发生错误 : 400x : 所有用户
  - 4001 : 用户未认证，或者无权限
  - 4005 : 用户信息获取失败
  - 4006 : 服务器发生错误
- WS_LOGOUT : 有用户退出登录 : 2 : 管理员
  - 直接返回该用户的信息
- WS_LOGIN : 有新的用户登录 : 1 : 管理员
  - 直接返回该用户的信息

## LogType
```java
interface common {
    //LoggingType 日志类型
    public static int LOG_USER_LOGIN = 1000; // 用户登录
    public static int LOG_ERROR = 1100; // 错误级别日志
    public static int LOG_WARNING = 1101;// 警告级别日志
    public static int LOG_USER_LOGOUT = 1001;// 用户退出登录
    public static int LOG_FACE_RECOGNITION = 1002;// 人脸识别
}
```


