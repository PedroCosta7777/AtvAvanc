package com.example.automath;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import org.jetbrains.annotations.TestOnly;

public class Mathematics {
    @NonNull
    public static Pair<Integer, Integer> getPoint(int x, int y, int angle, int d_angle, int distance) {
        float deltaX = (float) (distance * Math.sin(Math.toRadians(angle + d_angle)));
        float deltaY = (float) (distance * Math.cos(Math.toRadians(angle + d_angle)));

        int _x = (int) (x + deltaX);
        int _y = (int) (y - deltaY);
        return new Pair<>(_x, _y);
    }

    @NonNull
    @TestOnly
    public static Integer getPointX(int x, int angle, int d_angle, int distance) {
        float deltaX = (float) (distance * Math.sin(Math.toRadians(angle + d_angle)));
        return (int) (x + deltaX);
    }

    @NonNull
    @TestOnly
    public static Integer getPointY(int y, int angle, int d_angle, int distance) {
        float deltaY = (float) (distance * Math.cos(Math.toRadians(angle + d_angle)));
        return (int) (y - deltaY);
    }
}
