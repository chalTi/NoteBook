package com.wentongwang.notebook.view.custome;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.utils.ImageUtils;


/**
 * Created by Wentong WANG on 2016/6/2.
 */
public class CircleImageView extends View {
    //该view的长宽
    private int imageSize;
    private int resourceId = -1;
    //要显示的图片
    private Bitmap bitmap;
    private Bitmap defaut;

    private Context mContext;
    private Paint mPaint;


    private static final int DEFAUT = 0;
    private static final int RESOURCE = 1;
    private static final int BITMAP = 2;
    private int mPicFrom = DEFAUT;
    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measure(heightMeasureSpec);
        int width = measure(widthMeasureSpec);

        imageSize = Math.max(width, height);
        setMeasuredDimension(imageSize, imageSize);
    }

    /**
     * 测量宽高
     *
     * @param measureSpec
     * @return
     */
    private int measure(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        int result = 0;
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = 50;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, result);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);
        //显示圆形图片有问题
        switch (mPicFrom) {
            case DEFAUT:
                //最后使用默认
                Log.i("xxxx", "使用默认");
                defaut = ImageUtils.decodeBitmapFromResource(getResources(), R.drawable.user_head_defaut, imageSize, imageSize);
                defaut = ImageUtils.resize(bitmap, imageSize, imageSize);
                canvas.drawBitmap(ImageUtils.creatCircleBitmap(defaut, imageSize), 0, 0, mPaint);
                break;
            case RESOURCE:
                bitmap = ImageUtils.decodeBitmapFromResource(getResources(), resourceId, imageSize, imageSize);
                bitmap = ImageUtils.resize(bitmap, imageSize, imageSize);
                canvas.drawBitmap(ImageUtils.creatCircleBitmap(bitmap, imageSize), 0, 0, mPaint);
                break;
            case BITMAP:
                //如果使用的是bitmap
                Log.i("xxxx", "使用bitmap");
                bitmap = ImageUtils.resize(bitmap, imageSize, imageSize);
                canvas.drawBitmap(ImageUtils.creatCircleBitmap(bitmap, imageSize), 0, 0, mPaint);
                break;
        }

    }

    public void setImage(int resourceId) {
        this.resourceId = resourceId;
//        bitmap = ImageUtils.decodeBitmapFromResource(getResources(),resourceId,imageSize,imageSize);
        mPicFrom = RESOURCE;
        invalidate();
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
        mPicFrom = BITMAP;
        invalidate();
    }
}
