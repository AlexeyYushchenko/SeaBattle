package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FileIO {
    private static final String FILENAME = "gamesettings.txt";

    private FileIO(){}

    public static String[] loadGameSettings(){
        try {
            List<String> strings = Files.readAllLines(Path.of(FILENAME));
            return strings.get(0).split(",");
        } catch (IOException e) {
            System.out.println("readFile method Error!");
            System.out.println(e.getMessage());
        }
        return new String[0];
    }

    public static void saveResults(int playerScore, int computerScore){
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            String result = localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ": Player wins. Final Score Player (" + playerScore + ") and Computer ("+ computerScore +")\n";
            Files.writeString(Path.of("gameoutcome.txt"), result, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("writeResults method Error!");
            System.out.println(e.getMessage());
        }
    }
}
