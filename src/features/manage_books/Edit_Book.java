package features.manage_books;

import models.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Edit_Book {
    public Edit_Book() {
        // load list of books from JSON file
        List<Book> bookList = loadData();
        boolean continueEditing = true; // Flag to control the loop

        while (continueEditing) {
            String title = "Perpustakaan XYZ\n\n";
            String content = "List Buku\n\n";

            // display list of books
            for (int i = 0; i < bookList.size(); i++) {
                content += (i + 1) + ". " + bookList.get(i) + "\n";
            }
            content += "\n";

            String askId = "Masukkan ID buku yang ingin diubah";
            String askName = "Masukkan nama buku";

            String id = JOptionPane.showInputDialog(null, title + content + askId, "Ubah Buku", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (id == null) {
                System.exit(0);
            }

            // search book by Id
            int bookIndex = -1;
            try {
                bookIndex = Integer.parseInt(id) - 1;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Masukkan ID yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (bookIndex < 0 || bookIndex >= bookList.size()) {
                JOptionPane.showMessageDialog(null, "ID buku tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // now bookList.get(bookIndex) gives you the book with the specified ID
            String bookName = JOptionPane.showInputDialog(null, title + content + askName, "Ubah Buku", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (bookName == null) {
                System.exit(0);
            }

            // edit book
            doEditBook(bookName, bookIndex + 1);

            // ask if the user wants to continue editing
            int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin mengubah buku lain?", "Ubah Buku", JOptionPane.YES_NO_OPTION);
            continueEditing = (choice == JOptionPane.YES_OPTION);
        }
    }

    private List<Book> loadData() {
        List<Book> bookList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\books.json")) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            for (Object bookObj : bookArray) {
                JSONObject bookJson = (JSONObject) bookObj;
                String name = (String) bookJson.get("name");
                String author = (String) bookJson.get("author");
                String published = (String) bookJson.get("published");
                int stock = Integer.parseInt(bookJson.get("stock").toString());
                bookList.add(new Book(name, author, published, stock));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public void doEditBook(String bookName, int bookId) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\books.json")) {
            JSONArray bookArray = (JSONArray) parser.parse(reader);

            // parse bookArray.get(bookId - 1) to get name, author, published, and stock
            JSONObject bookJson = (JSONObject) bookArray.get(bookId - 1);
            String author = (String) bookJson.get("author");
            String published = (String) bookJson.get("published");
            int stock = Integer.parseInt(bookJson.get("stock").toString());

            JSONObject insetBook = new JSONObject();
            insetBook.put("name", bookName);
            insetBook.put("author", author);
            insetBook.put("published", published);
            insetBook.put("stock", stock);
            bookArray.set(bookId - 1, insetBook);

            try (FileWriter file = new FileWriter("src\\data\\books.json")) {
                file.write(bookArray.toJSONString());
                file.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

