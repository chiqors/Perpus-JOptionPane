package features;

import javax.swing.*;
import features.manage_books.Book_Management_Menu;
import features.membership.Member_Management_Menu;

public class Main_Menu {
    public Main_Menu() {
        int choice = 0;
        do {
            String title = "Perpustakaan XYZ\n";
            String menuData = "1. Pengelolaan Data Buku\n2. Registrasi Member\n3. Peminjaman Buku\n4. Pengembalian Buku\n5. Transaksi\n\n0. Exit";
            String menu = JOptionPane.showInputDialog(null, title + menuData, "Main Menu", JOptionPane.QUESTION_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Peminjaman Buku");
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "Pengembalian Buku");
                    break;
                case 5:
                    JOptionPane.showMessageDialog(null, "Transaksi");
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
