package com.lingjing;

import com.example.lingjing.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


@SuppressLint("ViewConstructor")
public class MyGLSurfaceView extends GLSurfaceView {
    protected Resources mResources;
    private SphereVideoRenderer mRenderer;
    private MediaPlayer mMediaPlayer;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320 / 3.8f;
    private float mPreviousX;
    private float mPreviousY;


    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mMediaPlayer = new MediaPlayer();

        mResources = context.getResources();

        try {
            AssetFileDescriptor afd = mResources.openRawResourceFd(R.raw.boat);
            mMediaPlayer.setDataSource(
                    afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
        } catch (Exception e) {
            Log.e("false", e.getMessage(), e);
        }


        mRenderer = new SphereVideoRenderer(context, mMediaPlayer);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;
                    mRenderer.setAngleX(
                            mRenderer.getAngleX() +
                                    (dx * TOUCH_SCALE_FACTOR));
                    mRenderer.setAngleY(
                            mRenderer.getAngleY() +
                                    (dy * TOUCH_SCALE_FACTOR));
                    requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    @Override
    public void onResume() {
        queueEvent(new Runnable(){
            public void run() {
                mRenderer.setMediaPlayer(mMediaPlayer);
            }});
        super.onResume();
    }

}
