package com.example.breakoutgame;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.breakoutgame.graphics.Sprite;
import com.example.breakoutgame.objects.Ball;
import com.example.breakoutgame.objects.Brick;
import com.example.breakoutgame.objects.Player;


// Game manages all object in the game and is responsible for all states and render
// all objects to the screen

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Ball ball;
    private final GameLoop gameLoop;
    private final Context context;
    private Sprite sprite;
    private float temporary_x;
    private float playerSpeed;
    private Brick[] bricks;
    public int NumOfBricks = 52;
    private int collisionCount = 0;
    private int level = 1;
    private SoundPlayer sound;

    public Game(Context context) {
        super(context);

        //Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);
        sound = new SoundPlayer(context);
        //initialize player
        player = new Player(getContext());
        ball = new Ball(getContext(),15);
        bricks = new Brick[52];
        int id = 0;
        for (int y = 0; y < 4; y++){
            for (int x = 0; x < 13; x++){
                int rangeY = (2 - 0) + 1;
                int brick_color = (int)Math.floor(Math.random() * rangeY) + 0;
                int addX = (60 + 10) * x;
                int addY = (30 + 10) * y;
                bricks[id] = new Brick(getContext(), 100 + addX, 100 + addY, brick_color);
                id++;
            }
        }
        float rangeX = (5f - (-5f)) + 1f;
        ball.ballSpeedX = (float)Math.floor(Math.random() * rangeX) + (-5f);
        float rangeY = (5f - 4f) + 1f;
        ball.ballSpeedY = (float)Math.floor(Math.random() * rangeY) + 4f;
        setFocusable(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // handle touch event actions
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                temporary_x = player.paddleX;
                player.setPosition(event.getX());
                playerSpeed = temporary_x - player.paddleX;
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawFPS(canvas);
        printLevel(canvas);

        player.draw(canvas);
        ball.draw(canvas);
        for(int i = 0; i < 52; i++){
            bricks[i].draw(canvas);
        }

        if (player.heart <= 0){
            Intent intent = new Intent(context, GameOver.class);
            context.startActivity(intent);
        //    ((Activity) context).finish();
        }
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 300, paint);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void printLevel(Canvas canvas){
        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Level " + level, getScreenWidth() / 2 - 50, 50, paint);
    }

    public void nextLevel(){
        if(collisionCount == NumOfBricks){
            ball.resetBall();
            for(int i = 0; i < NumOfBricks; i++){
                bricks[i].isDestroyed = false;
            }
            collisionCount = 0;
            level++;
        }
    }

    public void update() {
        // Update game state
        player.update();
        ball.update();

        if(ball.ballY - ball.radius > player.paddleY + player.height){
            sound.playHurtSound();
            player.heart--;
            player.decreasePaddleWidth();
            if(player.heart > 0){
                ball.resetBall();
            }
        }
        // check ball collision
        if(ball.collides(player)){
            sound.playPaddleHit();
            ball.ballY = player.paddleY - player.height / 2.0 - ball.radius / 2 ;
            ball.ballSpeedY = -ball.ballSpeedY;
            // SOUND
            // if we hit the paddle on its left side while moving left...
            if (ball.ballX < player.paddleX + (player.width / 2.0) && playerSpeed > 5) {
                ball.ballSpeedX = -0.3 + (-(0.4 * (player.paddleX + player.width / 2.0 - ball.ballX)));
            }

            // else if we hit the paddle on its right side while moving right...
            else if (ball.ballX > player.paddleX + (player.width / 2.0) && playerSpeed < -5) {
                ball.ballSpeedX = 0.3 + (0.4 * Math.abs(player.paddleX + player.width / 2.0 - ball.ballX));
            }

        }

         for (int i = 0; i < NumOfBricks; i++){
            if (ball.collides(bricks[i]) && !bricks[i].isDestroyed){
                sound.playScoreSound();
                collisionCount++;
                bricks[i].isDestroyed = true;
                player.score = player.score + 1 + bricks[i].brickColor;
                player.temporaryScore = player.temporaryScore + 1 + bricks[i].brickColor;
                ball.ballSpeedX += 0.1;
                ball.ballSpeedY += 0.1;
                // ball RIGHT brick LEFT
                if (ball.ballX - ball.radius < bricks[i].x && ball.ballSpeedX > 0){
                    ball.ballSpeedX = -ball.ballSpeedX;
                    ball.ballX = bricks[i].x - ball.radius;
                    break;
                }
                // ball LEFT brick RIGHT
                else if(ball.ballX + ball.radius > bricks[i].x + bricks[i].width && ball.ballSpeedX < 0){
                    ball.ballSpeedX = -ball.ballSpeedX;
                    ball.ballX = bricks[i].x + bricks[i].width + ball.radius;
                    break;
                }
                // ball TOP brick BOTTOM
                else if(ball.ballY - ball.radius < bricks[i].y){
                    ball.ballSpeedY = -ball.ballSpeedY;
                    ball.ballY = bricks[i].y - ball.radius;
                    break;
                }
                // ball BOTTOM brick TOP
                else{
                    ball.ballSpeedY = -ball.ballSpeedY;
                    ball.ballY = bricks[i].y + bricks[i].height + ball.radius;
                    break;
                }
            }
         }
        nextLevel();

    }
}
