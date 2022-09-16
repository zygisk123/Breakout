package com.example.breakoutgame.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.breakoutgame.R;

public class SpriteSheet {
    private Bitmap bitmap;

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_sheet, bitmapOptions);
    }

    public Sprite getBlockSprite(int left, int top, int right, int bottom) {
        return new Sprite(this, new Rect(left,top,right,bottom));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
