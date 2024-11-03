package com.example.automath;

import org.junit.Test;

import static org.junit.Assert.*;
public class LibraryTest {
    @Test
    public void TesteGetPointX() {
        int x = 0;
        int angulo = 0;
        int delta_angulo = 90;
        int distancia = 20;
        Integer _x = Mathematics.getPointX(x, angulo, delta_angulo, distancia);
        assertEquals((Integer) 20, _x);
    }

    @Test
    public void TesteGetPointY() {
        int y = 0;
        int angulo = 0;
        int delta_angulo = 90;
        int distancia = 20;
        Integer _y = Mathematics.getPointY(y, angulo, delta_angulo, distancia);
        assertEquals((Integer) 0, _y);
    }
}