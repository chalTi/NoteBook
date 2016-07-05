package com.wentongwang.notebook.model.business;

/**
 *
 * 服务器回复的回调接口
 * Created by Wentong WANG on 2016/7/5.
 */
public interface OnResponseListener {

    /**
     * 成功
     * @param response 回复的结果
     */
    void onSuccess(Object response);

    /**
     * 失败
     * @param msg 失败描述
     */
    void onFailure(String msg);
}
