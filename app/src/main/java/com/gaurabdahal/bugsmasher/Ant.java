//Gaurab Dahal

package com.gaurabdahal.bugsmasher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Ant {

    // States of a Bug
    enum BugState {
        Dead,
        ComingBackToLife,
        Alive, 			    // in the game
        DrawDead,			// draw dead body on screen
    };

    BugState state;			// current state of bug
    int x,y;				// location on screen (in screen coordinates)
    double speed;			// speed of bug (in pixels per second)
    // All times are in seconds
    float timeToBirth;		// # seconds till birth
    float startBirthTimer;	// starting timestamp when decide to be born
    float deathTime;		// time of death
    float animateTimer;		// used to move and animate the bug

    boolean moveRight;
    int rand;
    boolean isBig;
    int killClicked=0;
    // Bug starts not alive
    public Ant(boolean isBig) {
        this.isBig=isBig;
        state = BugState.Dead;
        moveRight=true;
        rand = new Random().nextInt(8)+1;
        speed = new Random().nextInt(5)+1;
    }

    // Bug birth processing
    public void birth (Canvas canvas) {
        if(Assets.isplaying) {
            // Bring a bug to life?
            if (state == BugState.Dead) {
                // Set it to coming alive
                state = BugState.ComingBackToLife;
                // Set a random number of seconds before it comes to life
                if(isBig){
                    timeToBirth = (float) Math.random() * 25;
                }else {
                    timeToBirth = (float) Math.random() * 5;
                }
                // Note the current time
                startBirthTimer = System.nanoTime() / 1000000000f;
            }
            // Check if bug is alive yet
            else if (state == BugState.ComingBackToLife) {
                float curTime = System.nanoTime() / 1000000000f;
                // Has birth timer expired?
                if (curTime - startBirthTimer >= timeToBirth) {
                    // If so, then bring bug to life
                    state = BugState.Alive;
                    // Set bug starting location at top of screen
                    x = (int) (Math.random() * canvas.getWidth());
                    // Keep entire bug on screen
                    if (x < Assets.ant1.getWidth() / 2)
                        x = Assets.ant1.getWidth() / 2;
                    else if (x > canvas.getWidth() - Assets.ant1.getWidth() / 2)
                        x = canvas.getWidth() - Assets.ant1.getWidth() / 2;
                    y = 0;
                    // Set speed of this bug
                    speed = canvas.getHeight() / 4; // no faster than 1/4 a screen per second
                    // subtract a random amount off of this so some bugs are a little slower
                    // ADD CODE HERE
                    // Record timestamp of this bug being born
                    animateTimer = curTime;
                }
            }
        }
    }

    // Bug movement processing
    public void move (Canvas canvas) {
        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Get elapsed time since last call here
            float curTime = System.nanoTime() / 1000000000f;
            float elapsedTime = curTime - animateTimer;
            animateTimer = curTime;
            // Compute the amount of pixels to move (vertically down the screen)

            // Draw bug on screen
            long time = System.currentTimeMillis() / 100 % 10;
            Bitmap temp1,temp2;
            if(isBig){
                temp1 = Assets.m_ant1;
                temp2 = Assets.m_ant2;
            }else{
                temp1 = Assets.ant1;
                temp2 = Assets.ant2;
            }


            //canvas.drawBitmap(Assets.ant1, x-Assets.ant1.getWidth()/2,  y-Assets.ant1.getHeight()/2, null);
            // ADD CODE HERE - Draw each frame of animation as appropriate - don't just draw 1 frame





//---+
            if(Assets.isplaying) {
                if (time % 2 == 0)
                    canvas.drawBitmap(temp1, x-temp1.getWidth()/2, y-temp1.getHeight()/2, null);
                else
                    canvas.drawBitmap(temp2, x-temp2.getWidth()/2, y-temp2.getHeight()/2, null);

                y += (speed * elapsedTime);
                if (x > canvas.getWidth() - Assets.ant1.getWidth()) {
                    moveRight = false;
                }
                if (x + Assets.ant1.getWidth() < Assets.ant1.getWidth()) {
                    moveRight = true;
                }

                if (y % rand == 0) {
                    if (moveRight) {
                        x = (x + 3);
                    } else {
                        x = (x - 3);
                    }
                }
            }else{
                canvas.drawBitmap(temp2, x-temp2.getWidth()/2, y-temp2.getHeight()/2, null);
            }

//---+
            // Has it reached the bottom of the screen?
            if (y >= canvas.getHeight()) {
                // Kill the bug
                state = BugState.Dead;
                //---+
                rand = new Random().nextInt(8)+1;
                speed = new Random().nextInt(5)+1;
                //---+
                // Subtract 1 life
                Assets.livesLeft--;
            }
        }
    }

    // Process touch to see if kills bug - return true if bug killed
    public boolean touched (Canvas canvas, int touchx, int touchy) {
        boolean touched = false;
        // Make sure this bug is alive
        if (state == BugState.Alive) {
            // Compute distance between touch and center of bug
            float dis = (float)(Math.sqrt ((touchx - x) * (touchx - x) + (touchy - y) * (touchy - y)));
            // Is this close enough for a kill?
            if (dis <= Assets.ant1.getWidth()*1f) {
                touched = true;
                // Record time of death
            }
        }
        if(touched){
            if(isBig){
                	// need to draw dead body on screen for a while

                killClicked++;
                if(killClicked==4){
                    killClicked=0;
                    Assets.score=Assets.score+10;
                    state = BugState.DrawDead;
                    deathTime = System.nanoTime() / 1000000000f;
                    return true;
                }else{
                    return false;
                }
            }else{
                Assets.score++;
                state = BugState.DrawDead;
                deathTime = System.nanoTime() / 1000000000f;
            }
        }
        return (touched);
    }

    // Draw dead bug body on screen, if needed
    public void drawDead (Canvas canvas) {
        if (state == BugState.DrawDead) {
            Bitmap temp;
            if(isBig){
                temp = Assets.m_ant3;
            }else{
                temp = Assets.ant3;
            }

            if(Assets.isplaying) {
                canvas.drawBitmap(temp, x - temp.getWidth() / 2, y - temp.getHeight() / 2, null);
                // Get time since death
                float curTime = System.nanoTime() / 1000000000f;
                float timeSinceDeath = curTime - deathTime;
                // Drawn dead body long enough (4 seconds) ?
                if (timeSinceDeath > 4)
                    state = BugState.Dead;
            }else{
                canvas.drawBitmap(temp, x - temp.getWidth() / 2, y - temp.getHeight() / 2, null);
            }
        }
    }

}
