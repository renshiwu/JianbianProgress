package com.hibabypsy.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
/**
 *测评结果水平进度条
 */
public class JianbianProgess extends View implements Runnable {


    Handler handler = new Handler(); // 用于延时更新，实现动画
    float animprogress; // 进度条动画高度

    /**
     * 控件的宽和高
     */
    int view_width;
    int view_height;

    Paint linePaint;
    float lineHeight = 20;//线的高度
    float lineWidht;//线的宽度

    /**
     * 默认都是20
     */
    float margin_left = 20;//进度条左边的间距
    float margin_right = 20;//进度条右边的间距

    /**
     * 游标刻度值
     */
    float curr_Value = (float) 0.0;//当前值
    float max_Value = 100;//最大值

    String title;

    public JianbianProgess(Context context) {
        super(context);

    }

    public JianbianProgess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JianbianProgess(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initView(canvas);
    }

    private void initView(Canvas canvas) {
        view_width = getWidth();
        view_height = getHeight();
        /**进度条的画笔*/
        if (linePaint == null) {
            linePaint = new Paint();
            Shader mShader = new LinearGradient(0, 0, canvas.getWidth(), canvas.getHeight(), new int[]{Color.parseColor("#ffc40d"), Color.parseColor("#3fdbd6"), Color.parseColor("#039dea")}, null, Shader.TileMode.CLAMP);
            linePaint.setColor(Color.parseColor("#039dea"));
            linePaint.setAntiAlias(true);
            linePaint.setShader(mShader);
        }
        int width = 100;
        int hight = 40;

        /**计算游标显示完整的位置*/
        if (margin_left < width) {
            margin_left = width / 2;
        }
        if (margin_right < width) {
            margin_right = width / 2;
        }
        /**居中显示进度条*/
        RectF rf = new RectF(margin_left, view_height - 2 * lineHeight, view_width - margin_right, view_height - lineHeight);
        lineWidht = rf.width();
        /*绘制圆角进度条，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, rf.height() / 2, rf.height() / 2, linePaint);//开始绘制

        /*图片游标在进度条上显示的位置*/
        if (curr_Value > max_Value) {
            curr_Value = max_Value;
        }
        float leftd = (margin_left - width / 2) + (animprogress * lineWidht / max_Value);
        RectF mDestRect = new RectF(leftd, (view_height - 5 * lineHeight), leftd + width, view_height - 3 * lineHeight);//图片所在区域
        canvas.drawRoundRect(mDestRect, 10, 10, linePaint);//开始绘制
        handler.postDelayed(this, 1);
        Path path = new Path();
        path.moveTo(leftd + width / 3, view_height - 3 * lineHeight);// 此点为多边形的起点
        path.lineTo(leftd + width / 6 * 4, view_height - 3 * lineHeight);
        path.lineTo(leftd + width / 2, (float) (Math.cbrt(3) / 2 * 1 / 6 * width + view_height - 3 * lineHeight));
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, linePaint);

        /**绘制游标顶部的文字*/

        Paint tv_piant = new Paint(Paint.ANTI_ALIAS_FLAG);
        tv_piant.setTextSize(30);
        tv_piant.setColor(Color.WHITE);
        tv_piant.setTextAlign(Paint.Align.LEFT);
        Rect tv_Rect = new Rect();
        if (null == title) {
            return;
        }
        tv_piant.getTextBounds(title, 0, title.length(), tv_Rect);

        canvas.drawText(title, leftd + width / (title.length() + 2), (view_height - 3 * lineHeight - lineHeight / 2), tv_piant);
    }


    public void settitle(String title) {
        this.title = title;
        invalidate();
    }

    public void setCurr_Value(float curr_Value) {
        this.curr_Value = curr_Value;
        invalidate();
    }

    @Override
    public void run() {
        animprogress += 1;
        if (animprogress >= curr_Value) {
            return;
        } else {
            invalidate();
        }
    }
}
