package com.example.sqlite;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class Lista extends AppCompatActivity {
    ListView lv ;
    ArrayList<String> lista;
    ArrayAdapter adaptador;

    public void mostrarDatos() {
        Database db = new Database(getApplicationContext(), null, null, 2);
        ArrayList<String> listaDatos = db.obtenerTodosLosDatos();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaDatos);

        ListView listView = findViewById(R.id.lista);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        mostrarDatos();
    }


}