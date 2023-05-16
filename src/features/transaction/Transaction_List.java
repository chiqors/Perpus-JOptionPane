package features.transaction;

import features.Main_Menu;
import models.Book;
import models.Transaction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Transaction_List {
    private List<Transaction> transactionList;

    public Transaction_List() {
        int choice;
        transactionList = loadAllTransaction();
        do {
            String title = "Perpustakaan XYZ\n\n";
            // display a list of transactions
            String transactionData = "";
            for (Transaction transaction : transactionList) {
                transactionData += transaction.toString();
            }
            String menu = JOptionPane.showInputDialog(null, title + transactionData + "\n\n0. Kembali", "Daftar Transaksi", JOptionPane.PLAIN_MESSAGE);

            // if user click cancel button
            if (menu == null) {
                choice = 0;
            }

            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    new Main_Menu();
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);
    }

    public List<Transaction> loadAllTransaction() {
        List<Transaction> transactionList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\transactions.json")) {
            JSONArray transactionArray = (JSONArray) parser.parse(reader);

            for (Object transaction : transactionArray) {
                JSONObject transactionObject = (JSONObject) transaction;

                // get list of borrowed books from transaction (borrowed_books)
                JSONArray borrowedBooks = (JSONArray) transactionObject.get("borrowed_books");
                ArrayList<Book> borrowedBookList = new ArrayList<>();
                for (Object borrowedBook : borrowedBooks) {
                    JSONObject borrowedBookObject = (JSONObject) borrowedBook;
                    borrowedBookList.add(new Book(
                            Integer.parseInt(borrowedBookObject.get("id").toString()),
                            borrowedBookObject.get("name").toString(),
                            borrowedBookObject.get("author").toString(),
                            borrowedBookObject.get("published").toString()
                    ));
                }

                transactionList.add(new Transaction(
                        Integer.parseInt(transactionObject.get("id").toString()),
                        transactionObject.get("borrowed_date").toString(),
                        transactionObject.get("returned_date").toString(),
                        transactionObject.get("status").toString(),
                        Integer.parseInt(transactionObject.get("member_id").toString()),
                        transactionObject.get("member_name").toString(),
                        borrowedBookList
                ));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return transactionList;
    }
}
