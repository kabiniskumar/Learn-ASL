package com.example.kabin.learnasl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ShowVideo extends AppCompatActivity {
    Intent showvideo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        showvideo = new Intent(this,PlayVideo.class);
        Spinner spinner = findViewById(R.id.gesture_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gesture_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String gesture = parent.getItemAtPosition(position).toString();
                        showvideo.putExtra("gesture",gesture);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                }
        );
    }



    public void go_to_playvideo(View view){

        showvideo.putExtra("username",getIntent().getStringExtra("username"));
        startActivityForResult(showvideo, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
       // do nothing
    }
}
