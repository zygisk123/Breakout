package com.example.breakoutgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int score;
    private static int hurtSound;
    private static int paddleHit;
    private static int powerUp;
    private static int wallHit;

    public SoundPlayer(Context context){
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        score = soundPool.load(context, R.raw.score, 1);
        hurtSound = soundPool.load(context, R.raw.hurt, 1);
        paddleHit = soundPool.load(context, R.raw.paddle_hit, 1);
        powerUp = soundPool.load(context, R.raw.powerup, 1);
        wallHit = soundPool.load(context, R.raw.wall_hit, 1);
    }

    public void playScoreSound(){
        soundPool.play(score, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playHurtSound(){
        soundPool.play(hurtSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playPaddleHit(){
        soundPool.play(paddleHit, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playPowerUp(){
        soundPool.play(powerUp, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playWallHit(){
        soundPool.play(wallHit, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
