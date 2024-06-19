package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UpdateActivity_Libreria extends AppCompatActivity {


    ArrayList<String> id, titulo, autor;

    ArrayList<byte[]> imagen;
    Database_Libros db;

    String libreria;

    String correo;

    EditText nombre_libreria;

    Button cancelar,actualizar;

    ImageView empty_imageview;

    TextView no_data;

    CustomAdapter4 customAdapter;

    RecyclerView recyclerView;

    ImageButton agregar_libros;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_libreria);

        libreria = getIntent().getStringExtra("titulo");
        correo = getIntent().getStringExtra("correo");

        nombre_libreria = findViewById(R.id.editTextText_nombre_organizacion_update);

        nombre_libreria.setText(libreria);

        cancelar = findViewById(R.id.button_cancelar_organizacion_update);
        actualizar = findViewById(R.id.button_agregar_organizacion_update);

        empty_imageview = findViewById(R.id.empty_imageview_crear_libreria_update);
        no_data = findViewById(R.id.textView_nodata_crear_libreria_update);

        recyclerView = findViewById(R.id.recyclerView_crear_libreria_update);

        agregar_libros = findViewById(R.id.agregar_button_update);

        agregar_libros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity_Libreria.this, UpdateLibros_Libreria.class);
                intent.putExtra("titulo", libreria);
                intent.putExtra("correo", correo);
                startActivity(intent);
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity_Libreria.this, Organizacion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String libreria_nueva = nombre_libreria.getText().toString();
                if (libreria_nueva.equals(libreria)){
                    Intent intent = new Intent(UpdateActivity_Libreria.this, Organizacion.class);
                    intent.putExtra("correo", correo);
                    startActivity(intent);
                }
                else{
                    // Actualizar los libros de esa librería por el nuevo nombre
                    db = new Database_Libros(UpdateActivity_Libreria.this);
                    db.updateNombre_librerias(correo, libreria,libreria_nueva);

                    Intent intent = new Intent(UpdateActivity_Libreria.this, Organizacion.class);
                    intent.putExtra("correo", correo);
                    startActivity(intent);
                }

            }
        });


        db = new Database_Libros(UpdateActivity_Libreria.this);
        id = new ArrayList<>();
        titulo = new ArrayList<>();
        autor = new ArrayList<>();
        imagen = new ArrayList<byte[]>();

        storeDataInArrays();

        customAdapter = new CustomAdapter4(UpdateActivity_Libreria.this, id, imagen, titulo, autor,correo,libreria);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateActivity_Libreria.this));
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData_update_libreria(correo,libreria);
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

