package com.example.administrator.emit_mfcw;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    FMCW myFmcw;
    Recorder myRecorder;
    private final ThreadLocal<Button> Play_button = new ThreadLocal<>();
    private TextView Show_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Play_button.set((Button) findViewById(R.id.play_button));
        Button stop_button = (Button) findViewById(R.id.stop_button);
        Show_message = (TextView)findViewById(R.id.textView);
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
}
