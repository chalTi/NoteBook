package com.wentongwang.notebook.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ThumbnailUtils;
import android.os.Build;

/**
 * bitmap的工具类
 * Created by Wentong WANG on 2016/5/17.
 */
public class ImageUtils {

    /**
     *
     * 由资源id获取位图
     */

    public static Bitmap getBitmapById(Context context, int resId) {
        if (context == null) {
            return null;
        }
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * bitmap裁剪成圆形
     *
     * @param source
     * @param r 直径
     * @return
     */
    public static Bitmap creatCircleBitmap(Bitmap source, int r) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Bitmap mtraget = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);

        //产生同样大小的画布
        Canvas canvas = new Canvas(mtraget);
        //画圆
        canvas.drawCircle(r / 2, r / 2, r / 2, mPaint);
        //用src_in
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //最后裁剪
        if (r / 2 < source.getWidth()) {
            canvas.drawBitmap(source, 0, 0, mPaint);
        } else {
            canvas.drawBitmap(source,r / 2 - source.getWidth() / 2, r / 2 - source.getWidth() / 2, mPaint);
        }


        return mtraget;
    }

    /**
     * 对图片进行压缩（不改变宽高）, 展示在ImageView中
     *
     * @param res
     *            getResource()获取
     * @param resId
     *            資源id
     * @param reqWidth
     *            要求的宽度, 单位像素
     * @param reqHeight
     *            要求的高度, 单位像素
     * @return 压缩后的bitmap
     */
    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true,来获取图片大小,不会占据内存
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    /**
     * 计算压缩比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 改变图片的宽高
     * @param bm 所要转换的bitmap
     * @param newWidth 新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap resize(Bitmap bm, int newWidth ,int newHeight) {
        Bitmap newbm = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            newbm = ThumbnailUtils.extractThumbnail(bm, newWidth, newHeight);
        } else {
            // 获得图片的宽高
            int width = bm.getWidth();
            int height = bm.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        }
        return newbm;
    }
}
