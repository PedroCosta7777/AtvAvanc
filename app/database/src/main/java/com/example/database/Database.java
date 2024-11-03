package com.example.database;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.Map;

public class Database {
    private static Database _instance = null;

    private final FirebaseFirestore db;

    private Database() {
        db = FirebaseFirestore.getInstance();
    }

    public void LimparCarros() {
        db.collection("CARROS").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("CARROS").document(document.getId()).delete();
                        }
                    } else {
                        Log.d("DATABASE", "Error getting documents: ", task.getException());
                    }
                });
    }

    public static Database getInstance() {
        if (_instance == null)
            _instance = new Database();
        return _instance;
    }

    public Task<QuerySnapshot> BuscarCarros() {
        return db.collection("CARROS").get();
    }

    public void SalvarCarros(List<Map<String, Object>> carros) {
        for (Map<String, Object> c : carros) {
            db.collection("CARROS")
                    .document(((Integer) c.get("idCarro")).toString())
                    .set(c)
                    .addOnSuccessListener(documentReference -> Log.d("DATABASE", "Carro adicionado no banco id="+c.get("idCarro")))
                    .addOnFailureListener(e -> Log.w("DATABASE", "Error adding document", e));
        }
    }
}
