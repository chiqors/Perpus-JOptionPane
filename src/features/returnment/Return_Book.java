package features.returnment;

import features.Main_Menu;
import models.Book;
import models.Transaction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Return_Book {
    private List<Transaction> transactionBorrowedList;

    public Return_Book() {
        int choice;
        // load list of borrowed transaction from JSON file
        transactionBorrowedList = loadBorrowedTransaction();
        do {
            String title = "Perpustakaan XYZ\n\n";
            // display list of borrowed transaction
            String transactionData = "";
            for (int i = 0; i < transactionBorrowedList.size(); i++) {
                transactionData += (i + 1) + ". " + transactionBorrowedList.get(i).showMenuReturn() + "\n";
            }
            String ask = "Masukkan nomor transaksi yang ingin dikembalikan\n\n";
            String menu = JOptionPane.showInputDialog(null, title + transactionData + "\n0. Kembali\n\n" + ask, "Pengembalian", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to main menu
            if (menu == null) {
                choice = 0;
                break;
            }

            choice = getSelectedChoice(menu);
        } while (choice != 0);

        if (choice == 0) {
            new Main_Menu();
        }
    }

    private int getSelectedChoice(String menu) {
        int choice = 0;
        try {
            choice = Integer.parseInt(menu);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (choice == 0) {
            new features.Main_Menu();
        } else if (choice > 0 && choice <= transactionBorrowedList.size()) {
            Transaction transaction = transactionBorrowedList.get(choice - 1);
            returnBook(transaction, choice);
        } else {
            JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return choice;
    }

    private void returnBook(Transaction transaction, int choice) {
        int index = choice - 1;
        transaction.setStatus("returned");
        transaction.setReturnedDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        // Update the transaction in the list
        transactionBorrowedList.set(index, transaction);

        // Verify if the expected return date is after the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate returnedDate = LocalDate.parse(transaction.getExpected_return_date(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (returnedDate.isBefore(currentDate)) {
            // Calculate the fine
            long daysLate = currentDate.toEpochDay() - returnedDate.toEpochDay();
            double fine = daysLate * 5000; // Assuming the fine is 5000 per day

            // Display a JOptionPane dialog with the message and the fine
            JOptionPane.showMessageDialog(
                    null,
                    "Buku berhasil dikembalikan!\n\nTerlambat mengembalikan: " + daysLate + " hari\nDenda yang harus dibayar: Rp " + fine,
                    "Pengembalian",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(null, "Buku berhasil dikembalikan!", "Pengembalian", JOptionPane.INFORMATION_MESSAGE);
        }

        // Update transaction borrowed data into JSON transaction file
        updateTransaction(transaction);

        // reload list of borrowed transaction from JSON file
        transactionBorrowedList = loadBorrowedTransaction();
    }

    private void updateTransaction(Transaction transaction) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\transactions.json")) {
            JSONArray transactionArray = (JSONArray) parser.parse(reader);

            // Update the transaction data in the JSON file
            for (int i = 0; i < transactionArray.size(); i++) {
                JSONObject transactionObject = (JSONObject) transactionArray.get(i);

                // Find the transaction by ID
                int transactionId = Integer.parseInt(transactionObject.get("id").toString());
                if (transactionId == transaction.getId()) {
                    transactionObject.put("status", transaction.getStatus());
                    transactionObject.put("returned_date", transaction.getReturnedDate());

                    // Update the stock of borrowed books
                    JSONArray borrowedBooks = (JSONArray) transactionObject.get("borrowed_books");
                    for (Object borrowedBook : borrowedBooks) {
                        JSONObject borrowedBookObject = (JSONObject) borrowedBook;
                        int bookId = Integer.parseInt(borrowedBookObject.get("id").toString());

                        // Load books data from the books.json file
                        JSONArray booksArray;
                        try (FileReader booksReader = new FileReader("src\\data\\books.json")) {
                            booksArray = (JSONArray) parser.parse(booksReader);
                        }

                        // Update the stock of the book in the books.json file
                        for (Object book : booksArray) {
                            JSONObject bookObject = (JSONObject) book;
                            int bookIdFromJson = Integer.parseInt(bookObject.get("id").toString());

                            if (bookId == bookIdFromJson) {
                                int stock = Integer.parseInt(bookObject.get("stock").toString());
                                bookObject.put("stock", stock + 1);
                                break;
                            }
                        }

                        // Write the updated books data to the books.json file
                        try (FileWriter booksWriter = new FileWriter("src\\data\\books.json")) {
                            booksWriter.write(booksArray.toJSONString());
                            booksWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                }
            }

            // Write the updated transaction data to the JSON file
            try (FileWriter writer = new FileWriter("src\\data\\transactions.json")) {
                writer.write(transactionArray.toJSONString());
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> loadBorrowedTransaction() {
        List<Transaction> transactionBorrowedList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\transactions.json")) {
            JSONArray transactionList = (JSONArray) parser.parse(reader);

            for (Object transaction : transactionList) {
                JSONObject transactionObject = (JSONObject) transaction;
                if (transactionObject.get("status").equals("borrowed")) {
                    // get list of borrowed books from transaction (borrowed_books)
                    JSONArray borrowedBooks = (JSONArray) transactionObject.get("borrowed_books");
                    ArrayList<Book> bookList = new ArrayList<>();
                    for (Object borrowedBook : borrowedBooks) {
                        JSONObject borrowedBookObject = (JSONObject) borrowedBook;
                        bookList.add(new Book(
                            Integer.parseInt(borrowedBookObject.get("id").toString()),
                            borrowedBookObject.get("name").toString(),
                            borrowedBookObject.get("author").toString(),
                            borrowedBookObject.get("published").toString()
                        ));
                    }
                    transactionBorrowedList.add(new Transaction(
                        Integer.parseInt(transactionObject.get("id").toString()),
                        transactionObject.get("borrowed_date").toString(),
                        transactionObject.get("expected_return_date").toString(),
                        transactionObject.get("status").toString(),
                        Integer.parseInt(transactionObject.get("member_id").toString()),
                        transactionObject.get("member_name").toString(),
                        bookList
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactionBorrowedList;
    }
}
