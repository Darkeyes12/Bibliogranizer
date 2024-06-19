package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class Organizacion extends AppCompatActivity implements SearchView.OnQueryTextListener {

    String correo;

    ImageButton agregar;

    private EditText nombre;

    private Button cancelar,crear;

    Database_Libros db;

    ImageView empty_imageview;

    TextView no_data;

    RecyclerView recyclerView;
    ArrayList<String> id, titulo;

    SearchView txtBuscar;

    int cantidadLibrerias;

    CustomAdapter3 customAdapter;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organizacion);

        recyclerView = findViewById(R.id.recyclerView_organizacion);
        empty_imageview = findViewById(R.id.empty_imageview_organizacion);

        no_data = findViewById(R.id.textView_nodata_organizacion);



        TextView textView = findViewById(R.id.prueba_libros_organizacion);

        ImageButton regresar = findViewById(R.id.regresar_button_organizacion);

        //Button ninguno = findViewById(R.id.borrar_todo_organizacion);

        agregar = findViewById(R.id.agregar_button_organizacion);

        correo = getIntent().getStringExtra("correo");

        txtBuscar = findViewById(R.id.txtbuscar_organizacion);

        // Establecer el texto en el TextView
        textView.setText("Inicio de sesión con: " + correo);

        //Por si queda algun libro como pendiente
        db = new Database_Libros(Organizacion.this);
        db.updateLibreriasPendientes("Ninguna");


        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Organizacion.this, Seleccion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Organizacion.this, Crear_libreria.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        /*ninguno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Database_Libros(Organizacion.this);
                db.establecer_ninguna(correo);
                Intent intent = new Intent(Organizacion.this, Organizacion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });*/

        db = new Database_Libros(Organizacion.this);
        id = new ArrayList<>();
        titulo = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter3(Organizacion.this, id, titulo,correo);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Organizacion.this));

        txtBuscar.setOnQueryTextListener(this);

        textView.setText("Librerias en biblioteca: " + cantidadLibrerias);

    }

    void storeDataInArrays(){
        Set<String> libreriasSet = db.obtenerLibreriasPorCorreo(correo);
        cantidadLibrerias = libreriasSet.size();
        ArrayList<String> libreriasList = new ArrayList<>(libreriasSet);

        if(cantidadLibrerias == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            for (String libreria : libreriasList) {
                id.add(String.valueOf(id.size() + 1)); // Valor autoincremental iniciando desde 1
                titulo.add(libreria);
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        customAdapter.getFilter().filter(newText);
        return false;
    }
}


