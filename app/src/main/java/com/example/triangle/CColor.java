package com.example.triangle;
import android.graphics.Color;

public class CColor {
    static float[] purple() {
        return new float[]{
                Color.red(Color.rgb(128, 0, 128)) / 255f,
                Color.green(Color.rgb(128, 0, 128)) / 255f,
                Color.blue(Color.rgb(128, 0, 128)) / 255f,
                1.0f
        };
    }


    static float[] orange() {
        return new float[]{
                Color.red(Color.rgb(255, 165, 0)) / 255f,
                Color.green(Color.rgb(255, 165, 0)) / 255f,
                Color.blue(Color.rgb(255, 165, 0)) / 255f,
                1.0f
        };
    }

    static float[] pink() {
        return new float[]{
                Color.red(Color.rgb(255, 192, 203)) / 255f,
                Color.green(Color.rgb(255, 192, 203)) / 255f,
                Color.blue(Color.rgb(255, 192, 203)) / 255f,
                1.0f
        };
    }

    static float[] yellow() {
        return new float[]{
                Color.red(Color.YELLOW) / 255f,
                Color.green(Color.YELLOW) / 255f,
                Color.blue(Color.YELLOW) / 255f,
                1.0f
        };
    }

    static float[] cyan() {
        return new float[]{
                Color.red(Color.CYAN) / 255f,
                Color.green(Color.CYAN) / 255f,
                Color.blue(Color.CYAN) / 255f,
                1.0f
        };
    }

    static float[] gray() {
        return new float[]{
                Color.red(Color.GRAY) / 255f,
                Color.green(Color.GRAY) / 255f,
                Color.blue(Color.GRAY) / 255f,
                1.0f
        };
    }

}