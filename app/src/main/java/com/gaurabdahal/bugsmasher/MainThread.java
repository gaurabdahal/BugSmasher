//Gaurab Dahal
package com.gaurabdahal.bugsmasher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MainThread extends Thread {
    private SurfaceHolder holder;
    private Handler handler;		// required for running code in the UI thread
    private boolean isRunning = false;
    Context context;
    Paint paint;
    int touchx, touchy;	// x,y of touch event
    boolean touched;	// true if touch happened
    boolean data_initialized;
    private static final Object lock = new Object();

    public MainThread (SurfaceHolder surfaceHolder, Context context) {
        holder = surfaceHolder;
        this.context = context;
        handler = new Handler();
        data_initialized = false;
        touched = false;
    }

    public void setRunning(boolean b) {
        isRunning = b;	// no need to synchronize this since this is the only line of code to writes this variable
    }

    // Set the touch event x,y location and flag indicating a touch has happened
    public void setXY (int x, int y) {
        synchronized (lock) {
            touchx = x;
            touchy = y;
            this.touched = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // Lock the canvas before drawing
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                // Perform drawing operations on the canvas
                //for(int i=0;i<5;i++)
                render(canvas);
                // After drawing, unlock the canvas and display it
                holder.unlockCanvasAndPost (canvas);
            }
        }
    }

    // Loads graphics, etc. used in game
    private void loadData (Canvas canvas) {
        Bitmap bmp;
        int newWidth, newHeight;
        float scaleFactor;

        // Create a paint object for drawing vector graphics
        paint = new Paint();

        // Load score bar
        // ADD CODE HERE

        // Load food bar
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.food);
        // Compute size of bitmap needed (suppose want height = 10% of screen height)
        newHeight = (int)(canvas.getHeight() * 0.1f);
        // Scale it to a new size
        Assets.foodbar = Bitmap.createScaledBitmap(bmp, canvas.getWidth(), newHeight, false);
        // Delete the original
        bmp = null;

        // Load ant 1
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant1);
        newWidth = (int)(canvas.getWidth() * 0.1f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.ant1 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;

        // Load the other bitmaps similarly
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.ant2);
        Assets.ant2 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp=null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.dead);
        newWidth = (int)(canvas.getWidth() * 0.1f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.ant3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        //-----------
        // Load ant 1
        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.m_ant1);
        newWidth = (int)(canvas.getWidth() * 0.3f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.m_ant1 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;

        // Load the other bitmaps similarly
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.m_ant2);
        Assets.m_ant2 = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp=null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.dead);
        newWidth = (int)(canvas.getWidth() * 0.2f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.m_ant3 = Bitmap.createScaledBitmap (bmp, newWidth, newHeight, false);
        bmp = null;

        bmp = BitmapFactory.decodeResource (context.getResources(), R.drawable.life);
        newWidth = (int)(canvas.getWidth() * 0.1f);
        scaleFactor = (float)newWidth / bmp.getWidth();
        newHeight = (int)(bmp.getHeight() * scaleFactor);
        Assets.life = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp = null;

        bmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.gameplay);
        Assets.gameplay = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp=null;

        bmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.gamepause);
        Assets.gamepause = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        //Assets.life

        // Create a bug
        boolean big=true;
        Assets.bug[0] = new Ant(!big);
        Assets.bug[1] = new Ant(!big);
        Assets.bug[2] = new Ant(!big);
        Assets.bug[3] = new Ant(!big);
        Assets.bug[4] = new Ant(big);
    }

    // Load specific background screen
    private void loadBackground (Canvas canvas, int resId) {
        // Load background
        Bitmap bmp = BitmapFactory.decodeResource (context.getResources(), resId);
        // Scale it to fill entire canvas
        Assets.background = Bitmap.createScaledBitmap (bmp, canvas.getWidth(), canvas.getHeight(), false);
        // Delete the original
        bmp = null;
    }

    private void render (Canvas canvas) {
        int i, x, y;

        if (! data_initialized) {
            loadData(canvas);
            data_initialized = true;
        }

        switch (Assets.state) {
            case GettingReady:
                loadBackground (canvas, R.drawable.bg);
                // Draw the background screen
                canvas.drawBitmap (Assets.background, 0, 0, null);
                // Play a sound effect
                Assets.soundPool.play(Assets.sound_getready, 1, 1, 1, 0, 1);
                // Start a timer
                Assets.gameTimer = System.nanoTime() / 1000000000f;
                // Go to next state
                Assets.state = Assets.GameState.Starting;
                break;
            case Starting:
                // Draw the background screen
                canvas.drawBitmap (Assets.background, 0, 0, null);
                // Has 3 seconds elapsed?
                float currentTime = System.nanoTime() / 1000000000f;
                if (currentTime - Assets.gameTimer >= 3)
                    // Goto next state
                    Assets.state = Assets.GameState.Running;
                else{
                    if(currentTime - Assets.gameTimer >2){
                        displayGetReady(canvas,"Get Ready...");
                    }else if(currentTime - Assets.gameTimer >1){
                        displayGetReady(canvas,"Get Ready..");
                    }else if(currentTime - Assets.gameTimer >0){
                        displayGetReady(canvas,"Get Ready.");
                    }
                }
                break;
            case Running:
                // Draw the background screen
                canvas.drawBitmap (Assets.background, 0, 0, null);
                // Draw the score bar at top of screen
                String text = "Score : " + Assets.score;
                displayScore(canvas, text);


                // Draw the foodbar at bottom of screen
                canvas.drawBitmap (Assets.foodbar, 0, canvas.getHeight()-Assets.foodbar.getHeight(), null);

                int radius = (int)(canvas.getWidth() * 0.05f);
                int spacing = 8; // spacing in between circles
                x = canvas.getWidth() - radius - spacing-radius;	// coordinates for rightmost circle to draw
                y = spacing;
                for (i=0; i<Assets.livesLeft; i++) {
                    canvas.drawBitmap(Assets.life, x, y, null);
                    x -= (radius*2 + spacing);
                }

                displayPauseIcon(canvas);

                // Process a touch
                if (touched) {

                    checkPauseTouched(canvas, touchx, touchy);
                    // Set touch flag to false since we are processing this touch now
                    touched = false;
                    boolean bugKilled=false;
                    int squished_bugindex=0;
                    // See if this touch killed a bug
                    if(Assets.isplaying) {
                        for (int n = 0; n < Assets.bug.length; n++) {
                            //Log.i("hehe", "n=" + n);
                            if (Assets.bug[n].touched(canvas, touchx, touchy)) {
                                bugKilled = true;
                                squished_bugindex = n;
                            }
                            //break;
                        }
                    }
                    if (bugKilled)
                        if(squished_bugindex<4){
                            Assets.soundPool.play(Assets.sound_squish[squished_bugindex%3], 1, 1, 1, 0, 1);
                        }else{
                            Assets.soundPool.play(Assets.sound_squish[3], 1, 1, 1, 0, 1);
                        }

                    else
                        Assets.soundPool.play(Assets.sound_thump, 1, 1, 1, 0, 1);
                }

                for(int n=0;n<Assets.bug.length;n++) {
                    // Draw dead bugs on screen
                    Assets.bug[n].drawDead(canvas);
                    // Move bugs on screen
                        Assets.bug[n].move(canvas);
                    // Bring a dead bug to life?
                    Assets.bug[n].birth(canvas);
                }
                // ADD MORE CODE HERE TO PLAY GAME


                // Are no lives left?
                if (Assets.livesLeft == 0)
                    // Goto next state
                    Assets.state = Assets.GameState.GameEnding;
                break;
            case GameEnding:

                saveHighScore();

                // Show a game over message
                handler.post(new Runnable() {
                    public void run() {
                       // Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Assets.soundPool
                                .play(Assets.sound_gameover, 1, 1, 1, 0, 1);
                    }
                });
                // Goto next state
                Assets.state = Assets.GameState.GameOver;
                break;
            case GameOver:
                // Fill the entire canvas' bitmap with 'black'
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(160,0,0));
                paint.setTextSize(90);
                canvas.drawText("GAME OVER", 25, canvas.getHeight() / 2, paint);
                paint.setColor(Color.rgb(8, 8, 8));
                paint.setTextSize(25);
                canvas.drawText("Press back button to go to main screen", 50, canvas.getHeight() / 2 + 60, paint);
                break;
        }
    }


public void displayScore(Canvas canvas,String text){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(34, 98, 52));
        paint.setTextSize(40);
        canvas.drawText(text, 20, 50, paint);
        }

    public void displayGetReady(Canvas canvas,String text){
        Paint paint = new Paint();
        paint.setColor(Color.rgb(34, 98, 52));
        paint.setTextSize(90);
        canvas.drawText(text, 30, canvas.getHeight() / 2, paint);
    }

    public void displayPauseIcon(Canvas canvas){
        Bitmap icon;
        if(Assets.isplaying){
            icon = Assets.gamepause;
        }else{
            icon = Assets.gameplay;
        }

        canvas.drawBitmap(icon, canvas.getWidth() / 2 - (icon.getWidth() / 2), 8, null);

    }

    public boolean checkPauseTouched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;
        // Make sure this bug is alive
            // Compute distance between touch and center of bug
        int x = canvas.getWidth() / 2 - (Assets.gamepause.getWidth() / 2);
        int y=50;
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.gamepause.getWidth()*1f) {

                touched = true;


            }

        if(touched){
            Assets.isplaying = !Assets.isplaying;
        }

        if(Assets.isplaying){
            Assets.mp.setVolume(1, 1);
        }else{
            Assets.mp.setVolume(0, 0);
        }
    return touched;
    }

    public void saveHighScore(){
        SharedPreferences sprefs = PreferenceManager.getDefaultSharedPreferences(context);

        int highscore = sprefs.getInt("highscore", 0);

        if (highscore < Assets.score) {
            SharedPreferences.Editor editor = sprefs.edit();
            editor.putInt("highscore", Assets.score);
            editor.commit();
            // Assets.highscore=Assets.score;
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context, "New High Score: " + Assets.score,
                            Toast.LENGTH_LONG).show();
                    Assets.soundPool
                            .play(Assets.sound_clap, 1, 1, 1, 0, 1);
                }
            });

        }
    }
}
