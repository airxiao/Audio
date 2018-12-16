package com.airxiao.audio.business;

import com.airxiao.audio.bean.FileInfo;
import com.airxiao.audio.exception.BusinessException;


public interface ITask {


    String download(FileInfo fileInfo) throws BusinessException;

}
