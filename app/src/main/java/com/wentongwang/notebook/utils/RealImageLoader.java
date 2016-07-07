package com.wentongwang.notebook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * imageLoader全套
 * Created by Wentong WANG on 2016/7/6.
 */
public class RealImageLoader {

    private static final String TAG = "ImageLoader";

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int IO_BUFFER_SIEZ = 8 * 1024;
    //给handler的信号
    private static final int MESSAGE_POST_RESULT = 1;
    //cpu的数量
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //线程池最大线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //闲置超时时长
    private static final long KEEP_ALIVE = 10L;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };
    //imageloader的线程池
    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);
    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 本地磁盘缓存的核心
     */
    private DiskLruCache mDiskLruCache;
    //是否创建了本地缓存
    private boolean diskCacheCreated = false;
    /**
     * uihandler,用于更新主线程上的ui
     */
    private Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //这里写对ui主线程中设置bitmap的操作
            if (msg.what == MESSAGE_POST_RESULT) {
                LoaderResult result = (LoaderResult) msg.obj;
                if (result != null) {
                    String tag = (String) result.imageView.getTag();
                    if (tag.equals(result.url)) {
                        result.imageView.setImageBitmap(result.bitmap);
                    } else {
                        Log.e(TAG, "url is changed, can't set this bitmap");
                    }
                }
            }
        }
    };

    private Context mContext;

    private RealImageLoader(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        //初始化lru
        //获取应用最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        //图片的缓存容器
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //返回每个图片的大小
                return value.getRowBytes() * value.getHeight();
            }
        };

        //初始化本地磁盘缓存
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }
        //判断空间是否足够
        if (diskCacheDir.getUsableSpace() > DISK_CACHE_SIZE) {
            //open方法参数
            //第一个参数指定的是数据的缓存地址，
            //第二个参数指定当前应用程序的版本号，
            //第三个参数指定同一个key可以对应多少个缓存文件，基本都是传1，
            //第四个参数指定最多可以缓存多少字节的数据
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                diskCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取磁盘缓存文件夹
     * @param mContext
     * @param dirName 文件夹名称
     * @return
     */
    private File getDiskCacheDir(Context mContext, String dirName) {
        //判断sd卡是否可用
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + dirName);
    }

    public static RealImageLoader getInstance(Context context) {
        return new RealImageLoader(context);
    }

    /**
     * 获取可用空间大小
     * @return
     */
    private long getAvailableSize(File path) {
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;

    }


    /**
     * 将图片加到缓存中
     * @param key 图片url
     * @param bitmap 图片
     */
    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            if (bitmap != null)
                mLruCache.put(key, bitmap);
            else
                Log.e(TAG, "bitmap is null");
        } else {
            if (bitmap != null) {
                mLruCache.remove(key);
                mLruCache.put(key, bitmap);
            }
            else
                Log.e(TAG, "bitmap is null");
        }
    }

    /**
     * 从缓存中拿出bitmap
     * @param key 图片的url
     * @return
     */
    private Bitmap getBitmapFromCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * 从本地缓存中读取图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap getBitmapFromDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "should be done in the UI thread");
        }

        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = mDiskLruCache.get(url);
        if (snapshot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(0);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = ImageUtils.decodeBitmapFromFileDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToCache(url,bitmap);
            }
        }

        return bitmap;
    }

    /**
     * 向磁盘中添加图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return 添加后从磁盘中读取
     * @throws IOException
     */
    private Bitmap saveBitmapToDiskCache(String url, int reqWidth, int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.w(TAG, "should be done in the UI thread");
        }

        DiskLruCache.Editor editor = mDiskLruCache.edit(url);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            if (downLoadStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return getBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    /**
     * 网上下载图片的流
     * @param urlString
     * @param outputStream
     * @return
     */
    private boolean downLoadStream(String urlString, OutputStream outputStream) {

        HttpURLConnection httpURLConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(httpURLConnection.getInputStream(), IO_BUFFER_SIEZ);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIEZ);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            in.close();
            out.close();
            httpURLConnection.disconnect();

            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return false;
    }


    /**
     * 图片异步加载
     * @param url
     * @param imageView
     * @param reqWidth
     * @param reqHeight
     */
    public void bindBitmap(final String url, final ImageView imageView, final int reqWidth, final int reqHeight){
        imageView.setTag(url);
        //先看缓存中有没有，有的话就直接用
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        //异步加载任务
        Runnable loadTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    Message message = mUiHandler.obtainMessage();
                    message.obj = result;
                    message.what = MESSAGE_POST_RESULT;
                    mUiHandler.sendMessage(message);
                }
            }
        };
        //加入线程池
        THREAD_POOL_EXECUTOR.execute(loadTask);
    }

    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        //先看缓存中有没有，有的话就直接用
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap != null) {
            return bitmap;
        }

        try {
            //从本地读取
            bitmap = getBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                return bitmap;
            }
            //网上下载并保存到本地
            bitmap = saveBitmapToDiskCache(url, reqWidth, reqHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitmap == null && !diskCacheCreated) {
            //缓存没有,本地读取失败,或者没开本地缓存,直接网上下载
            bitmap = downLoadBitmap(url);
        }

        //下载成功后保存到缓存
        if (bitmap != null) {
            addBitmapToCache(url, bitmap);
        }

        return bitmap;
    }

    /**
     * 根据url下载图片
     * @param urlString
     * @return
     */
    private Bitmap downLoadBitmap(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream inputStream = null;

        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(httpURLConnection.getInputStream(), IO_BUFFER_SIEZ);
            bitmap = BitmapFactory.decodeStream(inputStream);

            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "error in download : " + e);
            e.printStackTrace();
        }



        return bitmap;
    }


    private static class LoaderResult{
        ImageView imageView;
        String url;
        Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
        }
    }
}
