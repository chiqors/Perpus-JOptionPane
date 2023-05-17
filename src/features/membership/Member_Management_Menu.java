package features.membership;

import features.Main_Menu;

import javax.swing.*;

public class Member_Management_Menu {
    public Member_Management_Menu() {
        int choice;
        do {
            String title = "Perpustakaan XYZ\n";
            String content = "1. List Anggota\n2. Tambah Anggota\n\n0. Kembali";
            String menu = JOptionPane.showInputDialog(null, title + content, "Pengelolaan Data Anggota", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then return to main menu
            if (menu == null) {
                choice = 0;
                break;
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

        if (choice == 0) {
            new Main_Menu();
        }
    }
}
