package com.example.atvavanc;

import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.graphics.Color;


public class Carro {
    private String name;
    private int x;
    private int y;
    private int fuelTank;
    private double speed;
    private int laps;
    private int distance;
    private int penalty;
    private Map<Integer, Integer> sensor;

    // Construtor
    public Carro(String name) {
        this.name = name;
        this.x = 0;
        this.y = 0;
        this.fuelTank = 100; // valor padrão
        this.speed = 0.0;
        this.laps = 0;
        this.distance = distance;
        this.penalty = 0;
        this.sensor = new HashMap<>();
    }

    // Métodos getters e setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFuelTank() {
        return fuelTank;
    }

    public void setFuelTank(int fuelTank) {
        this.fuelTank = fuelTank;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getLaps() {
        return laps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public void updateSensors(Bitmap pistaBitmap) {
        // Limpa o sensor anterior
        sensor.clear();

        // Direções em que os sensores vão operar (e.g., 8 direções: 0°, 45°, 90°, etc.)
        int[] dx = {0, distance, distance, distance, 0, -distance, -distance, -distance};
        int[] dy = {-distance, -distance, 0, distance, distance, distance, 0, -distance};

        // Para cada direção, faz a leitura da cor do pixel
        for (int i = 0; i < dx.length; i++) {
            int sensorX = x + dx[i];
            int sensorY = y + dy[i];

            // Garante que a posição do sensor está dentro dos limites da pista
            if (sensorX >= 0 && sensorX < pistaBitmap.getWidth() && sensorY >= 0 && sensorY < pistaBitmap.getHeight()) {
                int pixelColor = pistaBitmap.getPixel(sensorX, sensorY);

                // Identifica se a cor do pixel representa uma colisão (por exemplo, se for uma cor preta)
                if (isCollisionColor(pixelColor)) {
                    // Se houver uma colisão, a distância medida é menor
                    sensor.put(i + 1, distance - 3); // Por exemplo, se a colisão estiver a 3 unidades de distância
                } else {
                    // Caso contrário, não há obstáculo, então a distância é máxima
                    sensor.put(i + 1, distance);
                }
            } else {
                // Fora dos limites da pista, considera como colisão
                sensor.put(i + 1, 0); // 0 significa colisão
            }
        }
    }

    // Função para determinar se a cor do pixel indica colisão
    private boolean isCollisionColor(int color) {
        // Aqui estamos assumindo que a cor preta (RGB = 0, 0, 0) indica um obstáculo/colisão
        return color == Color.BLACK;
    }

}
