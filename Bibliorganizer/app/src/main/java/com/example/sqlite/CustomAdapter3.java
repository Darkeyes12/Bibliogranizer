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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.MyViewHolder> implements Filterable {

    private Context context;
    private ArrayList<String> id, titulo;

    private ArrayList<String> idFull, tituloFull;

    private String correo;




    CustomAdapter3(Context context, ArrayList<String> id, ArrayList<String> titulo, String correo){
        this.context = context;
        this.id = id;
        this.titulo = titulo;
        this.correo = correo;

        idFull = new ArrayList<>(id);
        tituloFull = new ArrayList<>(titulo);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row3, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.book_id.setText(String.valueOf(id.get(position)));
        holder.book_title.setText(String.valueOf(titulo.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity_Libreria.class);
                intent.putExtra("titulo", titulo.get(position));
                intent.putExtra("correo", correo);
                context.startActivity(intent);
            }
        });

        holder.deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro de que deseas borrar esta libreria?");

                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Database_Libros db = new Database_Libros(context);

                        // Actualizar todas las libros de la librería a "Ninguna"
                        db.updateLibreriasByCorreoAndLibreria(correo, titulo.get(position));

                        // Actualizar las listas de datos en el adaptador
                        id.remove(position);
                        titulo.remove(position);

                        // Notificar al adaptador del cambio
                        notifyDataSetChanged();
                        // Usar el contexto de la actividad para iniciar la nueva actividad
                        Intent intent = new Intent(context, Organizacion.class);
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
            ArrayList<String> filteredListTitulo = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredListId.addAll(idFull);
                filteredListTitulo.addAll(tituloFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (int i = 0; i < idFull.size(); i++) {
                    if (tituloFull.get(i).toLowerCase().contains(filterPattern)) {
                        filteredListId.add(idFull.get(i));
                        filteredListTitulo.add(tituloFull.get(i));
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredListId;
            id.clear();
            titulo.clear();
            id.addAll(filteredListId);
            titulo.addAll(filteredListTitulo);
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView book_id, book_author, book_title;


        ImageButton deletebutton;

        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_id = itemView.findViewById(R.id.book_id_organizacion_librerias);
            book_title = itemView.findViewById(R.id.book_title_organizacion_librerias);
            deletebutton = itemView.findViewById(R.id.imageButton_delete_organizacion_librerias);
            mainLayout = itemView.findViewById(R.id.mainLayout_organizacion_librerias);

        }
    }
}
