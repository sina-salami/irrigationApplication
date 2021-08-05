package com.example.myapplication.Canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.concurrent.atomic.AtomicInteger;

public class BorderView extends View {

    private int id;
    private double depth;
    private double topWidth;
    private double middleWidth;
    private double bottomWidth;

    Point startPoint =new Point();
    Point endPoint =new Point();

    private Paint paint=new Paint();

    private static final AtomicInteger ID_Generator=new AtomicInteger(0);
    public BorderView(Context context) {
        super(context);
        id=ID_Generator.getAndIncrement();
    }

    public BorderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BasinView);
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

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        //canvas.drawLine(10,10,10,100,paint);
        canvas.drawLine(startPoint.x,startPoint.y,endPoint.x,endPoint.y,paint);
    }
}
