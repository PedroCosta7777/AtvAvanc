package com.example.automath.interfaces;

import android.graphics.Bitmap;

import java.util.concurrent.Semaphore;

public interface MainToCar {
    Semaphore getSemaforo();
    Bitmap getPista();
}