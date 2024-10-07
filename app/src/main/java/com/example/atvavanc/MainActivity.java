package com.example.atvavanc;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
    private EditText editTextNumber;
    private Button startButton, pauseButton, finishButton;
    private ImageView carImageView;
    private RelativeLayout relativeLayout;
    private List<Carro> listaDeCarros;
    private List<ImageView> listaDeCarrosViews;
    private Handler handler;
    private Runnable moveCarsRunnable;
    private boolean isMoving;

    //-----------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Verifique se o nome do seu layout está correto

        editTextNumber = findViewById(R.id.editTextNumber);
        relativeLayout = findViewById(R.id.relativeLayout);

        // Inicializar as listas
        listaDeCarros = new ArrayList<>();
        listaDeCarrosViews = new ArrayList<>();

        startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCars();
                startCarMovement();
            }
        });

        // Botão Pause
        pauseButton = findViewById(R.id.button2); // Id button2 para pause
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause(); // Pausa o movimento dos carros
            }
        });

        // Botão Finish
        finishButton = findViewById(R.id.button3); // Supondo que você tenha um botão com esse ID
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRace(); // Chama o método para finalizar a corrida
            }
        });

        handler = new Handler();
    }

//----------------------------------------------------------------------------------------------------------------------------------
    private void addCars() {
        String numberStr = editTextNumber.getText().toString();

        if (!numberStr.isEmpty()) {
            int numberOfCars = Integer.parseInt(numberStr);
            relativeLayout.removeViews(7, relativeLayout.getChildCount() - 7); // Remove carros anteriores, se houver

            listaDeCarros.clear();
            listaDeCarrosViews.clear();

            for (int i = 0; i < numberOfCars; i++) {
                Carro novoCarro = new Carro("Carro " + (i + 1)); // Cria um carro com nome
                listaDeCarros.add(novoCarro);

                ImageView carImageView = new ImageView(this);
                carImageView.setImageResource(R.drawable.carro); // Defina a imagem do carro


                // Define a posição do carro
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        120,
                        55
                );
                params.leftMargin = 780; // ajuste a posição horizontal
                params.topMargin = 600 + (i * 70); // ajuste a posição vertical para cada carro

                carImageView.setLayoutParams(params);

                relativeLayout.addView(carImageView); // Adiciona o carro ao layout
                listaDeCarrosViews.add(carImageView);
            }
        } else {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show();
        }
    }
    //---------------------------------------------------------------------------------------------------------------------
    private void startCarMovement(){
        isMoving = true; //inicializa os movimentos

        moveCarsRunnable = new Runnable() {
            @Override
            public void run() {
                // Move cada carro para a direita
                for (int i = 0; i < listaDeCarrosViews.size(); i++) {
                    ImageView carView = listaDeCarrosViews.get(i);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) carView.getLayoutParams();
                    params.topMargin -= 5; // Move 5 pixels para cima
                    carView.setLayoutParams(params);
                }

                if (isMoving) {
                    handler.postDelayed(this, 100); // Atualiza a posição a cada 100ms
                }
            }
        };
        handler.post(moveCarsRunnable);
    }
    //------------------------------------------------------------------------------------------------------------------------
    // Para parar o movimento dos carros quando a atividade for encerrada
    @Override
    protected void onPause() {
        super.onPause();
        isMoving = false; // Para o movimento
        handler.removeCallbacks(moveCarsRunnable); // Remove as atualizações
    }
    //----------------------------------------------------------------------------------------------------------------------
    private void finishRace() {
        onPause(); // Para o movimento dos carros
        relativeLayout.removeViews(7, relativeLayout.getChildCount() - 7); // Remove carros anteriores, se houver
        listaDeCarrosViews.clear(); // Limpa a lista de carros
        editTextNumber.setText(""); // Limpa o campo de entrada para novo valor
        Toast.makeText(this, "Corrida finalizada! Insira um novo número.", Toast.LENGTH_SHORT).show();
    }
}


