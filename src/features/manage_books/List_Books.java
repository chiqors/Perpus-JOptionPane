package features.manage_books;

import config.Constant;
import models.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class List_Books {
    public List_Books() {
        int choice = 0;
        // load list of books from JSON file
        List<Book> bookList = loadData();
        do {
            String title = "Daftar Buku\n\n";
            // display list of books
            String bookData = "";
            for (int i = 0; i < bookList.size(); i++) {
                bookData += (i + 1) + ". " + bookList.get(i) + "\n";
            }
            String menu = JOptionPane.showInputDialog(null, title + bookData + "\n0. Kembali", Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to Book Management Menu
            if (menu == null) {
                choice = 0;
                break;
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);

        if (choice == 0) {
            new Book_Management_Menu();
        }
    }

    private List<Book> loadData() {
        List<Book> bookList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\books.json")) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (Object bookObj : bookArray) {
                JSONObject bookJson = (JSONObject) bookObj;
                int id = Integer.parseInt(bookJson.get("id").toString());
                String name = (String) bookJson.get("name");
                String author = (String) bookJson.get("author");
                String published = (String) bookJson.get("published");
                int stock = Integer.parseInt(bookJson.get("stock").toString());
                bookList.add(new Book(id, name, author, published, stock));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }
}
