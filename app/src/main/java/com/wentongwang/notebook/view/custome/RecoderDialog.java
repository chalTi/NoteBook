package com.wentongwang.notebook.view.custome;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;


/**
 * 录音时弹出对话框
 * Created by Wentong WANG on 2016/7/8.
 */
public class RecoderDialog {

    private ImageView mVoice;
    private TextView mLable;

    private Context mContext;

    private Dialog dialog;

    /**
     * 构造方法 传入上下文
     */
    public RecoderDialog(Context context) {
        this.mContext = context;
    }

    // 显示录音的对话框
    public void showRecordingDialog() {

        dialog = new Dialog(mContext, R.style.mydialog_translucent_style);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recoder_dialog_layout, null);


        mVoice = (ImageView) view.findViewById(R.id.tv_voice);
        mLable = (TextView) view.findViewById(R.id.tv_recoder_state);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        //透明
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        dialog.show();
    }

    public void recording() {
        if (dialog != null && dialog.isShowing()) { //显示状态

            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mLable.setText("正在录音,手指上滑可取消");
        }
    }

    // 显示想取消的对话框
    public void wantToCancel() {
        if (dialog != null && dialog.isShowing()) { //显示状态

            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);


            mLable.setText("松开手指,取消录音");
        }
    }

    // 显示时间过短的对话框
    public void tooShort() {
        if (dialog != null && dialog.isShowing()) { //显示状态
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mLable.setText("录音时间过短");
        }
    }

    // 显示取消的对话框
    public void dimissDialog() {
        if (dialog != null && dialog.isShowing()) { //显示状态
            dialog.dismiss();
            dialog = null;
        }
    }


    /**
     *
     * 更新音量
     * 利用getIdentifier()方法获取资源ID
     * 方法描述:
     * getIdentifier(String name, String defType, String defPackage)
     * 第一个参数:资源的名称
     * 第二个参数:资源的类型(drawable,string)
     * 第三个参数:包名
     */
    public void updateVoiceLevel(int level) {
        if (dialog != null && dialog.isShowing()) { //显示状态
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            //设置图片的id
            int resId = mContext.getResources().getIdentifier("voice" + level, "drawable", mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }

}
