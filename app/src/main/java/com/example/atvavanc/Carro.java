package com.example.atvavanc;

import java.util.HashMap;
import java.util.Map;

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
        this.distance = 0;
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

    public Map<Integer, Integer> getSensor() {
        return sensor;
    }

    public void setSensor(Map<Integer, Integer> sensor) {
        this.sensor = sensor;
    }
}
