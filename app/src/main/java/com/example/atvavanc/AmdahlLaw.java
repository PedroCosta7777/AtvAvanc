package com.example.atvavanc;

public class AmdahlLaw {
    // Método para calcular o SpeedUp com base na Lei de Amdahl
    public static double calculateSpeedUp(double f, int N) {
        // f é a fração do código que pode ser paralelizada
        // N é o número de núcleos disponíveis
        return 1.0 / ((1 - f) + (f / N));
    }
}