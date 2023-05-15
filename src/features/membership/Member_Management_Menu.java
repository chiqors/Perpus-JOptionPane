package features.membership;

import javax.swing.*;

public class Member_Management_Menu {
    public Member_Management_Menu() {
        int choice;
        do {
            String title = "Perpustakaan XYZ\n";
            String content = "1. List Anggota\n2. Tambah Anggota\n\n0. Kembali";
            String menu = JOptionPane.showInputDialog(null, title + content, "Pengelolaan Data Anggota", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (menu == null) {
                System.exit(0);
            }

            choice = Integer.parseInt(menu);

            switch (choice) {
                case 1:
                    new List_Members();
                    break;
                case 2:
                    new Add_Member();
                    break;
                case 0:
                    break;
            }
        } while (choice < 1 || choice > 2);
    }
}
