package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {


    private Button b,b2; /*boton*/

    private EditText e1; /*usuario*/

    private EditText e2; /*contraseña*/

    private boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        e1 = (EditText) findViewById(R.id.editTextText);
        e2 = (EditText) findViewById(R.id.editTextText2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String correo = e1.getText().toString().trim();
                String contrasena = e2.getText().toString().trim();

                if (correo.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email format before proceeding
                if (!isValidEmail(correo)) {
                    Toast.makeText(Login.this, "Por favor, ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Buscar en la base de datos si existe una entrada con el mismo correo y contraseña
                Database db = new Database(getApplicationContext(),null,null,2);
                String[] datos = db.buscar_reg(correo);


                if (correo.equals("12345") && contrasena.equals("12345")) {
                    db.borrar_todo();
                    Toast.makeText(Login.this, "Todos los datos borrados", Toast.LENGTH_SHORT).show();
                } else {
                    if (datos[2].equals(correo) && datos[3].equals(contrasena)) {
                        // Si las credenciales son correctas, cambiar a la siguiente actividad
                        Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Seleccion.class);
                        intent.putExtra("correo", correo); // Pasar el correo como extra
                        startActivity(intent);
                    } else {
                        // Si las credenciales son incorrectas, mostrar un mensaje al usuario
                        Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
            }
        });
    }


}