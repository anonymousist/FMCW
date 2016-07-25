package com.example.administrator.emit_mfcw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class myView extends View {
    protected double[] pathArry = new double[2048];

    public myView(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas){
        for (int i=0;i<2048;i++){
            pathArry[i] = i;
        }
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(5);
        canvas.drawColor(Color.BLACK);
       // canvas.drawLine(0,0,500,1000,paint);
       // canvas.drawCircle(500,1000,100,paint);
        for (int i = 0; i < pathArry.length; i++) {
            int x = i;
            int downy = 2000;
            int upy = 1500 - (int) (i /2048.0 *1500);
            canvas.drawLine(x, downy, x, upy, paint);

        }
        super.onDraw(canvas);
    }
    protected boolean pathData(double[] FFTsignal){
        pathArry = new double[2048];
        double tem = 0;
        int j =0;
        for(int i =0;i<4096;i++){
            tem = Math.abs(tem+FFTsignal[i]);
            if((i+1)%2==0){
                pathArry[j++] = tem/2;
                tem = 0;
            }
        }
        invalidate();
        return true;
    }

}
