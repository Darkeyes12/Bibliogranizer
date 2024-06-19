package com.example.sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {

    private Context context;
    private ArrayList<String> id, titulo, autor;
    private ArrayList<byte[]> imagen;

    private String correo;



    CustomAdapter2(Context context, ArrayList<String> id, ArrayList<byte[]> imagen, ArrayList<String> titulo,
                  ArrayList<String> autor, String correo){
        this.context = context;
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.autor = autor;
        this.correo = correo;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row2, parent, false);
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

        holder.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Actualizar el estado del libro a "pendiente" en la base de datos
                String bookId = id.get(position);
                Database_Libros db = new Database_Libros(context);
                db.updateLibreria(bookId, "pendiente");

                // Actualizar las listas de datos en el adaptador
                id.remove(position);
                imagen.remove(position);
                titulo.remove(position);
                autor.remove(position);

                // Notificar al adaptador del cambio
                notifyDataSetChanged();

                // Mostrar un Toast indicando que el libro ha sido agregado
                Toast.makeText(context, "Libro agregado", Toast.LENGTH_SHORT).show();
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


        ImageButton addbutton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.book_id_organizacion);
            book_author = itemView.findViewById(R.id.book_author_organizacion);
            book_title = itemView.findViewById(R.id.book_title_organizacion);
            book_imageView = itemView.findViewById(R.id.book_imageView_organizacion);
            addbutton = itemView.findViewById(R.id.imageButton_add_organizacion);
        }
    }
}
