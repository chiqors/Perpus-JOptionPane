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
    public Borrow_Book() {
        int choice;
        // load list of books from JSON file
        List<Book> bookList = loadBook();
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

            choice = Integer.parseInt(menu);

            if (choice == 0) {
                new Main_Menu();
            } else if (choice > 0 && choice <= bookList.size()) {
                Book book = bookList.get(choice - 1);
                if (book.getStock() > 0) {
                    book.setStock(book.getStock() - 1);
                    bookList.set(choice - 1, book);

                    // save updated book list to JSON file
                    JSONArray bookArray = new JSONArray();
                    for (Book b : bookList) {
                        JSONObject bookJson = new JSONObject();
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

                    // save borrowed book to JSON file
                    JSONArray borrowedArray = new JSONArray();
                    try (FileReader reader = new FileReader("src\\data\\transactions.json")) {
                        // [
                        //  {
                        //    "id": 1,
                        //    "member_id": 1,
                        //    "member_name": "John Doe",
                        //    "status": "returned",
                        //    "borrowed_date": "2023-05-01T00:00:00.000Z",
                        //    "returned_date": "2023-05-15T00:00:00.000Z",
                        //    "borrowed_books": [
                        //      {
                        //        "id": 6,
                        //        "name": "The Lord of the Rings",
                        //        "author": "J.R.R. Tolkien",
                        //        "published": "2004-07-29"
                        //      },
                        //      {
                        //        "id": 5,
                        //        "name": "Learning Spring Boot 3.0",
                        //        "author": "Greg L. Turnquist",
                        //        "published": "2017-03-11"
                        //      }
                        //    ]
                        //  }
                        //]
                        borrowedArray = (JSONArray) new JSONParser().parse(reader);

                        // get last transaction id
                        int lastId = 0;
                        if (borrowedArray.size() > 0) {
                            JSONObject lastTransaction = (JSONObject) borrowedArray.get(borrowedArray.size() - 1);
                            lastId = Integer.parseInt(lastTransaction.get("id").toString());
                        }

                        // get member id and name
                        String memberId = JOptionPane.showInputDialog(null, "Masukkan ID member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);
                        String memberName = JOptionPane.showInputDialog(null, "Masukkan nama member", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

                        // get borrowed date
                        String borrowedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal peminjaman (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

                        // get returned date
                        String returnedDate = JOptionPane.showInputDialog(null, "Masukkan tanggal pengembalian (YYYY-MM-DD)", "Peminjaman", JOptionPane.QUESTION_MESSAGE);

                        // create borrowed books array
                        JSONArray borrowedBooksArray = new JSONArray();
                        JSONObject borrowedBookJson = new JSONObject();
                        borrowedBookJson.put("id", book.getId());
                        borrowedBookJson.put("name", book.getName());
                        borrowedBookJson.put("author", book.getAuthor());
                        borrowedBookJson.put("published", book.getPublished());
                        borrowedBooksArray.add(borrowedBookJson);

                        // create transaction object
                        JSONObject transactionJson = new JSONObject();
                        transactionJson.put("id", lastId + 1);
                        transactionJson.put("member_id", memberId);
                        transactionJson.put("member_name", memberName);
                        transactionJson.put("status", "borrowed");
                        transactionJson.put("borrowed_date", borrowedDate);
                        transactionJson.put("returned_date", returnedDate);
                        transactionJson.put("borrowed_books", borrowedBooksArray);

                        // add transaction object to borrowed array
                        borrowedArray.add(transactionJson);

                        // save borrowed array to JSON file
                        try (FileWriter file = new FileWriter("src\\data\\transactions.json")) {
                            file.write(borrowedArray.toJSONString());
                            file.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(null, "Buku berhasil dipinjam!", "Peminjaman", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Buku tidak tersedia!", "Peminjaman", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } while (choice != 0);
    }

    private List<Book> loadBook() {
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
}
