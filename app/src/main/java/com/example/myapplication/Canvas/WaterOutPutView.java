package com.example.myapplication.Canvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class WaterOutPutView extends View {

    private Integer latitude;
    private Integer longitude;
    Paint paint = new Paint();

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }
    public WaterOutPutView(Context context) {
        super(context);
    }

    public WaterOutPutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.WaterOutPutSrcView);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        canvas.drawCircle(latitude, longitude, 20, paint);
    }
}
