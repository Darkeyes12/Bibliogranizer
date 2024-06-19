package com.example.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registrar extends AppCompatActivity {

    private Button registrar,cancelar;

    private EditText e1,e2,e3,e4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        registrar = (Button) findViewById(R.id.button_registrar);
        cancelar = (Button) findViewById(R.id.button_cancelar);
        e1 = (EditText) findViewById(R.id.editTextText_registrar);
        e2 = (EditText) findViewById(R.id.editTextText_registrar2);
        e3 = (EditText) findViewById(R.id.editTextText_registrar3);
        e4 = (EditText) findViewById(R.id.editTextText_registrar4);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(getApplicationContext(), null, null, 2);
                db.getWritableDatabase();
                String nombre = e1.getText().toString();
                String apellido = e2.getText().toString();
                String correo = e3.getText().toString();
                String contrasena = e4.getText().toString();

                if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    String mensaje =db.guardar(nombre,apellido,correo,contrasena);
                    if (mensaje.equals("Ingresado Correctamente")){
                        Toast.makeText(getApplicationContext(),"Registro Exitoso",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrar.this, Login.class);
                        startActivity(intent);
                    }
                     else{
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrar.this, Registrar.class);
                        startActivity(intent);
                    }

                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrar.this,Login.class);
                startActivity(intent);
            }
        });
    }
}
