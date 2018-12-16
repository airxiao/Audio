package com.airxiao.audio.exception;

import java.util.concurrent.Future;

/**
 * 功能说明：
 * 版权申明：浙江大华技术股份有限公司
 * 创建标记：Xiao_Yunlou 2018-12-12
 */

public abstract class BaseRunnable implements Runnable {

    private BaseHandler mHandle;
    private Future<?> mFuture;

    public BaseRunnable(BaseHandler handle) {
        mHandle = handle;
        mFuture = ThreadPool.submit(this);
    }

    @Override
    public void run() {
        try {
            doBusiness();
        } catch (BusinessException e) {
            e.printStackTrace();
            if (mHandle != null) {
                mHandle.obtainMessage(HandleMessageCode.HMC_EXCEPTION, e.errorCode, e.expandCode ,e.errorDescription)
                        .sendToTarget();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (mHandle != null) {
                mHandle.obtainMessage(HandleMessageCode.HMC_EXCEPTION,
                        BaseErrorCode.BEC_COMMON_UNKNOWN, BaseErrorCode.BEC_COMMON_UNKNOWN)
                        .sendToTarget();
            }
        }
    }

    public abstract void doBusiness() throws BusinessException;

    public void cancel() {
        if (mFuture != null && !mFuture.isCancelled() && !mFuture.isDone()) {
            mFuture.cancel(true);
        }
    }

}
