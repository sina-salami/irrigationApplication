package com.example.myapplication.Canvas;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class FarmLandView extends View {

    Paint paint = new Paint();
    Path path = new Path();
    private List<Point> pointList =new ArrayList<>();


    public FarmLandView(Context context) {
        super(context);
        init();
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
        init();
    }

    private void init() {
        if (pointList!=null && pointList.size()!=0){
            path.moveTo(pointList.get(0).x,pointList.get(0).y);
            for (int i = 1; i < pointList.size(); i++) {
                path.lineTo(pointList.get(i).x,pointList.get(i).y);
            }
            path.lineTo(pointList.get(0).x,pointList.get(0).y);
        }

  /*      path.moveTo(50, 50);
        path.lineTo(50, 500);
        path.lineTo(200, 500);
        path.lineTo(200, 300);
        path.lineTo(350, 300);*/
    }


    public FarmLandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FarmLandView);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(R.color.black));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        // canvas.drawLine(100,200,200,500,paint);
       /* for(int i=0;i<points.size()-1;i++){
            canvas.drawLine(points.get(i).x,points.get(i).y,points.get(i+1).x,points.get(i+1).y,paint);
        }*/

    }
}
