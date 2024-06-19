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

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Prueba", factory, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table usuario (nombre text, apellido text, correo text, contrasena text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(db);
    }

    public void borrar_todo(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM usuario");
        database.close();
    }
    public String guardar(String nombre, String apellido, String correo, String contrasena){
        String mensaje="";
        SQLiteDatabase database = this.getWritableDatabase();

        // Consultar si ya existe un registro con el mismo correo electrónico
        Cursor cursor = database.rawQuery("SELECT * FROM usuario WHERE correo=?", new String[]{correo});
        if(cursor.getCount() > 0){
            mensaje = "Correo electrónico duplicado, por favor ingrese otro correo.";
        } else {
            ContentValues contenedor = new ContentValues();
            contenedor.put("nombre",nombre);
            contenedor.put("apellido", apellido);
            contenedor.put("correo", correo);
            contenedor.put("contrasena", contrasena);
            try {
                database.insertOrThrow("usuario",null,contenedor);
                mensaje="Ingresado Correctamente";
            }catch (SQLException e){
                mensaje="No Ingresado";
            }
        }

        cursor.close();
        database.close();
        return mensaje;
    }
    public String actualizar(String Buscar, String Nombre, String Apellido, String Correo, String Contrasena){
        String Mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contenedor = new ContentValues();
        contenedor.put("nombre", Nombre);
        contenedor.put("apellido", Apellido);
        contenedor.put("correo", Correo);
        contenedor.put("contrasena", Contrasena);
        int cantidad = database.update("usuario", contenedor, "nombre='" + Buscar + "'", null);
        if(cantidad!=0){
            Mensaje="Actualizado Correctamente";
        }else{
            Mensaje="No Actualizado";
        }
        database.close();
        return Mensaje;
    }

    public String[] buscar_reg(String buscar) {
        String[] datos = new String[5];
        SQLiteDatabase database = this.getReadableDatabase();
        String q = "SELECT * FROM usuario WHERE correo = ?";
        Cursor registros = database.rawQuery(q, new String[]{buscar});
        if (registros.moveToFirst()) {
            datos[0] = registros.getString(0); // Obtener el nombre
            datos[1] = registros.getString(1); // Obtener el apellido
            datos[2] = registros.getString(2); // Obtener el correo
            datos[3] = registros.getString(3); // Obtener la contraseña
            datos[4] = "Encontrado";
        } else {
            datos[4] = "No se encontró a " + buscar;
        }
        registros.close();
        return datos;
    }

    public String eliminar(String Nombre){
        String mensaje ="";
        SQLiteDatabase database = this.getWritableDatabase();
        int cantidad =database.delete("usuario", "nombre='" + Nombre + "'", null);
        if (cantidad !=0){
            mensaje="Eliminado Correctamente";

        }
        else{
            mensaje = "No existe";
        }
        database.close();
        return mensaje;
    }
    public ArrayList<String> llenar_lv(){
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        String q = "SELECT * FROM usuario";
        Cursor registros = database.rawQuery(q, null);
        if(registros.moveToFirst()){
            do{
                String fila = "";
                for(int i = 0; i < registros.getColumnCount(); i++){
                    fila += registros.getString(i) + " ";
                }
                lista.add(fila.trim());
            }while(registros.moveToNext());
        }
        return lista;
    }

    public ArrayList<String> obtenerTodosLosDatos() {
        ArrayList<String> listaDatos = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

        // Consulta para obtener todos los datos de la tabla "datos"
        String query = "SELECT * FROM usuario";
        Cursor cursor = database.rawQuery(query, null);

        // Iterar sobre el cursor para obtener los datos
        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            String apellido = cursor.getString(cursor.getColumnIndex("apellido"));
            String correo = cursor.getString(cursor.getColumnIndex("correo"));
            String contrasena = cursor.getString(cursor.getColumnIndex("contrasena"));

            // Agregar los datos a la lista
            listaDatos.add("Nombre: " + nombre + ", Apellido: " + apellido + ", Correo: " + correo + ", Contraseña: " + contrasena);
        }

        // Cerrar el cursor y la base de datos
        cursor.close();
        database.close();

        return listaDatos;
    }

}