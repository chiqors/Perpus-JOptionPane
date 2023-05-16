package models;

public class Book {
    private int id;
    private String name;
    private String author;
    private String published;
    private int stock;

    public Book(int id, String name, String author, String published, int stock) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published = published;
        this.stock = stock;
    }

    public Book(int id, String name, String author, String published) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.published = published;
    }

    @Override
    public String toString() {
        return name + " - " + author + " - " + published + " - " + stock;
    }

    public String toJSONString() {
        return "{" +
                "\"id\":" + id +
                ", \"name\":\"" + name + "\"" +
                ", \"author\":\"" + author + "\"" +
                ", \"published\":\"" + published + "\"" +
                ", \"stock\":" + stock +
                "}";
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
