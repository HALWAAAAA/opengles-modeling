package com.example.triangle;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Plane {
    private static final String TAG = "Plane";
    private final int mProgram;
    private FloatBuffer vertexBuffer;
    private IntBuffer indexBuffer;
    private int numIndices;
    private int positionHandle;
    private int mvpMatrixHandle;
    private int colorHandle;  // Handle for the color uniform

    // Shaders
    private static final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private static final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Plane(Context context, String objFilename) {
        loadObj(context, objFilename);
        mProgram = createOpenGLProgram();
        colorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
    }

    private void loadObj(Context context, String filename) {
        List<Float> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] vertex = line.split("\\s+");
                    vertices.add(Float.parseFloat(vertex[1])); // x
                    vertices.add(Float.parseFloat(vertex[2])); // y
                    vertices.add(Float.parseFloat(vertex[3])); // z
                } else if (line.startsWith("f ")) {
                    String[] face = line.split("\\s+");
                    for (int i = 1; i < face.length; i++) {
                        String[] vertex = face[i].split("/");
                        indices.add(Integer.parseInt(vertex[0]) - 1); // Convert to zero-based index
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to load asset " + filename, e);
        }

        float[] verticesArray = new float[vertices.size()];
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < vertices.size(); i++) verticesArray[i] = vertices.get(i);
        for (int i = 0; i < indices.size(); i++) indicesArray[i] = indices.get(i);

        ByteBuffer bb = ByteBuffer.allocateDirect(verticesArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(verticesArray);
        vertexBuffer.position(0);

        ByteBuffer ib = ByteBuffer.allocateDirect(indicesArray.length * 4);
        ib.order(ByteOrder.nativeOrder());
        indexBuffer = ib.asIntBuffer();
        indexBuffer.put(indicesArray);
        indexBuffer.position(0);

        numIndices = indicesArray.length;
    }

    private int createOpenGLProgram() {
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);

        return program;
    }

    private int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }

    public void draw(float[] mvpMatrix) {
        GLES30.glUseProgram(mProgram);

        // Set color to gray
        float[] color = {0.5f, 0.5f, 0.5f, 1.0f}; // RGBA for gray
        GLES30.glUniform4fv(colorHandle, 1, color, 0);

        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glEnableVertexAttribArray(positionHandle);
        GLES30.glVertexAttribPointer(positionHandle, 3, GLES30.GL_FLOAT, false, 12, vertexBuffer);

        mvpMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, numIndices, GLES30.GL_UNSIGNED_INT, indexBuffer);

        GLES30.glDisableVertexAttribArray(positionHandle);
    }
}