package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Filehandling {

    String fileName = null;

    public Filehandling(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
    }


    public static ArrayList<Account> readAccountsFromCSV(String FilePath) {

        ArrayList<Account> accounts = new ArrayList<>();

        try {

            int i = 0;
            BufferedReader br = Files.newBufferedReader(Paths.get(FilePath), StandardCharsets.US_ASCII);
            String l = br.readLine();

            if (i == 0) {
                l = br.readLine();
            }

            while (l != null) {


                String[] attributes = l.split(",");
                Account account = new Account(attributes[0], attributes[1], attributes[2], Boolean.valueOf(attributes[3]), Integer.parseInt(attributes[4]), attributes[5], Boolean.valueOf(attributes[6]), Boolean.valueOf(attributes[7]));
                accounts.add(account);
                l = br.readLine();
                i++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
