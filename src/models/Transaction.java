package models;

import java.util.ArrayList;

public class Transaction {
    private int id;
    private String borrowed_date;
    private String expected_return_date;
    private String status;
    private int member_id;
    private String member_name;
    private ArrayList<Book> borrowed_books;
    private String returned_date;

    public Transaction(int id, String borrowed_date, String expected_return_date, String status, int member_id, String member_name, ArrayList<Book> borrowed_books, String returned_date) {
        this.id = id;
        this.borrowed_date = borrowed_date;
        this.expected_return_date = expected_return_date;
        this.status = status;
        this.member_id = member_id;
        this.member_name = member_name;
        this.borrowed_books = borrowed_books;
        this.returned_date = returned_date;
    }

    public Transaction(int id, String borrowed_date, String expected_return_date, String status, int member_id, String member_name, ArrayList<Book> borrowed_books) {
        this.id = id;
        this.borrowed_date = borrowed_date;
        this.expected_return_date = expected_return_date;
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
                "\n# Tanggal Pinjam/Batas Pengembalian: " + borrowed_date + " - " + expected_return_date +
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
                "\n# Tanggal Pinjam: " + borrowed_date +
                "\n# Tanggal Batas Pengembalian: " + expected_return_date +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n\n# Buku yang dipinjam:\n" + stringBuilder.toString();
    }

    public String showMenuTransaction() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Book borrowedBook : borrowed_books) {
            stringBuilder.append("## "+borrowedBook.getName()).append("\n");
        }
        return "\n# ID: " + id +
                "\n# Tanggal Pinjam/Batas Pengembalian: " + borrowed_date + " - " + expected_return_date +
                "\n# Tanggal Pengembalian: " + returned_date +
                "\n# Status: " + status +
                "\n# Anggota: " + member_id + " - " + member_name +
                "\n# Buku yang dipinjam:\n" + stringBuilder.toString();
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

    public String getExpected_return_date() {
        return expected_return_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReturnedDate(String returned_date) {
        this.returned_date = returned_date;
    }
}
