package com.example.atvavanc;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;
import com.example.automath.Constants;
import com.example.automath.Mathematics;
import com.example.automath.interfaces.MainToCar;
import com.example.escalonamento.Tarefas_Escalonamento;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Carro implements Runnable {
    private String name;
    private int idCarro;
    private int fuelTank;
    private double speed;
    private int laps;
    private int distance;
    private int penalty;
    private int distPercorrida;

    private int angulo;
    private int x;
    private int y;
    private final Map<Integer, Integer> sensor;
    private boolean running = false;
    protected Thread thread;

    private MainToCar main;

    private static final double velocidadeIdeal = 4500.0/50000.0; //px/ms

    ExecutorService executor = Executors.newSingleThreadExecutor();

    // Construtor
    public Carro(String name, Integer idCarro, Integer x, Integer y) {
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
        this.idCarro = idCarro;

        sensor.put(1, -90);
        sensor.put(2, -60);
        sensor.put(3, -30);
        sensor.put(-3, 30);
        sensor.put(-2, 60);
        sensor.put(-1, 90);
    }

    public Carro(Map<String, Object> dados) throws Exception{
        try {
            this.angulo = ((Long) dados.get("angulo")).intValue();
            this.name = (String) dados.get("name");
            this.x = ((Long) dados.get("x")).intValue();
            this.y = ((Long)dados.get("y")).intValue();
            this.fuelTank = ((Long)dados.get("fuelTank")).intValue();
            this.speed = (double) dados.get("speed");
            this.laps = ((Long)dados.get("laps")).intValue();
            this.distance = ((Long)dados.get("distance")).intValue();
            this.penalty = ((Long)dados.get("penalty")).intValue();
            this.idCarro = ((Long)dados.get("idCarro")).intValue();
        } catch (Exception e) {
            Log.d("CRIAR CARROS", e.toString());
            throw new Exception("FALHA AO CONVERTER DADOS DO CARRO");
        }

        this.sensor = new HashMap<>();
        sensor.put(1, -90);
        sensor.put(2, -60);
        sensor.put(3, -30);
        sensor.put(-3, 30);
        sensor.put(-2, 60);
        sensor.put(-1, 90);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("fuelTank", fuelTank);
        map.put("idCarro", idCarro);
        map.put("angulo", angulo);
        map.put("x", x);
        map.put("y", y);
        map.put("speed", speed);
        map.put("laps", laps);
        map.put("distance", distance);
        map.put("penalty", penalty);
        return map;
    }

    public int getDistPercorrida() {
        return distPercorrida;
    }

    public void setDistPercorrida(int distPercorrida) {
        this.distPercorrida = distPercorrida;
    }

    public void setMain(MainToCar main) {
        this.main = main;
    }

    public Rect getRect() {
        int width = Constants.width;
        int height = Constants.height;
        return new Rect(getX() - (width / 2), getY() - (height / 2), getX() + (width / 2), getY() + (height / 2));
    }

    public int getX() { return x; }


    public int getY() { return y; }


    public Map<Integer, Integer> getSensor() { return sensor; }

    public void virar (int angulo){
        this.angulo = this.angulo + angulo;

    }

    public Pair<Integer, Integer> getSensorPoint(int n) {
        Integer ang = sensor.get(n);
        int radius = Constants.radius;
        if (ang != null) {
            return Mathematics.getPoint(x, y, angulo, ang, radius);
        }
        return null;
    }

    public void andarFrente(int distancia){
        Pair<Integer, Integer> p = Mathematics.getPoint(x, y, angulo, 0, distancia);
        x = p.first;
        y = p.second;
        distPercorrida = distPercorrida + distancia;

        Log.d("Distancia Percorrida", "Distancia decorrida: " + distPercorrida + " px");
    }


    private boolean estaNoSemaforo() {
        executor.submit(() -> {

            long startTimeT2 = System.currentTimeMillis();

            Rect r = getRect();
            int x_semaforo = main.getPista().getWidth() / 2;

            // Marca o fim do tempo
            long endTimeT2 = System.currentTimeMillis();
            long elapsedTimeT2 = endTimeT2 - startTimeT2;

            Tarefas_Escalonamento.adicionarTarefa(2, elapsedTimeT2, Tarefas_Escalonamento.tarefas.size() + 1);

            // Log do tempo total
            Log.d("TEMPO_EXECUCAO T2", "Tempo de execução T2: " + elapsedTimeT2 + " ms");

            return (y < (main.getPista().getHeight() / 2)) && (r.left <= x_semaforo && r.right + 30 >= x_semaforo);
        });
        return false;
    }

    public int getAngulo() {
        return angulo;
    }

    public void checkSensor(Bitmap pistaBitmap) {
        for (Map.Entry<Integer, Integer> entry : sensor.entrySet()) {
            Pair<Integer, Integer> p = getSensorPoint(entry.getKey());
            int c = pistaBitmap.getPixel(p.first, p.second);
            if (c != -1) {
                virar(entry.getKey() * 2);
            }
        }
    }

    public void stopCar() {
        try {
            running = false;
            thread.join();
        } catch (Exception e) {
            Log.d("EXCEPTION", e.toString());
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                if (estaNoSemaforo()) {
                    main.getSemaforo().acquire();
                    // repete o andar até passar o semaforo e depois libera ele
                    while (estaNoSemaforo()) {
                        checkSensor(main.getPista());
                        andarFrente(10);
                        Thread.sleep(100);
                    }
                    main.getSemaforo().release();
                }

                executor.submit(() -> {
                    long startTimeT1 = System.currentTimeMillis();
                    checkSensor(main.getPista());
                    long endTimeT1 = System.currentTimeMillis();
                    long elapsedTimeT1 = endTimeT1 - startTimeT1;
                    // Log do tempo total
                    Tarefas_Escalonamento.adicionarTarefa(1, elapsedTimeT1, Tarefas_Escalonamento.tarefas.size()+1);


                    Log.d("TEMPO_EXECUCAO T1", "Tempo de execução T1: " + elapsedTimeT1 + " ms");

                    long startTimeT4 = System.currentTimeMillis();
                    andarFrente(10);
                    long endTimeT4 = System.currentTimeMillis();
                    long elapsedTimeT4 = endTimeT4 - startTimeT4;
                    Tarefas_Escalonamento.adicionarTarefa(4, elapsedTimeT4, Tarefas_Escalonamento.tarefas.size()+1);

                    // Log do tempo total
                    Log.d("TEMPO_EXECUCAO T4", "Tempo de execução T4: " + elapsedTimeT4 + " ms");
                });

                Thread.sleep(100);
            } catch (Exception e) {
                Log.d("EXCEPTION", e.toString());
            }
        }
    }
}