package com.example.atvavanc;

import java.util.Map;

public class SafetyCar extends Carro {
    public SafetyCar(String name, Integer idCarro, Integer x, Integer y) {
        super(name, idCarro, x, y);
        setPriority(2);
    }

    public SafetyCar(Map<String, Object> dados) throws Exception {
        super(dados);
        setPriority(2);
    }
}
