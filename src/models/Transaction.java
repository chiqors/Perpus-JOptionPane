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

    public Transaction(String borrowed_date, String returned_date, String status, int member_id, String member_name, ArrayList<Book> borrowed_books) {
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
        return "Transaction{" + "id=" + id + ", borrowed_date=" + borrowed_date + ", returned_date=" + returned_date + ", status=" + status + ", member_id=" + member_id + ", member_name=" + member_name + ", borrowed_books=" + borrowed_books + '}';
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
}