package com.airxiao.audio.business;

import com.airxiao.audio.bean.FileInfo;
import com.airxiao.audio.exception.BaseHandler;
import com.airxiao.audio.exception.BaseRunnable;
import com.airxiao.audio.exception.BusinessException;
import com.airxiao.audio.exception.HandleMessageCode;


public class TaskProxy {

    private ITask mTaskInterface;

    static class Instance {
        static TaskProxy instance = new TaskProxy();
    }

    public static TaskProxy getInstance() {
        return Instance.instance;
    }

    private TaskProxy() {
        mTaskInterface = TaskImpl.getInstance();
    }


    public void download(final FileInfo fileInfo, final BaseHandler baseHandler) throws BusinessException {
        BaseRunnable baseRunnable = new BaseRunnable(baseHandler) {
            @Override
            public void doBusiness() throws BusinessException {
                String path = mTaskInterface.download(fileInfo);
                baseHandler.obtainMessage(HandleMessageCode.HMC_SUCCESS, path).sendToTarget();
            }
        };
    }


}
