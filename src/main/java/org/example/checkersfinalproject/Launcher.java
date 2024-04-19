package org.example.checkersfinalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Launcher {
    public static void main(String[] args){
        FileWriter writer = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\org\\example\\checkersfinalproject\\openings.txt"));
            System.out.println(reader.readLine());
            reader.close();
        }
        catch (Exception e) {
            System.out.println("An error occurred.");
        }

        CheckersBoard.main(args);
    }
}
