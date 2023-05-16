package features.membership;

import models.Member;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class List_Members {
    public List_Members() {
        int choice;
        // load list of members from JSON file
        List<Member> memberList = loadData();
        do {
            String title = "Perpustakaan XYZ\n\n";

            // display list of members
            String memberData = "";
            for (int i = 0; i < memberList.size(); i++) {
                memberData += (i + 1) + ". " + memberList.get(i) + "\n";
            }

            String menu = JOptionPane.showInputDialog(null, title + memberData + "\n0. Kembali", "List Anggota", JOptionPane.QUESTION_MESSAGE);

            // if cancel button is clicked, then exit the program
            if (menu == null) {
                System.exit(0);
            }

            choice = Integer.parseInt(menu);

            switch (choice) {
                case 0:
                    new Member_Management_Menu();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Pilihan tidak tersedia!", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } while (choice != 0);
    }

    private List<Member> loadData() {
        List<Member> memberList = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("src\\data\\members.json")) {
            JSONArray memberArray = (JSONArray) parser.parse(reader);

            for (Object memberObj : memberArray) {
                JSONObject memberJson = (JSONObject) memberObj;
                String name = (String) memberJson.get("name");
                String email = (String) memberJson.get("email");
                String phone = (String) memberJson.get("phone");
                String registered_at = (String) memberJson.get("registered_at");
                memberList.add(new Member(name, email, phone, registered_at));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memberList;
    }
}