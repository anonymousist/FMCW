package com.example.administrator.emit_mfcw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    FMCW myFmcw;
    Recorder myRecorder;
    private final ThreadLocal<Button> Play_button = new ThreadLocal<>();
    private TextView Show_message;
    private SurfaceView mySurfaceView;
    private myView myViewer;
    private Bitmap bitmap;
    private ImageView imageView;
    private LinearLayout linlay;
    protected Canvas canvas;
    protected  Paint paint;
    protected double[] values = new  double[8192];
    protected double max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play_button.set((Button) findViewById(R.id.play_button));
        Button stop_button = (Button) findViewById(R.id.stop_button);
        Show_message = (TextView)findViewById(R.id.textView);
        //mySurfaceView =(SurfaceView)findViewById(R.id.SurfaceView);
        imageView = (ImageView)findViewById(R.id.imageView);
        linlay = (LinearLayout)findViewById(R.id.LinearLayout);
        myViewer = new myView(this);
        myViewer.setMinimumHeight(1000);
        myViewer.setMinimumWidth(1000);
        linlay.addView(myViewer);
       // setContentView(myViewer);
        /*bitmap = Bitmap.createBitmap(8192,4000, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        imageView.setImageBitmap(bitmap);
        canvas.drawCircle(4000, 2000, 500, paint);
        paint.setColor(Color.YELLOW);
        Thread draw  =  new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                for (int i = 0; i < 8192; i++) {
                    int x = i;
                    int downy = 4000;
                    int upy = 4000 - (int) (i / 8192.0 * 4000);
                    canvas.drawLine(x, downy, x, upy, paint);

                }
                }
            }
        });
        draw.start();
        */
        Play_button.get().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFmcw = new FMCW();
                myRecorder = new Recorder();
                myFmcw.play();
                myRecorder.startRecord();
                Play_button.get().setEnabled(false);

            }
        });
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFmcw.stop();
                myRecorder.stopRecord();
                Play_button.get().setEnabled(true);
            }
        });
    }
    /*protected void onProgressUpdate(double[] values,double max){
        canvas.drawColor(Color.BLACK);
        for (int i = 0; i < values.length; i++) {
            int x=i;
            int downy=4000;
            int upy= (int) (values[i]/max*4000);
            canvas.drawLine(x, downy, x, upy, paint);
        }
        imageView.invalidate();

    }*/
}
