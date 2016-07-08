package com.wentongwang.notebook.managers;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 音频录制管理类
 * Created by Wentong WANG on 2016/7/8.
 */
public class RecorderManager {

    private MediaRecorder mediaRecorder;
    private boolean isRecoding = false;
    //音频存储文件夹
    private String mDir;
    //当前录制的录音文件的位置
    private String mCurrentFilePath;

    public void setSaveDir(String dir) {
        mDir = dir;
    }

    public void recorde() {

        try {
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = generateFileName();
            File file = new File(dir, fileName);

            mCurrentFilePath = file.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            //设置输出文件
            mediaRecorder.setOutputFile(mCurrentFilePath);
            // 设置MediaRecorder的音频源为麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            // 设置音频编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // 准备录音
            mediaRecorder.prepare();

            if (listener != null) {
                listener.endPrepare();
            }

            // 开始录音
            mediaRecorder.start();
            isRecoding = true;


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 生成文件的名称
     */
    private String generateFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        return  sdf.format(new Date())+ ".amr";
    }

//    /**
//     * 开始录音
//     */
//    public void startRecode() {
//
//        if (mediaRecorder == null || !isPrepare) {
//            prepareRecorde();
//        }
//
//        if (mediaRecorder != null) {
//            mediaRecorder.start();
//            isRecoding = true;
//        }
//    }
//
//    public void stopRecode(){
//        if (mediaRecorder != null && isRecoding) {
//            mediaRecorder.stop();
//            isRecoding = false;
//        }
//    }

    /**
     * 录音完成，释放资源
     */
    public void release() {
        mediaRecorder.reset(); //reset是可以从任何状态恢复到初始 stop只能在录音状态恢复到初始
        mediaRecorder = null;
        isRecoding = false;
    }

    /**
     * 取消录音，删除本地文件
     */
    public void cancel() {
        if (mediaRecorder != null && isRecoding) {
            release();
            if (mCurrentFilePath != null) {
                File file = new File(mCurrentFilePath);
                file.delete();
                mCurrentFilePath = null;
            }
        }
    }

    /**
     * 获取音量
     * @param maxlevel
     * @return
     */
    public int getVoiceLevel(int maxlevel) {
        if (isRecoding) {
            try {
                // mMediaRecorder.getMaxAmplitude() 1~32767
                return maxlevel * mediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
            }
        }
        return 1;
    }

    public String getmCurrentFilePath() {
        return mCurrentFilePath;
    }


    private RecoderStateListener listener;
    public void setRecoderStateListener(RecoderStateListener listener) {
        this.listener = listener;
    }
    public interface RecoderStateListener{
        void endPrepare();
    }
}
