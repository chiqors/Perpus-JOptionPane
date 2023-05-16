package models;

import java.util.ArrayList;

public class Transaction {
    private int id;
    private String borrowed_date;
    private String returned_date;
    private String status;
    private int member_id;
    private String member_name;
    private ArrayList<Book> borrowed_books;

    public Transaction(int id, String borrowed_date, String returned_date, String status, int member_id, String member_name, ArrayList<Book> borrowed_books) {
        this.id = id;
        this.borrowed_date = borrowed_date;
        this.returned_date = returned_date;
        this.status = status;
        this.member_id = member_id;
        this.member_name = member_name;
        this.borrowed_books = borrowed_books;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append(borrowedBook.getName()).append("\n");
        }
        return "\n# ID: " + id +
                "\n# Tanggal Pinjam/Kembali: " + borrowed_date + " - " + returned_date +
                "\n# Status: " + status +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n# Buku yang dipinjam:\n" + stringBuilder.toString();
    }

    public String showMenuReturn() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append("## "+borrowedBook.getName()).append("\n");
        }
        return "\n# ID: " + id +
                "\n# Tanggal Pinjam/Kembali: " + borrowed_date + " - " + returned_date +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n# Buku yang dipinjam:\n" + stringBuilder.toString();
    }

    public String toJSONString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append(borrowedBook.getName()).append("\n");
        }
        return "{" +
                "\"id\":" + id +
                ", \"borrowed_date\":\"" + borrowed_date + "\"" +
                ", \"returned_date\":\"" + returned_date + "\"" +
                ", \"status\":\"" + status + "\"" +
                ", \"member_id\":" + member_id +
                ", \"member_name\":\"" + member_name + "\"" +
                ", \"borrowed_books\":\"" + stringBuilder.toString() + "\"" +
                "}";
    }

    public int getId() {
        return id;
    }

    public String getBorrowedDate() {
        return borrowed_date;
    }

    public String getReturnedDate() {
        return returned_date;
    }

    public String getStatus() {
        return status;
    }

    public int getMemberId() {
        return member_id;
    }

    public String getMemberName() {
        return member_name;
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowed_books;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
