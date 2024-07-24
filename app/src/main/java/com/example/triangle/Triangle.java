package com.example.triangle;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    String vertexShaderCode =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "}                            \n";
    String fragmentShaderCode =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + "  fragColor = vec4(0.63671875f, 0.76953125f, 0.22265625f, 1.0f);    \n" // Red color
                    + "}                                            \n";
    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int vPMatrixHandle;

    float size = 1.0f;
    float[] mVerticesData = new float[]{
            0.0f, size, 0.0f, // Top
            -size, -size, 0.0f, // Bottom-left
            size, -size, 0.0f,

            size, size, 0.0f, // Top
            -size, -size, -size, // Bottom-left
            size, -size, 0.0f// Bottom-right
    };

    String TAG = "Triangle";
    public Triangle() {
        vertexBuffer = ByteBuffer
                .allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(mVerticesData);
        vertexBuffer.position(0);
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];
        vertexShader = MyGLRenderer.LoadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        fragmentShader = MyGLRenderer.LoadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);
        programObject = GLES30.glCreateProgram();
        if (programObject == 0) {
            Log.e(TAG, "Error creating program object");
            return;
        }
        GLES30.glAttachShader(programObject, vertexShader);
        GLES30.glAttachShader(programObject, fragmentShader);
        GLES30.glBindAttribLocation(programObject, 0, "vPosition");
        GLES30.glLinkProgram(programObject);
        GLES30.glGetProgramiv(programObject, GLES30.GL_LINK_STATUS, linked, 0);
        if (linked[0] == 0) {
            Log.e(TAG, "Error linking program:");
            Log.e(TAG, GLES30.glGetProgramInfoLog(programObject));
            GLES30.glDeleteProgram(programObject);
            return;
        }
        mProgram = programObject;
    }
    public void draw(float[] mvpMatrix) {
        GLES30.glUseProgram(mProgram);
        vPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");
        int VERTEX_POS_INDX = 0;
        vertexBuffer.position(VERTEX_POS_INDX);
        GLES30.glVertexAttribPointer(VERTEX_POS_INDX, 3, GLES30.GL_FLOAT,
                false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
    }
}


