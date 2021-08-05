package com.example.myapplication.Canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.concurrent.atomic.AtomicInteger;

public class FurrowView extends View {

    private int id;
    private double depth;
    private double topWidth;
    private double middleWidth;
    private double bottomWidth;
    private Integer leftMargin;
    private Integer rightMargin;
    private Integer topMargin;
    private Integer bottomMargin;

    private Paint paint=new Paint();

    private static final AtomicInteger ID_Generator=new AtomicInteger(0);

    public FurrowView(Context context) {
        super(context);
        id=ID_Generator.getAndIncrement();
    }

    public FurrowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FurrowView);
        typedArray.recycle();
    }

    @Override
    public int getId() {
        return id;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getTopWidth() {
        return topWidth;
    }

    public void setTopWidth(double topWidth) {
        this.topWidth = topWidth;
    }

    public double getMiddleWidth() {
        return middleWidth;
    }

    public void setMiddleWidth(double middleWidth) {
        this.middleWidth = middleWidth;
    }

    public double getBottomWidth() {
        return bottomWidth;
    }

    public void setBottomWidth(double bottomWidth) {
        this.bottomWidth = bottomWidth;
    }

    public Integer getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Integer leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Integer getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Integer rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Integer getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Integer topMargin) {
        this.topMargin = topMargin;
    }

    public Integer getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Integer bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(leftMargin,topMargin,rightMargin,bottomMargin,paint);
    }
}
