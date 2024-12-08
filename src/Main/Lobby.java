package Main;

import java.io.IOException;
import Wordle.Wordle;

public class Lobby {
    //Code to clear the terminal
    public static void clearTerminal() {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            if (os.contains("win")) {
                //windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //Linux/mac
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            clearTerminal();
        }
    }

    public static void main (String[] args) {
        Wordle.showMenu();
    }
}
