package features.borrowment;

import features.Main_Menu;
import models.Book;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

            // display list of books
            String bookData = "";
            for (int i = 0; i < bookList.size(); i++) {
                bookData += (i + 1) + ". " + bookList.get(i) + "\n";
            }

            // Get member information
            String memberId = JOptionPane.showInputDialog(null, "Masukkan ID member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);
            String memberName = JOptionPane.showInputDialog(null, "Masukkan nama member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

            // Get borrowed and returned dates
            String borrowedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal peminjaman (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);
            String returnedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal pengembalian (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

            // Create borrowed books array
            JSONArray borrowedBooksArray = new JSONArray();

            // Add the first borrowed book
            borrowedBooksArray.add(createBorrowedBookObject(book));

            // Ask if member wants to borrow another book
            int confirm = JOptionPane.showConfirmDialog(null, "Apakah member ingin meminjam buku lain?", "Peminjaman", JOptionPane.YES_NO_OPTION);

            // If member wants to borrow another book, keep adding books to the array
            while (confirm == JOptionPane.YES_OPTION) {
                int bookChoice = getSelectedChoice(JOptionPane.showInputDialog(null, bookData + "\n0. Kembali\n\n" + "Masukkan nomor buku yang ingin dipinjam", "Peminjaman", JOptionPane.QUESTION_MESSAGE)) - 1;
                if (bookChoice == -1) {
                    break;
                }
                Book newBook = bookList.get(bookChoice);
                newBook.setStock(newBook.getStock() - 1);
                saveBookListToJson();
                borrowedBooksArray.add(createBorrowedBookObject(newBook));

                confirm = JOptionPane.showConfirmDialog(null, "Apakah member ingin meminjam buku lain?", "Peminjaman", JOptionPane.YES_NO_OPTION);
            }

            // Create transaction object
            JSONObject transactionJson = createTransactionObject(lastId, memberId, memberName, borrowedDate, returnedDate, borrowedBooksArray);

            // Save the transaction object to the JSON array
            borrowedArray.add(transactionJson);

            // Save the updated JSON array to the file
            try (FileWriter writer = new FileWriter("src\\data\\transactions.json")) {
                writer.write(borrowedArray.toJSONString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject createBorrowedBookObject(Book book) {
        JSONObject bookJson = new JSONObject();
        bookJson.put("author", book.getAuthor());
        bookJson.put("name", book.getName());
        bookJson.put("id", book.getId());
        bookJson.put("published", book.getPublished());
        return bookJson;
    }

    private int getLastTransactionId(JSONArray borrowedArray) {
        int lastId = 0;
        if (borrowedArray.size() > 0) {
            JSONObject lastTransaction = (JSONObject) borrowedArray.get(borrowedArray.size() - 1);
            lastId = Integer.parseInt(lastTransaction.get("id").toString());
        }
        return lastId;
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