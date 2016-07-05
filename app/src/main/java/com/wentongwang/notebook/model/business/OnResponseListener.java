package com.wentongwang.notebook.model.business;

import com.wentongwang.notebook.model.Response;

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
    void onResponse(Response response);

}
