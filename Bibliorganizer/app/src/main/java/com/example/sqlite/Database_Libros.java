package com.example.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Database_Libros extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LibrosDB";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    public Database_Libros(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_LIBROS = "CREATE TABLE Libros (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "correo TEXT," +
                "uri_foto BLOB," +  // Columna para almacenar la imagen como blob
                "titulo TEXT," +
                "autor TEXT," +
                "isbn TEXT," +
                "editorial TEXT," +
                "genero TEXT," +
                "sinopsis TEXT," +
                "libreria TEXT DEFAULT 'Ninguna'," +
                "apartado INTEGER DEFAULT 0" +
                ")";
        db.execSQL(CREATE_TABLE_LIBROS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Libros");
        onCreate(db);
    }

    public void borrar_todo() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM Libros");
        database.close();
        Toast.makeText(context, "Se eliminaron todos los libros", Toast.LENGTH_SHORT).show();
    }

    public void establecer_ninguna(String correo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libreria", "Ninguna");

        String whereClause = "correo = ?";
        String[] whereArgs = { correo };

        int rowsAffected = database.update("Libros", values, whereClause, whereArgs);
        database.close();

        if (rowsAffected > 0) {
            Toast.makeText(context, "Se actualizaron los libros correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "No se encontraron libros para actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean guardarLibro(String correo, byte[] uriFoto, String titulo, String autor, String isbn, String editorial, String genero, String sinopsis) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo", correo);
        values.put("uri_foto", uriFoto);
        values.put("titulo", titulo);
        values.put("autor", autor);
        values.put("isbn", isbn);
        values.put("editorial", editorial);
        values.put("genero", genero);
        values.put("sinopsis", sinopsis);
        long result = database.insert("Libros", null, values);
        database.close();
        return result != -1; // Devuelve true si la inserción fue exitosa, false en caso contrario
    }

    public ArrayList<String> obtenerLibros() {
        ArrayList<String> libros = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query("Libros", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String libro = "Título: " + cursor.getString(3) +
                        "\nAutor: " + cursor.getString(4) +
                        "\nISBN: " + cursor.getString(5) +
                        "\nEditorial: " + cursor.getString(6) +
                        "\nGénero: " + cursor.getString(7) +
                        "\nSinopsis: " + cursor.getString(8);
                libros.add(libro);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return libros;
    }

    void actualizar(String id, String correo, byte[] uriFoto, String titulo, String autor, String isbn, String editorial, String genero, String sinopsis, String libreria, int apartado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("correo", correo);
        values.put("uri_foto", uriFoto);
        values.put("titulo", titulo);
        values.put("autor", autor);
        values.put("isbn", isbn);
        values.put("editorial", editorial);
        values.put("genero", genero);
        values.put("sinopsis", sinopsis);
        values.put("libreria", libreria);
        values.put("apartado", apartado);

        String selection = "id = ?";
        String[] selectionArgs = { id };

        int count = db.update("Libros", values, selection, selectionArgs);

        if (count > 0) {
            Toast.makeText(context, "Libro actualizado correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al actualizar el libro", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    void updateLibreria(String id,String estado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libreria", estado);

        String selection = "id = ?";
        String[] selectionArgs = {id};

        db.update("Libros", values, selection, selectionArgs);

    }

    void updateLibreriasPendientes(String nuevoValor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libreria", nuevoValor);

        String selection = "libreria = ?";
        String[] selectionArgs = {"pendiente"};

        db.update("Libros", values, selection, selectionArgs);

        db.close();
    }

    public void updateLibreriasByCorreoAndLibreria(String correo, String tituloLibreria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libreria", "Ninguna");

        String whereClause = "correo = ? AND libreria = ?";
        String[] whereArgs = {correo, tituloLibreria};

        int rowsUpdated = db.update("Libros", values, whereClause, whereArgs);
        db.close();

        if (rowsUpdated > 0) {
            // Se actualizaron correctamente los libros
        } else {
            // No se encontraron libros para actualizar
        }
    }

    public void updateNombre_librerias(String correo, String tituloLibreria, String nuevoNombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("libreria", nuevoNombre);

        String whereClause = "correo = ? AND libreria = ?";
        String[] whereArgs = {correo, tituloLibreria};

        int rowsUpdated = db.update("Libros", values, whereClause, whereArgs);
        db.close();

        if (rowsUpdated > 0) {
            // Se actualizaron correctamente los libros
        } else {
            // No se encontraron libros para actualizar
        }
    }



    Cursor readAllData(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "id",
                "correo",
                "uri_foto",
                "titulo",
                "autor",
                "isbn",
                "editorial",
                "genero",
                "sinopsis",
                "libreria",
                "apartado"
        };
        String selection = "correo = ?";
        String[] selectionArgs = {correo};

        Cursor cursor = db.query(
                "Libros",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    Cursor readAllData_crear_libreria(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "id",
                "correo",
                "uri_foto",
                "titulo",
                "autor",
                "isbn",
                "editorial",
                "genero",
                "sinopsis",
                "libreria",
                "apartado"
        };
        String selection = "correo = ? AND libreria = ?";
        String[] selectionArgs = {correo, "Ninguna"};

        Cursor cursor = db.query(
                "Libros",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    Cursor readAllData_update_libreria(String correo, String libreria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "id",
                "correo",
                "uri_foto",
                "titulo",
                "autor",
                "isbn",
                "editorial",
                "genero",
                "sinopsis",
                "libreria",
                "apartado"
        };
        String selection = "correo = ? AND libreria = ?";
        String[] selectionArgs = {correo, libreria}; // Utilizar libreria en lugar de "Ninguna"

        Cursor cursor = db.query(
                "Libros",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor;
    }

    public Libro getLibroById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Libro libro = null;

        String[] projection = {
                "id",
                "correo",
                "uri_foto",
                "titulo",
                "autor",
                "isbn",
                "editorial",
                "genero",
                "sinopsis",
                "libreria",
                "apartado"
        };
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                "Libros",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            libro = new Libro(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getBlob(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getInt(10)
            );
        }

        cursor.close();
        db.close();
        return libro;
    }

    public void deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Libros", "id=?", new String[]{bookId});
        if(result == -1){
            Toast.makeText(context, "Error al borrar.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Libro borrado correctamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public Set<String> obtenerLibreriasPorCorreo(String correo) {
        Set<String> librerias = new HashSet<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta para obtener las librerías asociadas al correo específico, excluyendo "Ninguna" y "Apartado"
        String[] projection = { "libreria" };
        String selection = "correo = ? AND libreria != ? AND libreria != ?";
        String[] selectionArgs = { correo, "Ninguna", "Apartado" };

        Cursor cursor = db.query("Libros", projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String libreria = cursor.getString(cursor.getColumnIndex("libreria"));
                librerias.add(libreria);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return librerias;
    }
}



