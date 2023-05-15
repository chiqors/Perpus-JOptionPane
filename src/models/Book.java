package models;

public class Book {
    private String name;
    private String author;
    private String published;
    private int stock;

    public Book(String name, String author, String published, int stock) {
        this.name = name;
        this.author = author;
        this.published = published;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return name + " - " + author + " - " + published + " - " + stock;
    }
}
