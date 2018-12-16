package com.airxiao.audio.business;

import android.content.Context;

import com.airxiao.audio.bean.FileInfo;
import com.airxiao.audio.exception.BusinessException;


public class TaskImpl implements ITask{

    private static final String TAG = TaskImpl.class.getName();
    private static final int TASK_COUNT = 10;
    private static final String CREATE = "CREATE";
    private static final String DEAL = "DEAL";

    private Context context;

    static class Instance {
        static TaskImpl instance = new TaskImpl();
    }

    public static TaskImpl getInstance() {
        return Instance.instance;
    }

    private TaskImpl() {
    }

    @Override
    public String download(FileInfo fileInfo) throws BusinessException {
        return null;
    }

}
