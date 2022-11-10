package com.ishtiaq.tehkhanaykaraaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    static int splash_scr = 3000;

    Animation top, bottom;
    ImageView main_ico;
    TextView app_name, app_by, app_auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        main_ico = findViewById(R.id.app_ico);
        app_name = findViewById(R.id.app_name);
        app_by = findViewById(R.id.app_by);
        app_auth = findViewById(R.id.app_auth);

        main_ico.setAnimation(top);
        app_name.setAnimation(bottom);
        app_by.setAnimation(bottom);
        app_auth.setAnimation(bottom);

        new Handler().postDelayed(() -> {


                Intent intent = new Intent(MainActivity.this, MainNovel.class);
                startActivity(intent);
                finish();
            }, splash_scr);

    }
}