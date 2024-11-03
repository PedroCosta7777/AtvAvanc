package com.example.atvavanc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.automath.Constants;
import com.example.automath.interfaces.MainToCar;
import com.example.database.Database;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements MainToCar {

    private ImageView pista;
    private EditText editTextNumber;
    private Button startButton, pauseButton, finishButton, clearButton;
    private ProgressBar progressbar;
    private List<Carro> listaDeCarros;
    private Handler handler;
    private Runnable moveCarsRunnable;
    private boolean isMoving;

    private Bitmap pistaBitmap; // Bitmap da pista
    private Bitmap dataBitmap;
    private Bitmap carroBmp; // Bitmap da pista

    Semaphore semaforo = new Semaphore(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Verifique se o nome do seu layout está correto

        editTextNumber = findViewById(R.id.editTextNumber);
        pista = findViewById(R.id.pista); // Certifique-se de ter a referência da pista

        // Inicializar as listas
        listaDeCarros = new ArrayList<>();

        // Carregar a imagem da pista como Bitmap
        pistaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pista1);
        dataBitmap = pistaBitmap.copy(pistaBitmap.getConfig(), true);
        carroBmp = BitmapFactory.decodeResource(getResources(), R.drawable.carro);
        pista.setImageBitmap(pistaBitmap);

        //Inicializa os botões
        startButton = findViewById(R.id.button);
        pauseButton = findViewById(R.id.button2);
        finishButton = findViewById(R.id.button3);
        clearButton = findViewById(R.id.button4);
        progressbar = findViewById(R.id.progress);

        funcaoBotoes();
        buscaCarrosBanco();
    }

    private void loading(boolean active) {
        finishButton.setEnabled(!active);
        startButton.setEnabled(!active);
        pauseButton.setEnabled(!active);
        clearButton.setEnabled(!active);
        if (active)
            progressbar.setVisibility(View.VISIBLE);
        else
            progressbar.setVisibility(View.GONE);
    }

    private void buscaCarrosBanco() {
        loading(true);
        Database.getInstance().BuscarCarros()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("DATABASE", document.getId() + " => " + document.getData());
                            try {
                                Carro car;
                                if (document.getData().get("name").equals("SafetyCar"))
                                    car = new SafetyCar(document.getData());
                                else
                                    car = new Carro(document.getData());
                                car.setMain(this);
                                listaDeCarros.add(car);
                            } catch (Exception e) {
                                Log.d("CRIAR CARROS", e.getMessage());
                            }
                        }
                        loading(false);
                        PaintCars();
                    } else {
                        Log.d("DATABASE", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void PaintCars() {
        Bitmap bitmap = pistaBitmap.copy(pistaBitmap.getConfig(), true);
        dataBitmap =  pistaBitmap.copy(pistaBitmap.getConfig(), true);
        Bitmap car = carroBmp.copy(carroBmp.getConfig(), true);

        Canvas canvas = new Canvas(bitmap);
        Canvas dataCanvas = new Canvas(dataBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        for (Carro c : listaDeCarros) {
            Rect rect  = c.getRect();
            paint.setColor(Color.BLACK);

            canvas.rotate(c.getAngulo(), c.getX(), c.getY());
            canvas.drawBitmap(car, null, rect, paint);
            canvas.rotate(-c.getAngulo(), c.getX(), c.getY());

            dataCanvas.rotate(c.getAngulo(), c.getX(), c.getY());
            dataCanvas.drawRect(rect, paint);
            dataCanvas.rotate(-c.getAngulo(), c.getX(), c.getY());

            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            for (Integer s : c.getSensor().keySet()) {
                Pair<Integer, Integer> pos = c.getSensorPoint(s);
                if (pos != null)
                    canvas.drawLine(c.getX(), c.getY(), pos.first, pos.second, paint);
            }
        }
        pista.setImageBitmap(bitmap);
    }


    private void funcaoBotoes(){
        startButton.setOnClickListener(v -> {
            if (listaDeCarros.isEmpty())
                addCars();
            startCarMovement();
        });

        // Botão Pause
        pauseButton.setOnClickListener(v -> {
            onPause(); // Pausa o movimento dos carros
        });

        clearButton.setOnClickListener( v -> {
            Database.getInstance().LimparCarros();
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

            SafetyCar safety = new SafetyCar("SafetyCar", 0, (int)(pistaBitmap.getWidth() * 0.82), pistaBitmap.getHeight() / 2 - 150);
            safety.setMain(this);
            listaDeCarros.add(safety);

            int mid_y = pistaBitmap.getHeight() / 2;
            int mid_x = (int)(pistaBitmap.getWidth() * 0.80);

            for (int i = 0; i < numberOfCars; i++) {
                Carro novoCarro = new Carro("Carro " + (i + 1), i+1, mid_x + i*(Constants.width + 40), mid_y);
                novoCarro.setMain(this);
                listaDeCarros.add(novoCarro);
            }
        } else {
            Toast.makeText(this, "Por favor, digite um número!", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCarMovement() {
        isMoving = true; // inicializa os movimentos

        for (Carro car : listaDeCarros) {
            car.start();
        }

        moveCarsRunnable = new Runnable() {
            @Override
            public void run() {
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
        for (Carro c : listaDeCarros) {
            try {
                c.stopCar();
                c.join();
            } catch (Exception e) {
                Log.d("Exeption", e.toString());
            }
        }
        ;
        Database.getInstance().SalvarCarros(listaDeCarros.stream().map(Carro::toMap).collect(Collectors.toList()));
    }

    @Override
    public Semaphore getSemaforo() {
        return semaforo;
    }

    @Override
    public Bitmap getPista() {
        return dataBitmap;
    }

    private void finishRace() {
        pista.setImageBitmap(pistaBitmap);
        onPause(); // Para o movimento dos carros
        editTextNumber.setText(""); // Limpa o campo de entrada para novo valor
        Toast.makeText(this, "Corrida finalizada! Insira um novo número.", Toast.LENGTH_SHORT).show();
    }
}

