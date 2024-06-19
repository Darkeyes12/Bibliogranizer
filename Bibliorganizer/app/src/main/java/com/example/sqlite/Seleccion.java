package com.example.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Seleccion extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);

        // Obtener el correo pasado como extra
        String correo = getIntent().getStringExtra("correo");

        // Buscar el TextView en tu layout
        ImageButton imgb1 = findViewById(R.id.imageButton_1);
        ImageButton imgb2 = findViewById(R.id.imageButton_2);
        Button cerrar_sesion = findViewById(R.id.btnCerrarSesion);
        // Establecer el texto en el TextView


        imgb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Seleccion.this, Libros.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        imgb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Seleccion.this, Organizacion.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Seleccion.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
