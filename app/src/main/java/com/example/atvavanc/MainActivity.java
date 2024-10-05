package com.example.atvavanc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import java.util.List;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageView pista;
    private Carro carro;
    private EditText editTextNumber;
    private Button startButton, pauseButton, finishButton;
    private ImageView carImageView;
    private RelativeLayout relativeLayout;

    private Bitmap pistaBitmap;

    private int d;

    private List<Carro> listaDeCarros;
    private List<ImageView> listaDeCarrosViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Verifique se o nome do seu layout está correto

        editTextNumber = findViewById(R.id.editTextNumber);
        relativeLayout = findViewById(R.id.relativeLayout); // Verifique se você definiu o ID do seu RelativeLayout

        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCars();
            }
        });
    }

    private void addCars() {
        // Remover apenas as imagens dos carros
        int carCount = relativeLayout.getChildCount();
        for (int i = carCount - 1; i >= 0; i--) { // Remover do final para o início
            View child = relativeLayout.getChildAt(i);
            if (child instanceof ImageView && child.getId() != R.id.pista) { // Verifica se é um carro
                relativeLayout.removeViewAt(i);
            }
        }

        String numberStr = editTextNumber.getText().toString();
        if (!numberStr.isEmpty()) {
            int numberOfCars = Integer.parseInt(numberStr);

            for (int i = 0; i < numberOfCars; i++) {
                ImageView carImageView = new ImageView(this);
                carImageView.setImageResource(R.drawable.carro); // Defina a imagem do carro

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        100, // Ajuste a largura
                        50   // Ajuste a altura
                );

                // Defina a posição do carro
                params.leftMargin = 830; // ajuste a posição horizontal
                params.topMargin = 600 + (i * 50); // ajuste a posição vertical para cada carro

                carImageView.setLayoutParams(params);
                relativeLayout.addView(carImageView); // Adiciona o carro ao layout
            }
        } else {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show();
        }
    }
}