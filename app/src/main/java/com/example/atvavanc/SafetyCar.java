package com.example.atvavanc;

import java.util.Map;

public class SafetyCar extends Carro {
    public SafetyCar(String name, Integer idCarro, Integer x, Integer y) {
        super(name, idCarro, x, y);
    }

    public SafetyCar(Map<String, Object> dados) throws Exception {
        super(dados);
    }
    @Override
    public void start() {
        thread = new Thread(this);
        thread.setPriority(10);
        thread.start();
    }

}
