package com.wentongwang.notebook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 用于下载图片的三级缓存
 * Created by Wentong WANG on 2016/6/21.
 */
public class ImageLoader {
    private static ImageLoader mInstance = null;
    /**
     * 是否允许缓存
     */
    private boolean isAllowedCache = true;
    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLurCache;

    private Context mContext;
    //TODO:优化下这个路径，可不可以不需要这个路径之类的
    private String mSavePath;
    private ImageLoader(Context context, String mSavePath) {
        this.mContext = context;
        this.mSavePath = mSavePath;
        //获取应用最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //图片的缓存容器
        mLurCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回每个图片的大小
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * 获取实例
     * @return
     */
    public static ImageLoader getmInstance(Context context, String savePath) {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader(context, savePath);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取图片
     * @param picUrl
     */
    public void downLoadBitmap(String picUrl, final onLoadBitmapListener listener) {
        Bitmap bitmap;
        //1.从内部缓存中获取图片
        bitmap = getBitmapFromLruCache(picUrl);
        if (bitmap != null) {
            //如果从缓存中获取到了,通过回调让用户去使用该bitmap
            if (listener != null) {
                listener.onLoad(bitmap);
            }
        } else {
            //2.没有的话，从本地文件中取图片
            File saveFile = new File(mSavePath, "userHead");
            bitmap = BitmapFactory.decodeFile(saveFile.getAbsolutePath());
            //把图片加入到缓存
            if (bitmap != null) {
                if (isAllowedCache) {
                    addBitmapToLruCache(picUrl, bitmap);
                }
                if (listener != null) {
                    listener.onLoad(bitmap);
                }
            } else {
                //3.还是没有的话，请求网络获取
                downloadBitmap(picUrl, new onNetworkListener() {
                    @Override
                    public void onSuccess(String picUrl) {

                    }
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if (listener != null) {
                            listener.onLoad(bitmap);
                        }
                    }
                    @Override
                    public void onFailure(String msg) {

                    }
                });
            }
        }

    }


    /**
     * 从缓存中获取图片
     * @param picUrl
     * @return
     */
    private Bitmap getBitmapFromLruCache(String picUrl) {
        return mLurCache.get(picUrl);
    }

    /**
     * 将图片加入到缓存里
     *
     * @param picUrl
     * @param bm
     */
    private void addBitmapToLruCache(String picUrl, Bitmap bm) {
        if (getBitmapFromLruCache(picUrl) == null) {
            if (bm != null) {
                mLurCache.put(picUrl, bm);
            }
        } else {
            if (bm != null) {
                mLurCache.remove(picUrl);
                mLurCache.put(picUrl, bm);
            }
        }
    }

    /**
     * 从服务器下载头像
     * @param picUrl
     */
    private void downloadBitmap(final String picUrl, final onNetworkListener listener){
        BmobFile file = new BmobFile("userHead.png","",picUrl);
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        File saveFile = new File(mSavePath, "userHead");
        if (saveFile.exists()) {
            saveFile.delete();
        }
        file.download(mContext, saveFile, new DownloadFileListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String savePath) {
                Bitmap bitmap = BitmapFactory.decodeFile(savePath);
                if (isAllowedCache) {
                    addBitmapToLruCache(picUrl, bitmap);
                }
                if (listener != null) {
                    listener.onSuccess(bitmap);
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });
    }

    /**
     * 上传图片到服务器
     */
    public void upLoadBitmap(final String picPath, final onNetworkListener listener){
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(mContext, new UploadFileListener() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    listener.onSuccess(bmobFile.getFileUrl(mContext));
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }

            @Override
            public void onFailure(int code, String msg) {
                if (listener != null) {
                    listener.onFailure(msg);
                }
            }
        });
    }

    /**
     * 是否允许使用缓存
     * @param useCache
     */
    public void useCache(boolean useCache) {
        this.isAllowedCache = useCache;
    }
    public interface onNetworkListener {
        void onSuccess(String picUrl);
        void onSuccess(Bitmap bitmap);
        void onFailure(String msg);
    }

    public interface onLoadBitmapListener{
        void onLoad(Bitmap bitmap);
    }
}
