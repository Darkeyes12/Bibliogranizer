package com.example.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Registrar extends AppCompatActivity {

    private Button registrar,cancelar;

    private EditText e1,e2,e3,e4;

    private boolean isValidEmail(String email) {
        // Using Android's built-in Patterns class for email validation
        // This checks for proper email format like: user@example.com
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateForm(String nombre, String apellido, String correo, String contrasena) {
        // Check for empty fields
        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Check email format
        if (!isValidEmail(correo)) {
            Toast.makeText(getApplicationContext(), "Por favor, ingrese un correo electrónico válido (ej: usuario@ejemplo.com)", Toast.LENGTH_LONG).show();
            return false;
        }
        
        // Additional validation: Check password length (optional security improvement)
        if (contrasena.length() < 6) {
            Toast.makeText(getApplicationContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }


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
                // Get input values from EditText fields
                String nombre = e1.getText().toString().trim();
                String apellido = e2.getText().toString().trim();
                String correo = e3.getText().toString().trim();
                String contrasena = e4.getText().toString().trim();

                // Validate all form fields using our validation method
                if (validateForm(nombre, apellido, correo, contrasena)) {
                    // All validations passed, proceed with database operation
                    Database db = new Database(getApplicationContext(), null, null, 2);
                    db.getWritableDatabase();
                    
                    String mensaje = db.guardar(nombre, apellido, correo, contrasena);
                    if (mensaje.equals("Ingresado Correctamente")) {
                        Toast.makeText(getApplicationContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrar.this, Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Registrar.this, Registrar.class);
                        startActivity(intent);
                    }
                }
                // If validation fails, the validateForm method already shows appropriate error messages
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
