package com.wentongwang.notebook.view.custome;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;

import com.wentongwang.notebook.managers.RecorderManager;

/**
 *
 * Created by Wentong WANG on 2016/7/8.
 */
public class AudioRecoderButton extends Button {

    private static final int STATE_NORMAL = 1;// 默认的状态
    private static final int STATE_RECORDING = 2;// 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3;// 希望取消

    private int mCurrentState = STATE_NORMAL; // 当前的状态
    private boolean isRecording = false;// 已经开始录音

    private static final int DISTANCE_Y_CANCEL = 50;

    private RecorderManager mRecorderManager;
    private RecoderDialog mRecoderDialog;

    private float mTime;//录音时长


    private static final int MSG_AUDIO_PREPARED = 0x110; //开始录音
    private static final int MSG_VOICE_CHANGED = 0x111; //录音音量改变
    private static final int MSG_DIALOG_DIMISS = 0x112; //录音结束

    /**
     * 更改音量的线程，短时间间隔，不断发送信息给handler来更新音量
     */
    private Thread changeVoice = new Thread(new Runnable() {
        @Override
        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    isRecording = true;
                    //录音开始显示对话框
                    mRecoderDialog.showRecordingDialog();
                    //开启更改音量的线程
                    new Thread(changeVoice).start();
                    break;
                case MSG_VOICE_CHANGED:
                    //改变录音音量
                    mRecoderDialog.updateVoiceLevel(mRecorderManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    //隐藏对话框
                    mRecoderDialog.dimissDialog();
                    break;
            }
        }
    };


    public AudioRecoderButton(Context context) {
        this(context, null);
    }

    public AudioRecoderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) { return; }
        init(context);

    }

    private void init(Context context) {
        mRecoderDialog = new RecoderDialog(context);

        mRecorderManager = new RecorderManager();
        //设置保存录音文件路径
        mRecorderManager.setSaveDir(context.getCacheDir().getPath());
        mRecorderManager.setRecoderStateListener(new RecorderManager.RecoderStateListener() {
            @Override
            public void endPrepare() {
                //录音准备完成通知显示对话框
                mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
            }
        });

        changeState(STATE_NORMAL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();// 获得x轴坐标
        int y = (int) event.getY();// 获得y轴坐标
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下开始录音
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    // 如果想要取消，根据x,y的坐标看是否需要取消
                    if (wantToCancle(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                        mRecoderDialog.wantToCancel();
                    } else {
                        changeState(STATE_RECORDING);
                        mRecoderDialog.recording();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                if (!isRecording || mTime < 0.6f) {
                    //时间过短取消录制
                    mRecoderDialog.tooShort();
                    mRecorderManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);// 延迟显示对话框
                } else if (mCurrentState == STATE_RECORDING) { // 正在录音的时候，结束
//TODO：先用cancel不会保存录音文件，等完全可以使用了改成release可以保存录音文件
//                    mRecorderManager.release();
                    mRecorderManager.cancel();
                    mRecoderDialog.dimissDialog();
                    if (finishRecodeListener != null) {
                        finishRecodeListener.onFinish(mTime, mRecorderManager.getmCurrentFilePath());
                    }

                } else if (mCurrentState == STATE_WANT_TO_CANCEL) { // 想要取消
                    mRecorderManager.cancel();
                    mRecoderDialog.dimissDialog();
                }

                changeState(STATE_NORMAL);
                break;

        }

        return super.onTouchEvent(event);
    }

    /**
     * 判断是否是需要取消的状态
     * @param x
     * @param y
     * @return
     */
    private boolean wantToCancle(int x, int y) {
//        if (x < 0 || x > getWidth()) { // 超过按钮的宽度
//            return true;
//        }

        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }
    /**
     * 改变
     */
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    isRecording = false;
//                    setText("点我录音");
                    break;
                case STATE_RECORDING:
//                    setText("正在录音");
                    if (!isRecording) {
                        //开始录音
                        mRecorderManager.recorde();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
//                    setText("松开取消录音");
                    break;
            }
        }
    }


    private OnFinishRecodeListener finishRecodeListener;
    public void setOnFinishRecodeListener(OnFinishRecodeListener listener) {
        finishRecodeListener = listener;
    }
    public interface OnFinishRecodeListener{
        void onFinish(float time, String filePath);
    }
}
