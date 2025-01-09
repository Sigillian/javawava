package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        clearTerminal();
        System.out.println("Pick:\n1 - wordle\n2-TheFactory");
        int choice = Integer.parseInt(System.console().readLine());
        if(choice == 1) {
            Wordle.showMenu();
        }else if(choice == 2) {
            TheFactory.Headquarters.run();
        }
    }
}
