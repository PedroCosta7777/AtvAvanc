package com.example.atvavanc;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.atvavanc.Carro;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private ImageView pista;
    private EditText editTextNumber;
    private Button startButton, pauseButton, finishButton;
    private RelativeLayout relativeLayout;
    private List<Carro> listaDeCarros;
    private List<ImageView> listaDeCarrosViews;
    private Handler handler;
    private Runnable moveCarsRunnable;
    private boolean isMoving;
    private ImageView sensor2, sensor3;

    private Bitmap pistaBitmap; // Bitmap da pista
    private Bitmap carroBmp; // Bitmap da pista


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Verifique se o nome do seu layout está correto

        editTextNumber = findViewById(R.id.editTextNumber);
        relativeLayout = findViewById(R.id.relativeLayout);
        pista = findViewById(R.id.pista); // Certifique-se de ter a referência da pista

        // Inicializar as listas
        listaDeCarros = new ArrayList<>();
        listaDeCarrosViews = new ArrayList<>();


        // Carregar a imagem da pista como Bitmap
        pistaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pista1);
        carroBmp = BitmapFactory.decodeResource(getResources(), R.drawable.carro);
        pista.setImageBitmap(pistaBitmap);

        //Inicializa os botões
        startButton = findViewById(R.id.button);
        pauseButton = findViewById(R.id.button2);
        finishButton = findViewById(R.id.button3);

        funcaoBotoes();
    }

    private void PaintCars() {
        Bitmap bitmap = pistaBitmap.copy(pistaBitmap.getConfig(), true);
        Bitmap car = carroBmp.copy(carroBmp.getConfig(), true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        for (Carro c : listaDeCarros) {
            Rect r = new Rect(c.getX() - 40, c.getY() - 50, c.getX() + 40, c.getY() + 50);

            canvas.rotate(c.getAngulo(), c.getX(), c.getY());
            canvas.drawBitmap(car, null, r, paint);
            canvas.rotate(-c.getAngulo(), c.getX(), c.getY());

            for (Integer s : c.getSensor().keySet()) {
                Pair<Integer, Integer> pos = c.getSensorPoint(s);
                canvas.drawCircle(pos.first, pos.second, 15, paint);
            }
        }
        pista.setImageBitmap(bitmap);
    }


    private void funcaoBotoes(){
        startButton.setOnClickListener(v -> {
            addCars();
            startCarMovement();
        });

        // Botão Pause
        pauseButton.setOnClickListener(v -> {
            onPause(); // Pausa o movimento dos carros
        });

        // Botão Finish
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishRace(); // Chama o método para finalizar a corrida
            }
        });
        handler = new Handler();
    }

    private void addCars() {
        String numberStr = editTextNumber.getText().toString();

        if (!numberStr.isEmpty()) {
            int numberOfCars = Integer.parseInt(numberStr);
            listaDeCarros.clear();
            int mid_y = pistaBitmap.getHeight() / 2;
            int mid_x = (int)(pistaBitmap.getWidth() * 0.80);

            for (int i = 0; i < numberOfCars; i++) {
                Carro novoCarro = new Carro("Carro " + (i + 1), i+1, mid_x, mid_y + i*200);
                listaDeCarros.add(novoCarro);
            }
        } else {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCarMovement() {
        isMoving = true; // inicializa os movimentos

        moveCarsRunnable = new Runnable() {
            @Override
            public void run() {
                for (Carro carro : listaDeCarros) {
                    carro.checkSensor(pistaBitmap);
                    carro.andarFrente(10);
                }
                PaintCars();

                if (isMoving) {
                    handler.postDelayed(this, 100); // Atualiza a posição a cada 100ms
                }
            }
        };
        handler.post(moveCarsRunnable);
    }
    @Override
    protected void onPause() {
        super.onPause();
        isMoving = false; // Para o movimento
        handler.removeCallbacks(moveCarsRunnable); // Remove as atualizações
    }


    private void finishRace() {
        pista.setImageBitmap(pistaBitmap);
        onPause(); // Para o movimento dos carros
        relativeLayout.removeViews(7, relativeLayout.getChildCount() - 7); // Remove carros anteriores, se houver
        listaDeCarrosViews.clear(); // Limpa a lista de carros
        editTextNumber.setText(""); // Limpa o campo de entrada para novo valor
        Toast.makeText(this, "Corrida finalizada! Insira um novo número.", Toast.LENGTH_SHORT).show();
    }
}

