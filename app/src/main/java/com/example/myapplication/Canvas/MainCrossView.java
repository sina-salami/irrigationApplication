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

public class MainCrossView extends View {

    Paint paint =new Paint();
    Paint textPaint =new Paint();
    private String value;

    public MainCrossView(Context context) {
        super(context);

    }

    public MainCrossView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainCrossView);
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
        canvas.drawLine(10,50,60,5,paint);
        canvas.drawLine(60,5,110,50,paint);
        canvas.drawLine(60,5,60,120,paint);
        canvas.drawText(value+",",55,240,textPaint);
        //canvas.drawText(value,50,150,textPaint);
    }
}
