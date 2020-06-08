package com.example.kabin.learnasl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        createStorageDir();
    }

    public void createStorageDir(){
        File f = new File(Environment.getExternalStorageDirectory(), "LearnASL");

        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public void begin(View view){
        EditText editText = findViewById(R.id.userName);
        String userName = editText.getText().toString().replaceAll(" ","");

       if (userName.equals("")){
           Toast t = Toast.makeText(getApplicationContext(),"Enter user name", Toast.LENGTH_LONG);
           t.show();
       }
       else{
           Intent intent = new Intent(view.getContext(),ShowVideo.class);
           intent.putExtra("username",userName);
           startActivity(intent);
       }
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }
}
