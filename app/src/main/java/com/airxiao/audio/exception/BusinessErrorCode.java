package com.airxiao.audio.exception;

public class BusinessErrorCode extends BaseErrorCode{

    private final static int BEC_COMMON_BASE= 0;			// 通用

    private final static int BEC_USER_BASE= 2000;			// 用户模块
    private final static int BEC_DEVICE_BASE= 3000;			// 设备管理模块
    private final static int BEC_DEVICE_TREE_BASE = 3200;   // 设备树模块
    private final static int BEC_DEVICE_PTZ_BASE = 3500;    // 云台
    private final static int BEC_MESSAGE_BASE= 4000;		// 消息模块
    private final static int BEC_RECORD_BASE= 5000;			// 录像模块


    private final static int BEC_SCHEME_BASE = 9000;        //预案模块
    private final static int BEC_USERORG_BASE = 10000;      //用户组织
    private final static int BEC_ACCESS_BASE = 11000;       //门禁
    //UCS模块
    protected final static int BEC_MCU_BASE = 12000;        // 音视频会议
    protected final static int BEC_IMU_BASE = 13000;        // 即时通讯非会议部分

    protected final static int BEC_UCA_BASE_BEGIN = 14000;    //融合通信begin
    protected final static int BEC_UCA_BASE_END = 16000;      //融合通信end

    protected final static int BEC_VTO_TALK_BASE = 17000;       //可视对讲

}
