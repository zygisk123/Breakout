package com.example.breakoutgame.objects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.breakoutgame.R;
import com.example.breakoutgame.SoundPlayer;

public class Ball {
    public double ballSpeedX;
    public double ballSpeedY;
    public double ballX;
    public double ballY;
    public double radius;
    public Player player;
    private Paint paint;
    private SoundPlayer sound;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Ball(Context context, double radius) {
        this.ballX = getScreenWidth() / 2 - radius / 2;
        this.ballY = getScreenHeight() / 2 - radius / 2;
        this.radius = radius;
        this.sound = new SoundPlayer(context);
        // these variables are for keeping track of our velocity on both the
        // X and Y axis, since the ball can move in two dimensions
        this.ballSpeedX = 0;
        this.ballSpeedY = 0;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.ball);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) ballX, (float) ballY, (float) radius, paint);
    }

    public void update() {
        ballX = ballX + ballSpeedX;
        ballY = ballY + ballSpeedY;
        collidesWall();

    }

    public void collidesWall(){
        // allow ball to bounce off walls
        if (ballX <= 0) {
            ballX = radius;
            ballSpeedX = -ballSpeedX;
            sound.playWallHit();
        }
        if (ballX >= getScreenWidth() - radius) {
            ballX = getScreenWidth() - radius;
            ballSpeedX = -ballSpeedX;
            sound.playWallHit();
        }
        if (ballX <= 0) {
            ballX = 0;
            ballSpeedX = -ballSpeedX;
            sound.playWallHit();
        }
        if (ballY <= 0) {
            ballY = 0;
            ballSpeedY = -ballSpeedY;
            sound.playWallHit();
        }
      //  if (ballY >= getScreenHeight() - radius) {
            // SOUND
      //  }

    }
    public boolean collides(Player target) {
        if (ballX - radius > target.paddleX + target.width || target.paddleX > ballX + radius){
            return false;
        }
        if (ballY - radius > target.paddleY + target.height || target.paddleY > ballY + radius ){
            return false;
        }
        return true;
    }

    public boolean collides(Brick target) {
        if (ballX - radius > target.x + target.width || target.x > ballX + radius){
            return false;
        }
        if (ballY - radius> target.y + target.height || target.y > ballY + radius){
            return false;
        }
        return true;
    }

    public void resetBall(){
        ballX = getScreenWidth() / 2 - radius / 2;
        ballY = getScreenHeight() / 2 - radius / 2;
        float rangeX = (5f - (-5f)) + 1f;
        ballSpeedX = (float)Math.floor(Math.random() * rangeX) + (-2f);
        float rangeY = (5f - 4f) + 1f;
        ballSpeedY = (float)Math.floor(Math.random() * rangeY) + 4f;
    }
}