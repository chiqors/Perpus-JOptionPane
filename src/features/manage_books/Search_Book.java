package features.manage_books;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;

public class Search_Book {
    public Search_Book() {
        int choice = 0;
        // load search result from JSON file
        String bookData = DoSearchBook(JOptionPane.showInputDialog(null, "Masukkan nama buku yang ingin dicari:", "Cari Buku", JOptionPane.QUESTION_MESSAGE));
        do {
            String title = "Perpustakaan XYZ\n\n";
            String menu = JOptionPane.showInputDialog(null, title + bookData + "\n0. Kembali", "Cari Buku", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (menu == null) {
                System.exit(0);
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    new Book_Management_Menu();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);
    }

    public String DoSearchBook(String bookName) {
        // search book by name
        String bookData = "";
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\books.json")) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (Object bookObj : bookArray) {
                JSONObject bookJson = (JSONObject) bookObj;
                String name = (String) bookJson.get("name");
                String author = (String) bookJson.get("author");
                String published = (String) bookJson.get("published");
                int stock = Integer.parseInt(bookJson.get("stock").toString());

                if (name.toLowerCase().contains(bookName.toLowerCase())) {
                    bookData += "Nama Buku: " + name + "\n";
                    bookData += "Penulis: " + author + "\n";
                    bookData += "Tahun Terbit: " + published + "\n";
                    bookData += "Stok: " + stock + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookData;
    }
}