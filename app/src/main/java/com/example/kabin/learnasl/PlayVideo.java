package com.example.kabin.learnasl;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

public class PlayVideo extends AppCompatActivity {

    private VideoView videoView;
    String gesture = null;
    String username = null;
    String filename;
    MediaController mediaController;
    Intent returnToScreen1;
    PlayVideo playVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        returnToScreen1 = new Intent();
        playVideo = this;
        videoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        gesture = intent.getStringExtra("gesture");
        username = intent.getStringExtra("username");

        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        initializePlayer(gesture);
    }

    @Override
    protected void onStop(){
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }

    private void initializePlayer(String gesture){
        Uri videoUri = getMedia(gesture);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    private Uri getMedia(String media) {
        return Uri.parse("android.resource://" + getPackageName() +
                "/raw/" + media.toLowerCase());

    }

    private void releasePlayer(){
        videoView.stopPlayback();
    }

    public void practice(View view){
        Toast.makeText(view.getContext(),"Called",Toast.LENGTH_LONG);
        Intent playIntent = new Intent(view.getContext(),PracticeActivity.class);
        playIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        playIntent.putExtra("gesture",gesture);
        playIntent.putExtra("username",username);
        startActivityForResult(playIntent, 9999);

    }

    public void saveVideo(View view){
        if (filename!=null){
            uploadVideo();
        }else{
            Toast.makeText(view.getContext(),"File is empty. Please record again",Toast.LENGTH_LONG).show();
        }
    }

    public void uploadVideo(){
        File file = new File(filename);
        RequestParams params = new RequestParams();
        params.put("accept",1);
        try {
            params.put("uploaded_file", file);
            params.put("id","1215098534");
            params.put("group_id","4");


        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.e("params",params.toString());

        AsyncHttpClient client = new AsyncHttpClient();

        client.post("http://192.168.0.213:80" +"/upload_video.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                if(statusCode==200) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    returnToScreen1.putExtra("username",username);
                    playVideo.setResult(3333,returnToScreen1);
                    playVideo.finish();

                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.e("Failed",statusCode+"");
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
            }


            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode==9999 && resultCode == 4455) {
            filename = intent.getStringExtra("file");
        }
    }
}
