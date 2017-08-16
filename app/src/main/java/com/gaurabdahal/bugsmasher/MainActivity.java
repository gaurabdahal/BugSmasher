//Gaurab Dahal
package com.gaurabdahal.bugsmasher;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    MainView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Disable the title
        //requestWindowFeature (Window.FEATURE_NO_TITLE);  // use the styles.xml file to set no title bar
        // Make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Start the view
        v = new MainView(this);
        setContentView(v);
    }

    @Override
    protected void onPause () {
        super.onPause();
        v.pause();

        if( Assets.mp != null) {
            Assets.mp.pause();
            Assets.mp.release();
            Assets.mp = null;
        }
    }

    @Override
    protected void onResume () {

        super.onResume();
        v.resume();

        if( Assets.mp != null){
            Assets.mp.start();
        }else{
            Assets.mp = MediaPlayer.create(this, R.raw.bgmusic);
            Assets.mp.setLooping(true);
            Assets.mp.start();
            //Assets.mp.setVolume(0.3f, 0.3f);
        }
    }
}
