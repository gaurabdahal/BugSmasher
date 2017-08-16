//Gaurab Dahal
package com.gaurabdahal.bugsmasher;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.SoundPool;


public class Assets {
    static Bitmap background;
    static Bitmap foodbar;
    static Bitmap ant1;
    static Bitmap ant2;
    static Bitmap ant3;

    static Bitmap m_ant1;
    static Bitmap m_ant2;
    static Bitmap m_ant3;

    static Bitmap life;
    static Bitmap gameplay,gamepause;

    static int score=0;
    static boolean isplaying=true;


    // States of the Game Screen
    enum GameState {
        GettingReady,	// play "get ready" sound and start timer, goto next state
        Starting,		// when 3 seconds have elapsed, goto next state
        Running, 		// play the game, when livesLeft == 0 goto next state
        GameEnding,	    // show game over message
        GameOver,		// game is over, wait for any Touch and go back to title activity screen
    };
    static GameState state;		// current state of the game
    static float gameTimer;	    // in seconds
    static int livesLeft;		// 0-3

    static MediaPlayer mp;
    static SoundPool soundPool;
    static int sound_getready;
    static int sound_gameover;
    static int[] sound_squish = new int[4];
    static int sound_thump;
    static int sound_clap;

    static Ant[] bug=new Ant[5]; // try using an array of bugs instead of only 1 bug (so you can have more than 1 on screen at a time)
}
