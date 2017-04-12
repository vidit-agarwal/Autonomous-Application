package com.hugeardor.vidit.autonomous;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vidit on 25/3/17.
 */

public class splash extends Activity {


    TextView tv;
    ImageView iv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        tv = (TextView)findViewById(R.id.tv) ;
       // iv = (ImageView)findViewById(R.id.iv);
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        //height = size.y;
        final float centerY= size.y/2;
        final float centerX = size.x/2;


        Thread timer= new Thread()
        {
            public void run()
            {
                try{

                    TranslateAnimation img = new TranslateAnimation(0.0f,0.0f,-100.0f,  centerY-130.00f );
                    img.setDuration(3000); // animation duration
                    img.setRepeatCount(0); // animation repeat count
                    img.setInterpolator(new AccelerateDecelerateInterpolator());
                    img.setFillAfter(true);
                    iv.startAnimation(img);

                    TranslateAnimation animation = new TranslateAnimation(-150.0f, centerX-155.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)

                    animation.setDuration(3000); // animation duration
                    animation.setRepeatCount(0); // animation repeat count
                    animation.setFillAfter(true);
                    tv.startAnimation(animation);//your_view for mine is imageView
                    sleep(4000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally{
                    Intent intent =new Intent(splash.this , MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();



        //super.onPause();


    }
    protected void onPause()
    {
        super.onPause();
        splash.this.finish();
    }
}
