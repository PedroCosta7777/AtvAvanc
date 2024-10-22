package com.example.atvavanc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

public class PistaView extends View {

    private Bitmap pistaBitmap;  // Bitmap da pista (imagem de fundo)
    private List<Carro> listaDeCarros;  // Lista de carros a serem desenhados
    private Paint paintCarro;  // Paint para desenhar os carros

    // Construtor
    public PistaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Inicializa as configurações do Paint
    private void init() {
        paintCarro = new Paint();
        paintCarro.setColor(Color.BLUE);  // Cor dos carros (você pode alterar)
        paintCarro.setStyle(Paint.Style.FILL);
    }

    // Método chamado sempre que a tela é redesenhada
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenha a pista (se a pista foi carregada)
        if (pistaBitmap != null) {
            canvas.drawBitmap(pistaBitmap, 0, 0, null);
        }

        // Desenha os carros na posição atual
        if (listaDeCarros != null) {
            for (Carro carro : listaDeCarros) {
                canvas.drawCircle(carro.getX(), carro.getY(), 20, paintCarro);  // Desenha o carro como um círculo
            }
        }
    }

    // Define a pista para ser desenhada
    public void setPistaBitmap(Bitmap pistaBitmap) {
        this.pistaBitmap = pistaBitmap;
        invalidate();  // Requisita uma atualização da tela
    }

    // Define a lista de carros a serem desenhados
    public void setListaDeCarros(List<Carro> listaDeCarros) {
        this.listaDeCarros = listaDeCarros;
        invalidate();  // Requisita uma atualização da tela
    }
}
