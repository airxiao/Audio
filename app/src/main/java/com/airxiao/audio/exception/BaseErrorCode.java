package com.airxiao.audio.exception;


public class BaseErrorCode {

    public final static int BEC_COMMON_BASE= 0;			// 通用

    /****************************通用错误模块*******************************/

    public final static int BEC_COMMON_OK = BEC_COMMON_BASE + 0;                         // 200 OK （API调用成功，但是具体返回结果，由content中的code和desc描述）
    public final static int BEC_COMMON_UNKNOWN = BEC_COMMON_BASE + 1; 	                // 未知错误
    public final static int BEC_COMMON_BAD_REQUEST = BEC_COMMON_BASE + 2;		        // 400 Bad Request （API格式错误，无返回内容）
    public final static int BEC_COMMON_UNAUTHORIZED = BEC_COMMON_BASE + 3;		        // 401 Unauthorized （用户名密码认证失败，无返回内容）
    public final static int BEC_COMMON_FORBIDDEN = BEC_COMMON_BASE + 4;		            // Forbidden （认证成功但是无权限，无返回内容）
    public final static int BEC_COMMON_NOT_FOUND = BEC_COMMON_BASE + 5;		            // 404 Not Found （请求的URI错误，无返回内容)
    public final static int BEC_COMMON_PRECONDITION_FAILED = BEC_COMMON_BASE + 6;		// Precondition Failed （先决条件失败，无返回内容。通常是由于客户端所带的x-hs-date时间与服务器时间相差过大。）
    public final static int BEC_COMMON_SERVER_ERROR= BEC_COMMON_BASE + 7;		        // 500 Internal Server Error （服务器内部错误，无返回内容）
    public final static int BEC_COMMON_SERVICE_UNAVAILABLE = BEC_COMMON_BASE + 8;	//503 Service Unavailable （服务不可用，无返回内容。这种情况一般是因为接口调用超出频率限制。）

    public final static int BEC_COMMON_GETWAY_UNAVAILABLE = BEC_COMMON_BASE + 9;	    //502 服务方法执行错误Method error
    public final static int BEC_COMMON_NULL_POINT = BEC_COMMON_BASE + 10;               //业务空指针异常
    public final static int BEC_COMMON_BROADCAST_ERROR = BEC_COMMON_BASE + 11;          //广播机制发生异常
    public final static int BEC_COMMON_PARSEDATA_ERROR = BEC_COMMON_BASE + 12;          //数据解析异常
    public final static int BEC_COMMON_TIME_OUT = BEC_COMMON_BASE + 13;                 //连接服务超时*
    public final static int BEC_COMMON_PLATFORM_SDK_INIT_ERROR = BEC_COMMON_BASE + 14; //平台SDK初始化异常**/
    public final static int BEC_COMMON_CONNECT_SVR_FAILED = BEC_COMMON_BASE + 15;       //*连接服务失败**/
    public final static int BEC_COMMON_NO_PRIVILEDGE = BEC_COMMON_BASE + 16;            // 无权限
    public final static int BEC_COMMON_INVALID_PARAM = BEC_COMMON_BASE + 17;            //无效的参数
    public static final int BEC_COMMON_UNCONNECT_SERVER = BEC_COMMON_BASE + 20;         // 无法连接服务器
    public static final int BEC_COMMON_NO_MORE_DATA = BEC_COMMON_BASE + 21;             //没有更多数据了
    public static final int BEC_COMMON_FILE_EXIST = BEC_COMMON_BASE + 22;               //文件已存在
    public static final int BEC_COMMON_FILE_NOT_EXIST = BEC_COMMON_BASE + 23;           //文件不存在
}
