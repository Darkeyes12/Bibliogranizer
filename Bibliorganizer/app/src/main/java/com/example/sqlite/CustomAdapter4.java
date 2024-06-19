package com.example.sqlite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter4 extends RecyclerView.Adapter<CustomAdapter4.MyViewHolder> {

    private Context context;
    private ArrayList<String> id, titulo, autor;
    private ArrayList<byte[]> imagen;

    private String correo,libreria;





    CustomAdapter4(Context context, ArrayList<String> id, ArrayList<byte[]> imagen, ArrayList<String> titulo,
                   ArrayList<String> autor, String correo, String libreria){
        this.context = context;
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.autor = autor;
        this.correo = correo;
        this.libreria = libreria;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_id.setText(String.valueOf(id.get(position)));
        holder.book_title.setText(String.valueOf(titulo.get(position)));
        holder.book_author.setText(String.valueOf(autor.get(position)));

        // Convertir byte[] a Bitmap
        byte[] byteArray = (byte[]) imagen.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        holder.book_imageView.setImageBitmap(bitmap);

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de que deseas borrar este libro de la libreria?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String bookId = id.get(position);
                        Database_Libros db = new Database_Libros(context);
                        db.updateLibreria(bookId, "Ninguna");

                        // Actualizar las listas de datos en el adaptador
                        id.remove(position);
                        imagen.remove(position);
                        titulo.remove(position);
                        autor.remove(position);

                        // Notificar al adaptador del cambio
                        notifyDataSetChanged();
                        // Usar el contexto de la actividad para iniciar la nueva actividad
                        Intent intent = new Intent(context, UpdateActivity_Libreria.class);
                        intent.putExtra("titulo", libreria);
                        intent.putExtra("correo", correo);
                        context.startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // No hacer nada, simplemente cerrar el diálogo
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_id, book_author, book_title;

        ImageView book_imageView;


        ImageButton deletebutton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.book_id);
            book_author = itemView.findViewById(R.id.book_author);
            book_title = itemView.findViewById(R.id.book_title);
            book_imageView = itemView.findViewById(R.id.book_imageView);
            deletebutton = itemView.findViewById(R.id.imageButton_delete);
        }
    }
}
