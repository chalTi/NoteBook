package com.wentongwang.notebook.view.custome;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wentongwang.notebook.R;


/**
 * Created by Wentong WANG on 2016/7/8.
 */
public class RecoderDialog {

    private AlertDialog.Builder builder;
    private ImageView mIcon;
    private TextView mVoice;
    private TextView mLable;

    private Context mContext;

    private AlertDialog dialog;//用于取消AlertDialog.Builder

    /**
     * 构造方法 传入上下文
     */
    public RecoderDialog(Context context) {
        this.mContext = context;
    }

    // 显示录音的对话框
    public void showRecordingDialog() {

        builder = new AlertDialog.Builder(mContext, R.style.mydialog_translucent_style);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recoder_dialog_layout, null);
//
//        mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
        mVoice = (TextView) view.findViewById(R.id.tv_voice);
        mLable = (TextView) view.findViewById(R.id.tv_recoder_state);

        builder.setView(view);
        builder.create();
        dialog = builder.show();
    }

    public void recording() {
        if (dialog != null && dialog.isShowing()) { //显示状态
//            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

//            mIcon.setImageResource(R.drawable.recorder);
            mLable.setText("手指上滑，取消发送");
        }
    }

    // 显示想取消的对话框
    public void wantToCancel() {
        if (dialog != null && dialog.isShowing()) { //显示状态
//            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

//            mIcon.setImageResource(R.drawable.cancel);
            mLable.setText("松开手指，取消发送");
        }
    }

    // 显示时间过短的对话框
    public void tooShort() {
        if (dialog != null && dialog.isShowing()) { //显示状态
//            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

//            mIcon.setImageResource(R.drawable.voice_to_short);
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

    // 显示更新音量级别的对话框
    public void updateVoiceLevel(int level) {
        if (dialog != null && dialog.isShowing()) { //显示状态
//          mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            mVoice.setText("" + level);
            //设置图片的id
//            int resId = mContext.getResources().getIdentifier(v+level, drawable, mContext.getPackageName());
//            mVoice.setImageResource(resId);
        }
    }

}
