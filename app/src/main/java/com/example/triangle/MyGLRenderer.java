package com.example.triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private float fov = 53.13f;
    public volatile float mAngleX = 0;
    public volatile float mAngleY = 0;
    private int nWidth;
    private int nHeight;
    private static String TAG = "myRenderer";


    private static final float NEAR = 1f;
    private static final float FAR = 40f;
    public float getAngleX() {
        return mAngleX;
    }
    public float getAngleY() {
        return mAngleY;
    }
    public void setAngleX(float angleX) {
        mAngleX = angleX;
    }
    public void setAngleY(float angleY) {
        mAngleY = angleY;
    }
    private final float[] vPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];
    public Plane mPlane;
    private GLSurfaceView glSurfaceView;
    public Triangle mTriangle;
    private Cube mCube;
    private Context mContext;
    public MyGLRenderer(Context context, GLSurfaceView glSurfaceView) {
        mContext = context;
        this.glSurfaceView = glSurfaceView;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }


    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        mPlane = new Plane(mContext, "plane animation.obj");
        mTriangle = new Triangle();
        mCube = new Cube();
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        // Setup camera and matrices...
        Matrix.setIdentityM(rotationMatrix, 0);
        Matrix.rotateM(rotationMatrix, 0, mAngleX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(rotationMatrix, 0, mAngleY, 1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(vPMatrix, 0, viewMatrix, 0, rotationMatrix, 0);
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, vPMatrix, 0);

        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 12f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);


        switch (currentModel) {
            case TRIANGLE:
                mTriangle.draw(vPMatrix);
                break;
            case PLANE:
                mPlane.draw(vPMatrix);
                break;
            case CUBE:
                mCube.draw(vPMatrix);
                break;
        }
    }
    private enum Model {
        TRIANGLE, CUBE, PLANE
    }

    private Model currentModel = Model.TRIANGLE;

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        nWidth = width;
        nHeight = height;
        GLES30.glViewport(0, 0, nWidth, nHeight);
        float aspect = (float) width / height;
        Matrix.perspectiveM(projectionMatrix, 0, 53.13f, aspect, NEAR, FAR);
    }

    public static int LoadShader(int type, String shaderSrc) {
        int shader;
        int[] compiled = new int[1];
        shader = GLES30.glCreateShader(type);
        if (shader == 0) {
            return 0;
        }
        GLES30.glShaderSource(shader, shaderSrc);
        GLES30.glCompileShader(shader);
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(TAG, "Error compiling shader:");
            Log.e(TAG, GLES30.glGetShaderInfoLog(shader));
            GLES30.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }
    public void switchModel() {
        if (currentModel == Model.TRIANGLE) {
            currentModel = Model.CUBE;
        } else if (currentModel == Model.CUBE) {
            currentModel = Model.PLANE;
        } else {
            currentModel = Model.TRIANGLE;
        }
        glSurfaceView.requestRender();
    }
    public void scaleView(float scaleFactor) {
        fov /= scaleFactor;
        fov = Math.max(Math.min(fov, 200), 10); // Limit the FOV to prevent flip or too close zoom
        updateProjectionMatrix();
        glSurfaceView.requestRender();
    }
    private void updateProjectionMatrix() {
        float aspect = (float) nWidth / nHeight;
        Matrix.perspectiveM(projectionMatrix, 0, fov, aspect, NEAR, FAR);
    }
}
