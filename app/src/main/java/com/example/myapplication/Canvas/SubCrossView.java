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

public class SubCrossView extends View {

    Paint paint =new Paint();
    Paint textPaint =new Paint();
    private String value;
    public SubCrossView(Context context) {
        super(context);
    }

    public SubCrossView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SubCrossView);
        typedArray.recycle();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
        textPaint.setTextSize(25);
        textPaint.setColor(Color.BLUE);
        canvas.drawLine(70,150,175,150,paint);
        canvas.drawLine(125,100,175,150,paint);
        canvas.drawLine(175,150,125,195,paint);
        canvas.drawText(value,90,240,textPaint);

    }
}
