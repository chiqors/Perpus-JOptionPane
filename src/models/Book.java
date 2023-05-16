package models;

public class Book {
    private int id;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublished() {
        return published;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int i) {
        this.stock = i;
    }
}
