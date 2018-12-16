package com.airxiao.audio.exception;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class BaseHandler extends Handler {

    private AtomicBoolean isCancle = new AtomicBoolean(false);

    public BaseHandler(Looper looper) {
        super(looper);
    }

    public BaseHandler(){
        super();
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (!isCancle.get()) {
            if (msg.what == HandleMessageCode.HMC_EXCEPTION) {
                /*if (msg.arg1 == BusinessErrorCode.BEC_COMMON_UNAUTHORIZED) {
                    authError(msg);
                }else  if (msg.arg1 == BusinessErrorCode.BEC_USER_FREEZE) {
                	childError(msg);
                }*/
            }
            handleBusiness(msg);
        }
    }

    /**
     * 回调信息
     *
     * @param msg
     */
    public abstract void handleBusiness(Message msg);

    /**
     * 鉴权信息失败
     */
    public void authError(Message msg) {

    };
    /**
     * 鉴权信息失败
     */
    public void childError(Message msg) {

    };

    /**
     * 取消数据回调
     */
    public void cancle() {
        isCancle.set(true);
    }

    /**
     * 是否继续运行
     * @return
     */
    public boolean canRun() {
        return !(isCancle.get());
    }

}
