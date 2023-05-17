package features.manage_books;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;

public class Add_Book {
    public Add_Book() {
        int choice;
        do {
            String title = "Perpustakaan XYZ\n\n";
            String askName = "Masukkan nama buku";
            String askAuthor = "Masukkan nama pengarang";
            String askPublished = "Masukkan tahun terbit";
            String askStock = "Masukkan jumlah stok";

            // get input from user
            String name = JOptionPane.showInputDialog(null, title + askName, "Tambah Buku", JOptionPane.QUESTION_MESSAGE);
            String author = JOptionPane.showInputDialog(null, title + askAuthor, "Tambah Buku", JOptionPane.QUESTION_MESSAGE);
            String published = JOptionPane.showInputDialog(null, title + askPublished, "Tambah Buku", JOptionPane.QUESTION_MESSAGE);
            String stock = JOptionPane.showInputDialog(null, title + askStock, "Tambah Buku", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Book Management Menu
            if (name == null || author == null || published == null || stock == null) {
                choice = 1;
                break;
            }

            // add book to JSON file
            DoAddBook(name, author, published, Integer.parseInt(stock));

            // ask user if they want to add another book
            String AddMore = JOptionPane.showConfirmDialog(null, title + "Tambah buku lagi?", "Tambah Buku", JOptionPane.YES_NO_OPTION) + "";
            choice = Integer.parseInt(AddMore);

            switch (choice) {
                case 0:
                case 1:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia! Dikembalikan ke menu awal", "Error", JOptionPane.ERROR_MESSAGE);
                    new Book_Management_Menu();
                    break;
            }
        } while (choice != 1);

        if (choice == 1) {
            new Book_Management_Menu();
        }
    }

    public void DoAddBook(String name, String author, String published, int stock) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\books.json")) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            JSONObject bookJson = new JSONObject();
            bookJson.put("id", bookArray.size() + 1); // id is auto-incremented (1, 2, 3, ...
            bookJson.put("name", name);
            bookJson.put("author", author);
            bookJson.put("published", published);
            bookJson.put("stock", stock);
            bookArray.add(bookJson);

            // write to JSON file
            FileWriter file = new FileWriter("src\\data\\books.json");
            file.write(bookArray.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
