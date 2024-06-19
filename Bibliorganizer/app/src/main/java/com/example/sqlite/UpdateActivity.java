package com.example.sqlite;

import android.content.Intent;
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


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private ImageButton imageButtonSelectImage;
    private ActivityResultLauncher<String> galleryLauncher;

    private ImageView imageView;
    private EditText editTextTitulo;
    private EditText editTextAutor;
    private EditText editTextIsbn;
    private EditText editTextEditorial;
    private EditText editTextGenero;
    private EditText editTextSinopsis;
    private CheckBox editTextPrestado;
    private Button buttonCancelar;
    private Button buttonUpdate;

    Database_Libros db;

    String id;

    String correo;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        String id = getIntent().getStringExtra("id");

        this.id = id;

        imageButtonSelectImage = findViewById(R.id.imageButton3_actualizar);
        imageView = findViewById(R.id.book_imageView_actualizar);

        editTextTitulo = findViewById(R.id.editTextText_registrar_libro_actualizar);
        editTextAutor = findViewById(R.id.editTextText_registrar_libro2_actualizar);
        editTextIsbn = findViewById(R.id.editTextText_registrar_libro3_actualizar);
        editTextEditorial = findViewById(R.id.editTextText_registrar_libro4_actualizar);
        editTextGenero = findViewById(R.id.editTextText_registrar_libro5_actualizar);
        editTextSinopsis = findViewById(R.id.editTextTextMultiLine_actualizar);
        editTextPrestado = findViewById(R.id.checkBox_prestado);
        buttonCancelar = findViewById(R.id.button_cancelar_actualizar);
        buttonUpdate = findViewById(R.id.button_actualizar);

        db = new Database_Libros(UpdateActivity.this);

        Libro libro = db.getLibroById(Integer.parseInt(id));
        //Ahora tienes que escribir los datos en update

        byte[] imageByteArray = libro.getUriFoto();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        imageButtonSelectImage.setImageBitmap(bitmap);
        editTextTitulo.setText(libro.getTitulo());
        editTextAutor.setText(libro.getAutor());
        editTextIsbn.setText(libro.getIsbn());
        editTextEditorial.setText(libro.getEditorial());
        editTextGenero.setText(libro.getGenero());
        editTextSinopsis.setText(libro.getSinopsis());
        boolean isApartado = (libro.getApartado() == 1);
        editTextPrestado.setChecked(isApartado);


        correo = libro.getCorreo();

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, Libros.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database_Libros db = new Database_Libros(UpdateActivity.this);
                String titulo = editTextTitulo.getText().toString();
                String autor = editTextAutor.getText().toString();
                String isbn = editTextIsbn.getText().toString();
                String editorial = editTextEditorial.getText().toString();
                String genero = editTextGenero.getText().toString();
                String sinopsis = editTextSinopsis.getText().toString();
                String libreria = "Ninguna";

                        BitmapDrawable drawable = (BitmapDrawable) imageButtonSelectImage.getDrawable();
                Bitmap imageBitmap = drawable.getBitmap();
                byte[] imageBytes = bitmapToByteArray(imageBitmap);

                // Obtener el valor del CheckBox y convertirlo a un entero (0 o 1)
                int apartado = editTextPrestado.isChecked() ? 1 : 0;

                if (apartado == 1){
                    libreria = "Apartado";
                }

                db.actualizar(id, correo, imageBytes, titulo, autor, isbn, editorial, genero, sinopsis, libreria, apartado);

                Intent intent = new Intent(UpdateActivity.this, Libros.class);
                intent.putExtra("correo", correo); // Pasar el correo como extra
                startActivity(intent);
            }
        });

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
