package Wordle;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import Main.Lobby;

public class Wordle {
    private final static Scanner userIn = new Scanner(System.in);

    //pick a word from the wordlist.txt file
    private static char[] getFromWordlist()  {
        ArrayList<String> wordList = new ArrayList<>();
        try {
            File wordListFile = new File("src/Wordle/wordlist.txt");
            if(wordListFile.exists()) {
                char[] fileContents = new Scanner(wordListFile).nextLine().toCharArray();
                for(int i = 0; i < fileContents.length-1; i++) {
                    String wordToAdd = "";
                    while(fileContents[i] != ',' && i < fileContents.length-1) {
                        wordToAdd += fileContents[i];
                        i++;
                    }
                    wordList.add(wordToAdd);
                }
            }
            return wordList.get(new Random().nextInt(wordList.size())).toCharArray();
        }
        catch (Exception e) {
            return "sorry".toCharArray();
        }
    }


    //Get any length response from the user
    private static char[] getResponse(int length){
        char[] r = userIn.next().toCharArray();
        if(r.length == length)
            return r;
        else {
            System.out.println("Your response must be "+length+" characters long.");
            return getResponse(length);
        }
    }

    //Main menu
    public static void showMenu() {
        System.out.println("Welcome to Javadle! Please select an option.");
        System.out.println("1 - Pick a word and give it to someone else. (if you send in a word longer than 5 chars, we will cut it)");
        System.out.println("2 - Use a pre-generated word.");
        System.out.println("3 - Tutorial!");
        int choice = Integer.parseInt(userIn.next());
        //Send user to game
        switch(choice) {
            case 1:
                System.out.println("Choose your word.");
                playGame(getResponse(5));
            case 2:
                playGame(getFromWordlist());
            case 3:
                playTutorial();
            default:
                showMenu();
        }
    }

    //Basic tutorial walkthrough
    private static void playTutorial() {
        System.out.println("Welcome to Javadle!");
        System.out.println("This is a copy of wordle, just made in Java and played in a terminal.");
        System.out.println("For those who haven't played wordle, the aim of the game is to guess what 5 letter word I am thinking of.");
        System.out.println("I will tell you if you got the character right in the right space, if the character is in the word but not in the right space, or is not in the word at all.");
        System.out.println("These are marked as 'c' (correct), 'p' (partial), or 'n' (not correct).");
        System.out.println("Enter 0 to return to the main menu.");
        while(true){
            if(userIn.nextInt() == 0) {
                Lobby.clearTerminal();
                showMenu();
            }
        }
    }

    //Main game method/loop
    private static void playGame(char[] word) {
        Lobby.clearTerminal();
        for(int playCount = 0; playCount < 5; playCount++) {
            char[] correctness = {'n', 'n', 'n', 'n', 'n'};
            System.out.println("Make a guess.");
            char[] response = getResponse(5);
            for (int i = 0; i < 5; i++) {
                if (response[i] == word[i])
                    correctness[i] = 'c';
                else for(int k = 0; k < 5; k++)
                    if(response[i] == word[k])
                        correctness[i] = 'p';
            }
            if(Arrays.equals(correctness, new char[]{'c', 'c', 'c', 'c', 'c'})){
                System.out.println("Congrats! You won. Your score: " + (playCount));
                System.exit(0);
            }else System.out.println(Arrays.toString(correctness));
        }
        System.out.println("You were wrong! The word was "+Arrays.toString(word)+" \nBetter luck next time pal.");
        System.exit(0);
    }
}