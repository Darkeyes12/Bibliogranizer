package com.example.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Libros extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;

    SearchView txtBuscar;
    ArrayList<String> id, titulo, autor;

    ArrayList<byte[]> imagen;

    CustomAdapter customAdapter;

    Database_Libros db;

    String correo;

    ImageView empty_imageview;

    TextView no_data;

    int cantLibros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);

        recyclerView = findViewById(R.id.recyclerView);

        txtBuscar = findViewById(R.id.txtbuscar);

        // Buscar el TextView en tu layout
        TextView textView = findViewById(R.id.prueba_libros);

        ImageButton agregar = findViewById(R.id.agregar_button_update);

        ImageButton regresar = findViewById(R.id.regresar_button);

        empty_imageview = findViewById(R.id.empty_imageview);

        no_data = findViewById(R.id.textView_nodata);

        //Button borrar_todo = findViewById(R.id.borrar_todo);
        // Obtener el correo pasado como extra

        String correo = getIntent().getStringExtra("correo");

        this.correo = correo;
        // Establecer el texto en el TextView
        textView.setText("Inicio de sesión con: " + correo);


        /*borrar_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new Database_Libros(Libros.this);
                db.borrar_todo();
            }
        });*/

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Libros.this, Registrar_Libros.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Libros.this, Seleccion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        // Crear una instancia de Database_Libros
        db = new Database_Libros(Libros.this);
        id = new ArrayList<>();
        titulo = new ArrayList<>();
        autor = new ArrayList<>();
        imagen = new ArrayList<byte[]>();

        // Llamar a storeDataInArrays() después de crear la instancia
        storeDataInArrays();

        customAdapter = new CustomAdapter(Libros.this, id, imagen, titulo, autor,correo);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Libros.this));

        txtBuscar.setOnQueryTextListener(this);

        textView.setText("Libros en biblioteca: " + cantLibros);
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData(correo);
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
            cantLibros = cursor.getCount();
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

