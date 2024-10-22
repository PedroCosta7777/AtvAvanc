package com.example.atvavanc;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import android.util.Log;
import android.util.Pair;
import android.view.inputmethod.InsertGesture;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Carro {
    private int angulo;
    private String name;
    private int idCarro; // Declare aqui
    private int x;
    private int y;
    private int fuelTank;
    private double speed;
    private int laps;
    private int distance;
    private int penalty;
    private Map<Integer, Integer> sensor;


    // Construtor
    public Carro(String name, Integer IdCarro, Integer x, Integer y) {
        this.angulo = 0;
        this.name = name;
        this.x = x;
        this.y = y;
        this.fuelTank = 100; // valor padrão
        this.speed = 0.0;
        this.laps = 0;
        this.distance = 10;
        this.penalty = 0;
        this.sensor = new HashMap<>();
        sensor.put(0,-30);
        sensor.put(1,30);
        this.idCarro = idCarro;
    }

    void andar(int d) {
        double dx =  d * Math.cos(Math.toRadians(angulo));
        double dy =  d * Math.sin(Math.toRadians(angulo));

        x += (int) dx;
        y += (int) dy;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Map<Integer, Integer> getSensor() {
        return sensor;
    }

    public void virar (int angulo){
        this.angulo = this.angulo + angulo;

    }

    public Pair<Integer, Integer> getSensorPoint(int n) {
        Integer ang = sensor.get(n);
        if (ang != null) {
            float deltaX = (float) (150 * Math.sin(Math.toRadians(angulo + ang)));
            float deltaY = (float) (150 * Math.cos(Math.toRadians(angulo + ang)));

            int _x = (int) (x + deltaX);
            int _y = (int) (y - deltaY);
            return new Pair<>(_x, _y);
        }
        return null;
    }

    public void andarFrente(int distancia){
        float deltaX = (float) (distancia * Math.sin(Math.toRadians(angulo)));
        float deltaY = (float) (distancia * Math.cos(Math.toRadians(angulo)));

        x = (int) (x + deltaX);
        y = (int) (y - deltaY);
    }

    public int getAngulo() {
        return angulo;
    }

    public void checkSensor(Bitmap pistaBitmap) {

        Pair<Integer, Integer> p0 = getSensorPoint(0);
        Pair<Integer, Integer> p1 = getSensorPoint(1);

        // Verifica a cor do pixel nos pontos dos sensores
        int c0 = pistaBitmap.getPixel(p0.first, p0.second);
        int c1 = pistaBitmap.getPixel(p1.first, p1.second);

        Log.d("COLOR", "c0 = " + c0);
        Log.d("COLOR", "c1 = " + c1);

        if (c1 != -1) {
            virar(-5);
        } else if (c0 != -1) {
            virar(5);
        }
    }
}
