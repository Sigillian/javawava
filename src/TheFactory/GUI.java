package TheFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI {
    public static ArrayList<String> messageList = new ArrayList<>(); // Public list for storing messages
    private static JTextArea displayArea; // Text area for displaying messages
    private static JTextField inputField; // Input field for user commands
    private static JTextArea commandHistory; // Area for command history

    public static int fontSize = 28;

    public static void changeFontSize(int size) {
        fontSize = size;
        displayArea.setFont(loadCustomFont("src/TheFactory/Josefin.ttf", fontSize));
    }
    // Constructor for GUI
    public GUI() {
        // Create the main frame
        JFrame frame = new JFrame("THE FACTORY");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set dark theme colors
        Color backgroundColor = Color.BLACK;
        Color textColor = Color.GREEN;

        // Load custom font
        Font customFont = loadCustomFont("src/TheFactory/Josefin.ttf", 28f);

        // Main panel with border layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        // Display area for messages
        displayArea = new JTextArea();
        displayArea.setBackground(backgroundColor);
        displayArea.setForeground(textColor);
        displayArea.setEditable(false);
        displayArea.setFont(customFont);

        // Scroll pane for the display area
        JScrollPane displayScroll = new JScrollPane(displayArea);
        displayScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Command history area
        commandHistory = new JTextArea(5, 30);
        commandHistory.setBackground(backgroundColor);
        commandHistory.setForeground(textColor);
        commandHistory.setEditable(false);
        commandHistory.setFont(customFont);

        JScrollPane historyScroll = new JScrollPane(commandHistory);
        historyScroll.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Input field for commands
        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(textColor);
        inputField.setCaretColor(textColor);
        inputField.setFont(customFont);

        // Add action to the input field to handle "Enter" key submission
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitCommand();
            }
        });

        // Button to add input to the display
        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.DARK_GRAY);
        sendButton.setForeground(textColor);
        sendButton.setFont(customFont);

        // Action for the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitCommand();
            }
        });

        // Bottom panel for input and button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Add components to the main panel
        panel.add(displayScroll, BorderLayout.CENTER);
        panel.add(historyScroll, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.NORTH);

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Add initial messages after GUI is set up
        addToCommandOutput("Welcome to The Factory!");
        addToCommandOutput("Type help for a list of commands.");
    }

    // Method to handle command submission
    private void submitCommand() {
        String input = inputField.getText();
        if (!input.isEmpty()) {
            commandHistory.append(input + "\n");
            inputField.setText("");

            CommandHandler.handleCommand(input);

            updateDisplay();
        }
    }

    // Method to update the display area
    public static void updateDisplay() {
        if (displayArea != null) {
            displayArea.setText(String.join("\n", messageList));
        }
    }

    // Method to add output to the display area
    public static void addToCommandOutput(String message) {
        messageList.add(message);
        updateDisplay();
    }
    public static void clearTerminal() {
        messageList.clear();
        updateDisplay();
    }

    // Method to load a custom font
    private static Font loadCustomFont(String path, float size) {
        try {
            File fontFile = new File(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            // Apply the bold style when deriving the font
            return font.deriveFont(1, size);
        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font: " + e.getMessage());
            return new Font("Monospaced", Font.BOLD, 18); // Fallback font
        }
    }

}
