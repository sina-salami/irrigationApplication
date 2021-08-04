package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.myapplication.Canvas.FarmLandView;
import com.example.myapplication.Canvas.WaterInputSrcView;
import com.example.myapplication.Canvas.WaterOutPutView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int IMAGE_HEIGHT_IN_PX = 1000;
    private static final int IMAGE_WIDTH_IN_PX = 1000;
    private static final int MINIMUM_IMAGE_PADDING_IN_PX = 50;
    private static final double QUARTERPI = Math.PI / 4;

    private FarmLandView farmLand;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        farmLand = new FarmLandView(this);
        WaterInputSrcView waterInputSrcViewInput = new WaterInputSrcView(this);
        waterInputSrcViewInput.setLatitude(100);
        waterInputSrcViewInput.setLongitude(100);
        WaterOutPutView waterOutSrcViewOutput = new WaterOutPutView(this);
        waterOutSrcViewOutput.setLatitude(800);
        waterOutSrcViewOutput.setLatitude(800);
        List<Double> lt = new ArrayList<>();
        List<Double> ln = new ArrayList<>();
        lt.add(36.39776);
        lt.add(36.41434);
        lt.add(36.40909);
        lt.add(36.39666);
        lt.add(36.39465);
        ln.add(54.9437);
        ln.add(54.95786);
        ln.add(54.97529);
        ln.add(54.96567);
        ln.add(54.94945);


       // farmLand.setPoints();

        List<Integer[]> a = convertLatLongToXY(lt, ln);
        a.size();

    List<Point>pointList=new ArrayList<>();
        for (int i=0;i<a.size(); i++) {
            Point point=new Point(a.get(i)[0],a.get(i)[1]);
           pointList.add(point);
        }
       farmLand.setPointList(pointList);

        //add farmLand to activity
        RelativeLayout relativeLayout = findViewById(R.id.mainLayout);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_LEFT,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        params.leftMargin=10;
        relativeLayout.addView(farmLand,params);

        RelativeLayout.LayoutParams params1=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_LEFT,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        params.leftMargin=10;
        relativeLayout.addView(waterInputSrcViewInput,params1);

        RelativeLayout.LayoutParams params2=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_LEFT,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        params.leftMargin=10;
        relativeLayout.addView(waterOutSrcViewOutput,params2);


    }


    public List<Integer[]> convertLatLongToXY(List<Double> lt, List<Double> ln) {
        // min and max coordinates, used in the computation below
        Double[] minXY = {-1.0, -1.0};
        Double[] maxXY = {-1.0, -1.0};
        List<Double[]> xy = new ArrayList<Double[]>();
        List<Integer[]> adjXY = new ArrayList<Integer[]>();
        for (int i = 0; i < lt.size(); i++) {

            // convert to radian
            double longitude = ln.get(i) * Math.PI / 180;
            double latitude = lt.get(i) * Math.PI / 180;
            xy.add(new Double[]{longitude, Math.log(Math.tan(QUARTERPI + 0.5 * latitude))});
            // The reason we need to determine the min X and Y values is because in order to draw the map,
            // we need to offset the position so that there will be no negative X and Y values
            minXY[0] = (minXY[0] == -1) ? xy.get(i)[0] : Math.min(minXY[0], xy.get(i)[0]);
            minXY[1] = (minXY[1] == -1) ? xy.get(i)[1] : Math.min(minXY[1], xy.get(i)[1]);

        }
        // readjust coordinate to ensure there are no negative values

        for (int j = 0; j < xy.size(); j++) {

            xy.get(j)[0] = xy.get(j)[0] - minXY[0];
            xy.get(j)[1] = xy.get(j)[1] - minXY[1];
            // now, we need to keep track the max X and Y values
            maxXY[0] = (maxXY[0] == -1) ? xy.get(j)[0] : Math.max(maxXY[0], xy.get(j)[0]);
            maxXY[1] = (maxXY[1] == -1) ? xy.get(j)[1] : Math.max(maxXY[1], xy.get(j)[1]);
        }
        int paddingBothSides = MINIMUM_IMAGE_PADDING_IN_PX * 2;

        // the actual drawing space for the map on the image
        int mapWidth = IMAGE_WIDTH_IN_PX - paddingBothSides;
        int mapHeight = IMAGE_HEIGHT_IN_PX - paddingBothSides;

        // determine the width and height ratio because we need to magnify the map to fit into the given image dimension
        double mapWidthRatio = mapWidth / maxXY[0];
        double mapHeightRatio = mapHeight / maxXY[1];

        // using different ratios for width and height will cause the map to be stretched. So, we have to determine
        // the global ratio that will perfectly fit into the given image dimension
        double globalRatio = Math.min(mapWidthRatio, mapHeightRatio);

        // now we need to readjust the padding to ensure the map is always drawn on the center of the given image dimension
        double heightPadding = (IMAGE_HEIGHT_IN_PX - (globalRatio * maxXY[1])) / 2;
        double widthPadding = (IMAGE_WIDTH_IN_PX - (globalRatio * maxXY[0])) / 2;

        // for each country, draw the boundary using polygon
        for (int k = 0; k < xy.size(); k++) {
            int adjustedX = (int) (widthPadding + (xy.get(k)[0] * globalRatio));
            // need to invert the Y since 0,0 starts at top left
            //int adjustedY = (int) (IMAGE_HEIGHT_IN_PX - heightPadding - (xy.get(k)[1] * globalRatio));
            int adjustedY = (int) (IMAGE_HEIGHT_IN_PX - heightPadding - (xy.get(k)[1] * globalRatio));
            adjXY.add(new Integer[]{adjustedY, adjustedX});
        }
        return adjXY;
    }
}

