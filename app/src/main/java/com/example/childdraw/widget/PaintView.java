package com.example.childdraw.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaintView extends View {
    private Bitmap btnBackground, btnView, image;
    private Paint maPaint = new Paint();
    private Path mPath = new Path();
    private int colorBackground, sizebrush, sizeErasor;
    private float mX, mY;
    private int leftImage = 50, topImage = 50;

    private Canvas mCanvas;
    private final float DEFFERENCE_SPACE = 4;
    private ArrayList<Bitmap> listActions= new ArrayList<>();
    private Paint mBitmapPaint  = new Paint(Paint.DITHER_FLAG);

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PaintView(Context context)  {
        super(context);

        init();
    }

    private void init() {
        sizeErasor = sizebrush = 12;
        colorBackground = Color.WHITE;

        maPaint.setColor(Color.BLACK);
        maPaint.setAntiAlias(true);
        maPaint.setDither(true);
        maPaint.setStyle(Paint.Style.STROKE);
        maPaint.setStrokeCap(Paint.Cap.ROUND);
        maPaint.setStrokeJoin(Paint.Join.ROUND);
        maPaint.setStrokeWidth(toPx(sizebrush));
        maPaint.setXfermode(null);
        maPaint.setAlpha(0xff);
    }

    private float toPx(int sizebrush) {
        return  sizebrush*(getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        btnBackground = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        btnView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(btnView);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawColor(colorBackground);
        canvas.drawBitmap(btnBackground,0,0,null);

        if(image!=null)
            canvas.drawBitmap(image, leftImage, topImage, null);

        canvas.drawBitmap(btnView, 0, 0, mBitmapPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                addLastAction(getBitmap());
                break;
        }

        return true;
    }

    private void touchUp() {
        mPath.reset();
        invalidate();
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x+mX);
        float dy = Math.abs(y+mY);

        if(dx >= DEFFERENCE_SPACE || dy>= DEFFERENCE_SPACE){
            mPath.quadTo(x,y,(x+mX)/2, (y+mY)/2);
            mY = y;
            mX = x;
            mCanvas.drawPath(mPath, maPaint);
            invalidate();
        }
    }

    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY= y;
        invalidate();
    }

    public Bitmap getBitmap(){
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(this.getDrawingCache());

        this.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void setImage(Bitmap bitmap) {
        image = Bitmap.createScaledBitmap(bitmap, getWidth()/2, getHeight()/2, true);
        invalidate();
    }



    public void setColorBackground(int color){
        colorBackground = color;
        invalidate();
    }
    public void setSizebrush(int s){
        sizebrush= s;
        maPaint.setStrokeWidth(toPx(sizebrush));
    }
    public void setBrushColor(int color){
        maPaint.setColor(color);
    }
    public void setSizeErasor(int s){
        sizeErasor =s;
        maPaint.setStrokeWidth(toPx(sizeErasor));
    }
    public void enableEreaser(){
        maPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    public void desableEreaser(){
        maPaint.setXfermode(null);
        maPaint.setShader(null);
        maPaint.setMaskFilter(null);
    }
    public void addLastAction(Bitmap bitmap){
        listActions.add(bitmap);
    }
    public void returnLastAction(){
        if(listActions.size()>0){
            listActions.remove(listActions.size()-1);
            if(listActions.size()>0){
                btnView = listActions.get(listActions.size()-1);
            }else{
                btnView = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            }
            mCanvas = new Canvas(btnView);
            invalidate();
        }
    }
}
