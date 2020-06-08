package com.example.kabin.learnasl;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class PracticeActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    Intent returnIntent;
    String outputFilepath;
    PracticeActivity practiceActivity;
    CountDownTimer timer;
    CountDownTimer time;

    private MediaRecorder mediaRecorder;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button startStopToggleBtn;
    private TextView tvCountdown;
    private TextView tvTime;
    private boolean aBoolean;

    String gestureFilename ="file";
    String gesture="gesture";
    String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        practiceActivity = this;
        returnIntent = new Intent();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        Intent prevIntent = getIntent();
        gesture = prevIntent.getStringExtra("gesture");
        username = prevIntent.getStringExtra("username");

        surfaceView = findViewById(R.id.surfaceViewCamera);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        tvCountdown = findViewById(R.id.textviewTimer);
        tvTime = findViewById(R.id.textViewTime);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        startStopToggleBtn = findViewById(R.id.btnRecord);
        time = new CountDownTimer(6000,1000) {
            @Override
            public void onTick(long l) {
                int a = (int) (l / 1000);
                tvTime.setText(a + " ");
            }

            @Override
            public void onFinish() {
                mediaRecorder.stop();
                mediaRecorder.reset();
                if(time!=null) {
                    time.cancel();
                }
                returnIntent.putExtra(gestureFilename, outputFilepath);
                practiceActivity.setResult(4455,returnIntent);
                practiceActivity.finish();
            }
        };
        startStopToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(final View v) {
                timer = new CountDownTimer(5000, 1000) {
                    public void onTick(long timeInMillis) {
                        int a = (int) (timeInMillis / 1000);
                        tvCountdown.setText(a + " ");
                        v.setEnabled(false);
                    }
                    public void onFinish() {
                        v.setEnabled(true);
                        tvCountdown.setVisibility(View.GONE);
                        ((Button) v).setText("STOP");
                        mediaRecorder.start();
                        time.start();
                    }
                };
                if (((Button) v).getText().toString().equals("START")) {
                    timer.start();
                }
                else if (((Button) v).getText().toString().equals("STOP")) {
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    ((Button) v).setText("START");
                    if(time!=null) {
                        time.cancel();
                    }
                    returnIntent.putExtra(gestureFilename, outputFilepath);
                    practiceActivity.setResult(4455,returnIntent);
                    practiceActivity.finish();

                }
            }
        });
    }

    boolean fileCreationSuccess = false;
    private void beginRecording(Surface surface) throws IOException {
        if(camera == null) {
            camera = Camera.open(1);
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            camera.unlock();
        }

        if(mediaRecorder == null)
            mediaRecorder = new MediaRecorder();
        mediaRecorder.setPreviewDisplay(surface);
        mediaRecorder.setCamera(camera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        
        int fileCount=0;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/LearnASL/"
                +gesture+"_PRACTICE_"+fileCount +"_" +username+ ".mp4");

        while(true){
            if(file.exists()){
                fileCount++;
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/LearnASL/"
                        +gesture+"_PRACTICE_"+fileCount+"_"+username+ ".mp4");
            }
            else{
                break;
            }
        }

        if(file.createNewFile()) {
            fileCreationSuccess = true;
            outputFilepath = file.getPath();
        }

        mediaRecorder.setOutputFile(file.getPath());
        mediaRecorder.setMaxDuration(15000);
        mediaRecorder.setVideoSize(320,240);
        mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {    //finish after max duration has been reached
                    PracticeActivity.this.mediaRecorder.stop();
                    PracticeActivity.this.mediaRecorder.reset();
                    if(time!=null) {
                        time.cancel();
                    }
                    returnIntent.putExtra(gestureFilename, outputFilepath);
                    practiceActivity.setResult(4455,returnIntent);
                    practiceActivity.finish();
                }

            }
        });

        mediaRecorder.setOrientationHint(270);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        try {

            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        aBoolean = true;
    }


    @Override
    public void onBackPressed() {
        if(timer!=null)
            timer.cancel();
        if(time!=null)
            time.cancel();

        returnIntent.putExtra(gestureFilename, outputFilepath);
        practiceActivity.setResult(2233,returnIntent);
        practiceActivity.finish();

        super.onBackPressed();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if(!aBoolean)
                beginRecording(surfaceHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroy();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {}

    private void destroy() {
        mediaRecorder.reset();
        mediaRecorder.release();
        camera.release();
        returnIntent.putExtra(gestureFilename, outputFilepath);
        practiceActivity.setResult(2233,returnIntent);
        camera = null;
        mediaRecorder = null;
        timer.cancel();
        finish();
    }

}