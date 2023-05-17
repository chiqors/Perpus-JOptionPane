package features;

import javax.swing.*;

import config.Constant;
import features.borrowment.Borrow_Book;
import features.manage_books.Book_Management_Menu;
import features.membership.Member_Management_Menu;
import features.returnment.Return_Book;
import features.transaction.Transaction_List;

public class Main_Menu {
    public Main_Menu() {
        int choice = 0;
        do {
            String title = "Main Menu\n\n";
            String menuData = "1. Pengelolaan Data Buku\n2. Registrasi Member\n3. Peminjaman Buku\n4. Pengembalian Buku\n5. Transaksi\n\n0. Exit";
            String menu = JOptionPane.showInputDialog(null, title + menuData, Constant.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            // if cancel button is clicked, then exit the program
            if (menu == null) {
                System.exit(0);
            }
            choice = Integer.parseInt(menu);

            switch (choice) {
                case 1:
                    new Book_Management_Menu();
                    break;
                case 2:
                    new Member_Management_Menu();
                    break;
                case 3:
                    new Borrow_Book();
                    break;
                case 4:
                    new Return_Book();
                    break;
                case 5:
                    new Transaction_List();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice < 1 || choice > 6);
    }
}
