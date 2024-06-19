package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Crear_libreria extends AppCompatActivity {


    private EditText nombre;

    private Button cancelar,crear;

    Database_Libros db;

    String correo;

    ImageView empty_imageview;

    TextView no_data;

    RecyclerView recyclerView;
    ArrayList<String> id, titulo, autor;

    ArrayList<byte[]> imagen;

    CustomAdapter2 customAdapter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_libreria);

        recyclerView = findViewById(R.id.recyclerView_crear_libreria);
        empty_imageview = findViewById(R.id.empty_imageview_crear_libreria);

        no_data = findViewById(R.id.textView_nodata_crear_libreria);

        correo = getIntent().getStringExtra("correo");

        nombre = findViewById(R.id.editTextText_nombre_organizacion);
        cancelar = findViewById(R.id.button_cancelar_organizacion);
        crear =  findViewById(R.id.button_crear_organizacion);



        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Database_Libros(Crear_libreria.this);
                db.updateLibreriasPendientes("Ninguna");
                Intent intent = new Intent(Crear_libreria.this, Organizacion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = nombre.getText().toString();

                // Verificar que el campo nombre no esté vacío
                if (text.trim().isEmpty() || text.equalsIgnoreCase("Ninguna") || text.equalsIgnoreCase("Apartado")) {
                    Toast.makeText(Crear_libreria.this, "Error, pruebe con otro nombre", Toast.LENGTH_SHORT).show();
                }
                else{
                    db = new Database_Libros(Crear_libreria.this);
                    db.updateLibreriasPendientes(text);
                    Intent intent = new Intent(Crear_libreria.this, Organizacion.class);
                    intent.putExtra("correo", correo); // Pasar el correo como extra
                    startActivity(intent);
                }

            }
        });

        // Crear una instancia de Database_Libros
        db = new Database_Libros(Crear_libreria.this);
        id = new ArrayList<>();
        titulo = new ArrayList<>();
        autor = new ArrayList<>();
        imagen = new ArrayList<byte[]>();

        // Llamar a storeDataInArrays() después de crear la instancia
        storeDataInArrays();

        customAdapter = new CustomAdapter2(Crear_libreria.this, id, imagen, titulo, autor,correo);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Crear_libreria.this));




    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData_crear_libreria(correo);
        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                byte[] imageBytes = cursor.getBlob(2);
                imagen.add(imageBytes);
                titulo.add(cursor.getString(3));
                autor.add(cursor.getString(4));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }
}
