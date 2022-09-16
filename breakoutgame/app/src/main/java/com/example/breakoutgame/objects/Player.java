package com.example.breakoutgame.objects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import com.example.breakoutgame.Game;
import com.example.breakoutgame.R;
import com.example.breakoutgame.graphics.Sprite;
import com.example.breakoutgame.graphics.SpriteSheet;

public class Player {
    private Game game;
    private final SpriteSheet spriteSheet;
    private int bottom;
    private int right;
    private int top;
    private int left;
    public int width;
    private int width1;
    public int width2;
    public int width3;
    public int height;
    public float paddleX;
    public float paddleY;
    private Paint paint;
    public int heart = 3;
    private Sprite sprite;
    public int heartX = 30;
    public int heartY = 30;
    public int score;
    public double scoreGoal1 = 52 * 0.1;
    public int temporaryScore = 0;


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public Player(Context context){
        this.width = 180;
        this.width1 = 180;
        this.width2 = 250;
        this.height = 30;
        this.paddleX = getScreenWidth() / 2 - 50;
        this.paddleY = getScreenHeight() - 195;
        this.left = 0;
        this.top = 31;
        this.right = 30;
        this.bottom = 60;
        this.score = 0;
        spriteSheet = new SpriteSheet(context);
        this.sprite = spriteSheet.getBlockSprite(left,top,right,bottom);
        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.paddle);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        RectF rectF = new RectF(
                paddleX, // left
                paddleY, // top
                paddleX + width, // right
                paddleY + height // bottom
        );
        int cornersRadius = 25;

        // Finally, draw the rounded corners rectangle object on the canvas
        canvas.drawRoundRect(
                rectF, // rect
                cornersRadius, // rx
                cornersRadius, // ry
                paint // Paint
        );
        drawHearts(canvas, heart);
        paint.setTextSize(50);
        canvas.drawText("SCORE: " + score, getScreenWidth() - 250, 50, paint);

    }

    public void update() {

        checkCollision();
        increasePaddleWidth();
    }

    private void increasePaddleWidth() {
        if(temporaryScore > scoreGoal1){
            width = width2;
        }
    }
    public void decreasePaddleWidth() {
        if(width == width2){
            width = width1;
            temporaryScore = 0;
        }
    }

    public void setPosition(float paddleX) {
        this.paddleX = paddleX;
    }

    public void checkCollision() {
        float screen_left_corner = 0;
        float screen_right_corner = getScreenWidth();
        if (this.paddleX <= screen_left_corner) {
            paddleX = screen_left_corner;
        }
        else if (this.paddleX >= screen_right_corner - width){
            paddleX = screen_right_corner - width;
        }
    }

    public void drawHearts(Canvas canvas, int heart){
        int addX;
        for (int i = 0; i < heart; i++){
            addX = 40 * i;
            sprite.draw(canvas, heartX + addX, heartY, 30, 30);
        }

    }

}
