package com.example.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer renderer;
    private final GestureDetector gestureDetector;
    private final ScaleGestureDetector scaleDetector;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        renderer = new MyGLRenderer(context, this); // Pass this GLSurfaceView to the renderer
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                renderer.switchModel(); // This will now correctly request a render
                return true;
            }
        });

        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                renderer.scaleView(scaleFactor);
                return true;
            }
        });
    }

    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // gesture detectors receive the motion event
        gestureDetector.onTouchEvent(e);
        scaleDetector.onTouchEvent(e);

        //  touch events only if no pinch zoom is detected
        if (!scaleDetector.isInProgress()) {
            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float dx = x - previousX;
                    float dy = y - previousY;
                    renderer.setAngleX(renderer.getAngleX() + dx * 0.1f);
                    renderer.setAngleY(renderer.getAngleY() + dy * 0.1f);
                    requestRender();
                    break;
            }
            previousX = x;
            previousY = y;
        }
        return true;
    }
}
