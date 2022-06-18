package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private Input(){}

    public static String readString(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("getMsg method error!");
            System.out.println("try again!");
            return readString();
        }
    }

    public static int readInt(){
        try {
            return Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            System.out.println("This will not work!");
            System.out.println("Try again!");
            return readInt();
        }
    }
}
