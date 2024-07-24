package com.example.triangle;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {
    String vertexShaderCode =
            "#version 300 es 			  \n"
                    + "uniform mat4 uMVPMatrix;     \n"
                    + "in vec4 vPosition;           \n"
                    + "void main()                  \n"
                    + "{                            \n"
                    + "   gl_Position = uMVPMatrix * vPosition;  \n"
                    + "}                            \n";
    //fragment shader code.
    String fragmentShaderCode =
            "#version 300 es		 			          	\n"
                    + "precision mediump float;					  	\n"
                    + "uniform vec4 vColor;	 			 		  	\n"
                    + "out vec4 fragColor;	 			 		  	\n"
                    + "void main()                                  \n"
                    + "{                                            \n"
                    + "  fragColor = vColor;                    	\n"
                    + "}            \n";

    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int vPMatrixHandle;
    float size = 1.0f;
    private int colorH;
    float colorcyan[] = CColor.cyan();
    float colorpink[] = CColor.pink();
    float colorpurple[] = CColor.purple();
    float colorgray[] = CColor.gray();
    float colororange[] = CColor.orange();
    float coloryellow[] = CColor.yellow();
    String TAG = "Cube";
    float[] mVerticesData = new float[]{
            //pink
            -size, -size, size, // top-left
            -size, -size, -size, // bottom-left
            size, -size, -size, // bottom-right

            size, -size, -size, // bottom-right
            size, -size, size, // top-right
            -size, -size, size, // top-left

            // BACK

            // Triangle 1 yellow
            size, -size, size, // top-left
            size, -size, -size, // bottom-left
            size, size, -size, // bottom-right
            // Triangle 2
            size, size, -size, // bottom-right
            size, size, size, // top-right
            size, -size, size, // top-left


            // LEFT


            // Triangle 1 purple
            size, size, size, // top-left
            size, size, -size, // bottom-left
            -size, size, -size, // bottom-right
            // Triangle 2
            -size, size, -size, // bottom-right
            -size, size, size, // top-right
            size, size, size,// top-left

            // RIGHT

            // Triangle 1 orange
            -size, size, size, // top-left
            -size, size, -size, // bottom-left
            -size, -size, -size, // bottom-right
            // Triangle 2
            -size, -size, -size, // bottom-right
            -size, -size, size, // top-right
            -size, size, size, // top-left


            // TOP

            // Triangle 1
            size, size, size, // top-left
            -size, size, size, // bottom-left
            -size, -size, size, // bottom-right
            // Triangle 2
            -size, -size, size, // bottom-right
            size, -size, size, // top-right
            size, size, size, // top-left

            // BOTTOM
            // Triangle 1
            -size, size, -size, // top-left
            size, size, -size, // bottom-left
            size, -size, -size, // bottom-right
            // Triangle 2
            size, -size, -size, // bottom-right
            -size, -size, -size, // top-right
            -size, size, -size // top-left
    };
    public Cube(){
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

        colorH = GLES30.glGetUniformLocation(mProgram, "vColor");

        GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        int index = 0;
        vertexBuffer.position(index);

        GLES30.glVertexAttribPointer(index, 3, GLES30.GL_FLOAT,
                false, 0, vertexBuffer);
        GLES30.glEnableVertexAttribArray(index);

        int start = 0;
        int vertices = 6;

        GLES30.glUniform4fv(colorH, 1, colorpink, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,start,vertices);
        start += vertices;

        GLES30.glUniform4fv(colorH, 1, colorcyan, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, start, vertices);
        start += vertices;

        GLES30.glUniform4fv(colorH, 1, colorpurple, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,start,vertices);
        start += vertices;

        GLES30.glUniform4fv(colorH, 1, colorgray, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,start,vertices);
        start += vertices;

        GLES30.glUniform4fv(colorH, 1, colororange, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,start,vertices);
        start += vertices;

        GLES30.glUniform4fv(colorH, 1, coloryellow, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,start,vertices);
    }
}
