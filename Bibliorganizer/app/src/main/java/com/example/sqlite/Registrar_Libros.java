package com.example.sqlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Registrar_Libros extends AppCompatActivity {

    private ImageButton imageButtonSelectImage;
    private ActivityResultLauncher<String> galleryLauncher;

    private ImageView imageView;
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private EditText editTextIsbn;
    private EditText editTextEditorial;
    private EditText editTextGenero;
    private EditText editTextSinopsis;
    private Button buttonCancelar;
    private Button buttonRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_libros);

        // Obtener el correo pasado como extra
        String correo = getIntent().getStringExtra("correo");

        imageButtonSelectImage = findViewById(R.id.imageButton3);
        imageView = findViewById(R.id.book_imageView);

        editTextTitulo = findViewById(R.id.editTextText_registrar_libro);
        editTextAutor = findViewById(R.id.editTextText_registrar_libro2);
        editTextIsbn = findViewById(R.id.editTextText_registrar_libro3);
        editTextEditorial = findViewById(R.id.editTextText_registrar_libro4);
        editTextGenero = findViewById(R.id.editTextText_registrar_libro5);
        editTextSinopsis = findViewById(R.id.editTextTextMultiLine);
        buttonCancelar = findViewById(R.id.button_cancelar);
        buttonRegistrar = findViewById(R.id.button_registrar);


        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Bitmap resizedBitmap = resizeImage(result, 400, 550);
                            if (resizedBitmap != null) {
                                imageButtonSelectImage.setImageBitmap(resizedBitmap);
                                // Guardar imageBytes en la base de datos en lugar de resizedBitmap
                            }
                        } else {
                            // Cargar imagen predefinida en caso de no seleccionar una imagen
                            imageButtonSelectImage.setImageResource(R.drawable.libro_predefinido);
                        }
                    }
                });

        imageButtonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });


        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrar_Libros.this, Libros.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Database_Libros db = new Database_Libros(Registrar_Libros.this);
                String titulo = editTextTitulo.getText().toString();
                String autor = editTextAutor.getText().toString();
                String isbn = editTextIsbn.getText().toString();
                String editorial = editTextEditorial.getText().toString();
                String genero = editTextGenero.getText().toString();
                String sinopsis = editTextSinopsis.getText().toString();

                // Verificar campos vacíos y establecer valor predeterminado para sinopsis
                if (titulo.trim().isEmpty() || autor.trim().isEmpty() || isbn.trim().isEmpty() || editorial.trim().isEmpty() || genero.trim().isEmpty()) {
                    Toast.makeText(Registrar_Libros.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sinopsis.trim().isEmpty()) {
                    sinopsis = "No especificado";
                }

                BitmapDrawable drawable = (BitmapDrawable) imageButtonSelectImage.getDrawable();
                Bitmap imageBitmap = drawable.getBitmap();
                byte[] imageBytes = bitmapToByteArray(imageBitmap);

                boolean insercionExitosa = db.guardarLibro(correo, imageBytes, titulo, autor, isbn, editorial, genero, sinopsis);

                if (insercionExitosa) {
                    Toast.makeText(Registrar_Libros.this, "Libro registrado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Registrar_Libros.this, Libros.class);
                    intent.putExtra("correo", correo); // Pasar el correo como extra
                    startActivity(intent);
                } else {
                    Toast.makeText(Registrar_Libros.this, "Error al registrar el libro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Bitmap resizeImage(Uri uri, int targetWidth, int targetHeight) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
