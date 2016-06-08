package com.wentongwang.notebook.custome;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import com.wentongwang.notebook.R;

/**
 * 自定义密码输入框
 *
 * 使用时引入   xmlns:custom="http://schemas.android.com/apk/res/[你的自定义View所在的包路径]"
 * Created by Wentong WANG on 2016/6/7.
 */
public class PasswordInputView extends EditText {

    private static final int defaultContMargin = 5;
    private static final int defaultSplitLineWidth = 3;

    private int borderColor = 0xFFCCCCCC;
    private float borderWidth = 5;
    private float borderRadius = 3;

    private int passwordLength = 6;
    private int passwordColor = 0xFFCCCCCC;
    private float passwordWidth = 8;
    private float passwordRadius = 3;

    private Paint passwordPaint = new Paint();
    private Paint borderPaint = new Paint();
    private int textLength;

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderWidth, dm);
        borderRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderRadius, dm);
        passwordLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordLength, dm);
        passwordWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordWidth, dm);
        passwordRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, passwordRadius, dm);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, 0, 0);
        borderColor = a.getColor(R.styleable.PasswordInputView_borderColor, borderColor);
        borderWidth = a.getDimension(R.styleable.PasswordInputView_borderWidth, borderWidth);
        borderRadius = a.getDimension(R.styleable.PasswordInputView_borderRadius, borderRadius);
        passwordLength = a.getInt(R.styleable.PasswordInputView_passwordLength, passwordLength);
        passwordColor = a.getColor(R.styleable.PasswordInputView_passwordColor, passwordColor);
        passwordWidth = a.getDimension(R.styleable.PasswordInputView_passwordWidth, passwordWidth);
        passwordRadius = a.getDimension(R.styleable.PasswordInputView_passwordRadius, passwordRadius);
        a.recycle();

        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);
        passwordPaint.setStrokeWidth(passwordWidth);
        passwordPaint.setStyle(Paint.Style.FILL);
        passwordPaint.setColor(passwordColor);
        passwordPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 外边框
        RectF rect = new RectF(0, 0, width, height);
        borderPaint.setColor(borderColor);
        canvas.drawRoundRect(rect, borderRadius, borderRadius, borderPaint);

        // 内容区
        RectF rectIn = new RectF(rect.left + defaultContMargin, rect.top + defaultContMargin,
                rect.right - defaultContMargin, rect.bottom - defaultContMargin);
        borderPaint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectIn, borderRadius, borderRadius, borderPaint);

        // 分割线
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 1; i < passwordLength; i++) {
            float x = width * i / passwordLength;
            canvas.drawLine(x, 0, x, height, borderPaint);
        }

        // 密码
        float cx, cy = height/ 2;
        float half = width / passwordLength / 2;
        for(int i = 0; i < textLength; i++) {
            cx = width * i / passwordLength + half;
            canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
        }
    }

    /**
     * 通过这个监听输入的改变来重新绘制View
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }
}