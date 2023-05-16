package features.borrowment;

import features.Main_Menu;
import models.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Borrow_Book {
    private List<Book> bookList;

    public Borrow_Book() {
        int choice;
        // load list of books from JSON file
        bookList = loadBook();
        do {
            String title = "Perpustakaan XYZ\n\n";
            // display list of books
            String bookData = "";
            for (int i = 0; i < bookList.size(); i++) {
                bookData += (i + 1) + ". " + bookList.get(i) + "\n";
            }
            String ask = "Masukkan nomor buku yang ingin dipinjam\n\n";
            String menu = JOptionPane.showInputDialog(null, title + bookData + "\n0. Kembali\n\n" + ask, "Peminjaman", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (menu == null) {
                System.exit(0);
            }

            choice = getSelectedChoice(menu);

            if (choice == 0) {
                new Main_Menu();
            } else if (choice > 0 && choice <= bookList.size()) {
                Book book = bookList.get(choice - 1);
                System.out.println(book);
                if (book.getStock() > 0) {
                    borrowBook(book, choice);
                } else {
                    JOptionPane.showMessageDialog(null, "Buku tidak tersedia!", "Peminjaman", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (choice != 0);
    }

    private int getSelectedChoice(String menu) {
        int choice = 0;
        try {
            choice = Integer.parseInt(menu);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return choice;
    }

    private void borrowBook(Book book, int choice) {
        book.setStock(book.getStock() - 1);
        bookList.set(choice - 1, book);

        // Save updated book list to JSON file
        saveBookListToJson();

        // Save borrowed book to JSON file
        saveBorrowedBookToJson(book);

        JOptionPane.showMessageDialog(null, "Buku berhasil dipinjam!", "Peminjaman", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveBookListToJson() {
        JSONArray bookArray = new JSONArray();
        for (Book b : bookList) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("id", b.getId());
            bookJson.put("name", b.getName());
            bookJson.put("author", b.getAuthor());
            bookJson.put("published", b.getPublished());
            bookJson.put("stock", b.getStock());
            bookArray.add(bookJson);
        }

        try (FileWriter file = new FileWriter("src\\data\\books.json")) {
            file.write(bookArray.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBorrowedBookToJson(Book book) {
        JSONArray borrowedArray = new JSONArray();
        try (FileReader reader = new FileReader("src\\data\\transactions.json")) {
            borrowedArray = (JSONArray) new JSONParser().parse(reader);
            // Get last transaction id
            int lastId = getLastTransactionId(borrowedArray);

            // Get member information
            String memberId = JOptionPane.showInputDialog(null, "Masukkan ID member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);
            String memberName = JOptionPane.showInputDialog(null, "Masukkan nama member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

            // Get borrowed and returned dates
            String borrowedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal peminjaman (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);
            String returnedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal pengembalian (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

            // Create borrowed books array
            JSONArray borrowedBooksArray = createBorrowedBooksArray(book);

            // Create transaction object
            JSONObject transactionJson = createTransactionObject(lastId, memberId, memberName, borrowedDate, returnedDate, borrowedBooksArray);

            // Add transaction object to borrowed array
            borrowedArray.add(transactionJson);

            // Save borrowed array to JSON file
            saveBorrowedArrayToJson(borrowedArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLastTransactionId(JSONArray borrowedArray) {
        int lastId = 0;
        if (borrowedArray.size() > 0) {
            JSONObject lastTransaction = (JSONObject) borrowedArray.get(borrowedArray.size() - 1);
            lastId = Integer.parseInt(lastTransaction.get("id").toString());
        }
        return lastId;
    }

    private JSONArray createBorrowedBooksArray(Book book) {
        JSONArray borrowedBooksArray = new JSONArray();
        JSONObject borrowedBookJson = new JSONObject();
        borrowedBookJson.put("id", book.getId());
        borrowedBookJson.put("name", book.getName());
        borrowedBookJson.put("author", book.getAuthor());
        borrowedBookJson.put("published", book.getPublished());
        borrowedBooksArray.add(borrowedBookJson);
        return borrowedBooksArray;
    }

    private JSONObject createTransactionObject(int lastId, String memberId, String memberName, String borrowedDate, String returnedDate, JSONArray borrowedBooksArray) {
        JSONObject transactionJson = new JSONObject();
        transactionJson.put("id", lastId + 1);
        transactionJson.put("member_id", memberId);
        transactionJson.put("member_name", memberName);
        transactionJson.put("status", "borrowed");
        transactionJson.put("borrowed_date", borrowedDate);
        transactionJson.put("returned_date", returnedDate);
        transactionJson.put("borrowed_books", borrowedBooksArray);
        return transactionJson;
    }

    private void saveBorrowedArrayToJson(JSONArray borrowedArray) {
        try (FileWriter file = new FileWriter("src\\data\\transactions.json")) {
            file.write(borrowedArray.toJSONString());
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Book> loadBook() {
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