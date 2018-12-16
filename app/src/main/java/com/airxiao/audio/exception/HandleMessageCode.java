package com.airxiao.audio.exception;


public class HandleMessageCode {

    private final static int HMC_EXCEPTION_BASE = 0; // 异常
    private final static int HMC_COMMON_BASE = 1000; // 通用
    private final static int HMC_ENVIRONMENT_BASE = 2000; // 环境配置模块
    private final static int HMC_USER_BASE = 3000; // 用户模块
    private final static int HMC_DEVICE_BASE = 4000; // 设备管理模块
    private final static int HMC_MESSAGE_BASE = 5000; // 消息模块
    private final static int HMC_RECORD_BASE = 6000; // 录像模块
    private final static int HMC_SCENEMODE_BASE = 7000; // 情景模式模块
    private final static int HMC_STATISTICS_BASE = 8000; // 统计模块

    // //////////////异常消息////////////////
    public final static int HMC_SUCCESS = HMC_EXCEPTION_BASE + 1;// 执行成功
    public final static int HMC_EXCEPTION = HMC_EXCEPTION_BASE + 2;// 执行失败
    public final static int HMC_BATCH_MIDDLE_RESULT = HMC_EXCEPTION_BASE + 3;// 批量操作中间结果
    public final static int HMC_BATCH_END = HMC_EXCEPTION_BASE + 4;// 批量操作结束

}
