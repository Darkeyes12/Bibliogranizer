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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<String> id, titulo, autor;
    private ArrayList<byte[]> imagen;

    private ArrayList<String> idFull, tituloFull, autorFull;
    private ArrayList<byte[]> imagenFull;

    private String correo;

    CustomAdapter(Context context, ArrayList<String> id, ArrayList<byte[]> imagen, ArrayList<String> titulo,
                  ArrayList<String> autor,String correo){
        this.context = context;
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.autor = autor;
        this.correo = correo;

        // Copiar los datos originales a las listas de respaldo
        idFull = new ArrayList<>(id);
        imagenFull = new ArrayList<>(imagen);
        tituloFull = new ArrayList<>(titulo);
        autorFull = new ArrayList<>(autor);
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
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(position)));
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de que deseas borrar este libro?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Obtener el ID del libro a eliminar
                        String bookId = String.valueOf(id.get(position));

                        // Eliminar el registro de la base de datos
                        Database_Libros db = new Database_Libros(context);
                        db.deleteBook(bookId);

                        // Actualizar las listas de datos en el adaptador
                        id.remove(position);
                        imagen.remove(position);
                        titulo.remove(position);
                        autor.remove(position);

                        // Notificar al adaptador del cambio
                        notifyDataSetChanged();
                        Intent intent = new Intent(context, Libros.class);
                        intent.putExtra("correo", correo); // Pasar el correo como extra
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredListId = new ArrayList<>();
            ArrayList<byte[]> filteredListImagen = new ArrayList<>();
            ArrayList<String> filteredListTitulo = new ArrayList<>();
            ArrayList<String> filteredListAutor = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredListId.addAll(idFull);
                filteredListImagen.addAll(imagenFull);
                filteredListTitulo.addAll(tituloFull);
                filteredListAutor.addAll(autorFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (int i = 0; i < idFull.size(); i++) {
                    if (tituloFull.get(i).toLowerCase().contains(filterPattern) || autorFull.get(i).toLowerCase().contains(filterPattern)) {
                        filteredListId.add(idFull.get(i));
                        filteredListImagen.add(imagenFull.get(i));
                        filteredListTitulo.add(tituloFull.get(i));
                        filteredListAutor.add(autorFull.get(i));
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredListId;
            imagen.clear();
            id.clear();
            titulo.clear();
            autor.clear();
            imagen.addAll(filteredListImagen);
            id.addAll(filteredListId);
            titulo.addAll(filteredListTitulo);
            autor.addAll(filteredListAutor);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_id, book_author, book_title;

        ImageView book_imageView;

        LinearLayout mainLayout;

        ImageButton deleteButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.book_id);
            book_author = itemView.findViewById(R.id.book_author);
            book_title = itemView.findViewById(R.id.book_title);
            book_imageView = itemView.findViewById(R.id.book_imageView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            deleteButton = itemView.findViewById(R.id.imageButton_delete);
        }
    }
}
