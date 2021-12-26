package com.grocery.food.Activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.grocery.food.R;

public class AboutsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);

        ImageView sled = findViewById(R.id.sledsow);
        AnimationDrawable sow = (AnimationDrawable) sled.getDrawable();
        sow.start();
    }
}
