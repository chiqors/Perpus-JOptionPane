package features.membership;

import config.Constant;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileReader;
import java.io.FileWriter;

public class Add_Member {
    public Add_Member() {
        int choice;
        do {
            String title = "Tambah Anggota\n\n";
            String askName = "Masukkan nama anggota";
            String askEmail = "Masukkan email anggota";
            String askPhone = "Masukkan nomor telepon anggota";

            // get input from user
            String name = JOptionPane.showInputDialog(null, title + askName, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (name == null) {
                choice = 2;
                break;
            }
            String email = JOptionPane.showInputDialog(null, title + askEmail, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (email == null) {
                choice = 2;
                break;
            }
            String phone = JOptionPane.showInputDialog(null, title + askPhone, Constant.APP_NAME, JOptionPane.QUESTION_MESSAGE);
            if (phone == null) {
                choice = 2;
                break;
            }

            // add member to JSON file
            DoAddMember(name, email, phone);

            // ask user if they want to add another member
            String AddMore = JOptionPane.showInputDialog(null, title + "Tambah anggota lagi?\n1. Ya\n2. Tidak", "Tambah Member", JOptionPane.QUESTION_MESSAGE);
            choice = Integer.parseInt(AddMore);

            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia! Dikembalikan ke menu awal", Constant.APP_NAME, JOptionPane.ERROR_MESSAGE);
                    new Member_Management_Menu();
                    break;
            }
        } while (choice != 2);

        if (choice == 2) {
            new Member_Management_Menu();
        }
    }

    public void DoAddMember(String name, String email, String phone) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\members.json")) {
            JSONArray memberArray = (JSONArray) parser.parse(reader);

            JSONObject memberJson = new JSONObject();
            memberJson.put("id", memberArray.size() + 1); // id is auto-incremented (1, 2, 3, ...
            memberJson.put("name", name);
            memberJson.put("email", email);
            memberJson.put("phone", phone);
            memberJson.put("registered_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            memberArray.add(memberJson);

            // write to JSON file
            try (FileWriter file = new FileWriter("src\\data\\members.json")) {
                file.write(memberArray.toJSONString());
                file.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
