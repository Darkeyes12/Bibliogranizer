package com.example.sqlite;
public class Libro {
    private int id;
    private String correo;
    private byte[] uriFoto;
    private String titulo;
    private String autor;
    private String isbn;
    private String editorial;
    private String genero;
    private String sinopsis;
    private String libreria;
    private int apartado;

    public Libro(int id, String correo, byte[] uriFoto, String titulo, String autor, String isbn, String editorial, String genero, String sinopsis, String libreria, int apartado) {
        this.id = id;
        this.correo = correo;
        this.uriFoto = uriFoto;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.editorial = editorial;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.libreria = libreria;
        this.apartado = apartado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public byte[] getUriFoto() {
        return uriFoto;
    }

    public void setUriFoto(byte[] uriFoto) {
        this.uriFoto = uriFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getLibreria() {
        return libreria;
    }

    public void setLibreria(String libreria) {
        this.libreria = libreria;
    }

    public int getApartado() {
        return apartado;
    }

    public void setApartado(int apartado) {
        this.apartado = apartado;
    }
}
