package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpdateLibros_Libreria extends AppCompatActivity {


    private TextView nombre;

    private Button cancelar,actualizar;


    Database_Libros db;

    String correo,libreria;

    ImageView empty_imageview;

    TextView no_data;

    RecyclerView recyclerView;
    ArrayList<String> id, titulo, autor;

    ArrayList<byte[]> imagen;

    CustomAdapter2 customAdapter;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_libros);

        recyclerView = findViewById(R.id.recyclerView_actulizar_libros);
        empty_imageview = findViewById(R.id.empty_imageview_actualizar_libreria);

        no_data = findViewById(R.id.textView_nodata_actualizar_libreria);

        libreria = getIntent().getStringExtra("titulo");
        correo = getIntent().getStringExtra("correo");

        nombre = findViewById(R.id.titulo_libreria_agregar);
        cancelar = findViewById(R.id.button_cancelar_actualizar);
        actualizar =  findViewById(R.id.button_actualizar_libros);

        nombre.setText(libreria);



        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Database_Libros(UpdateLibros_Libreria.this);
                db.updateLibreriasPendientes("Ninguna");
                Intent intent = new Intent(UpdateLibros_Libreria.this, Organizacion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    db = new Database_Libros(UpdateLibros_Libreria.this);
                    db.updateLibreriasPendientes(libreria);
                    Intent intent = new Intent(UpdateLibros_Libreria.this, Organizacion.class);
                    intent.putExtra("correo", correo); // Pasar el correo como extra
                    startActivity(intent);
            }
        });

        // Crear una instancia de Database_Libros
        db = new Database_Libros(UpdateLibros_Libreria.this);
        id = new ArrayList<>();
        titulo = new ArrayList<>();
        autor = new ArrayList<>();
        imagen = new ArrayList<byte[]>();

        // Llamar a storeDataInArrays() después de crear la instancia
        storeDataInArrays();

        customAdapter = new CustomAdapter2(UpdateLibros_Libreria.this, id, imagen, titulo, autor,correo);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateLibros_Libreria.this));




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
