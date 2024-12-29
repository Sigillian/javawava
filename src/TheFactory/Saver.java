package TheFactory;

import java.io.FileOutputStream;

public class Saver {

    private static String encode(String toEncode) {
        StringBuilder binaryStringBuilder = new StringBuilder();
        boolean flipThird = true; // Alternate flipping every third and fifth bit

        // Convert each character to ASCII and then to binary
        for (char character : toEncode.toCharArray()) {
            int asciiValue = (int) character; // Get ASCII value of the character
            String binaryString = Integer.toBinaryString(asciiValue); // Convert ASCII value to binary

            // Ensure each binary string is 8 bits long (padding with leading zeros if necessary)
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }

            char[] binaryArray = binaryString.toCharArray();
            for (int i = 0; i < binaryArray.length; i++) {
                // Flip every third or fifth bit based on the toggle
                if (flipThird && (i + 1) % 3 == 0) {
                    binaryArray[i] = binaryArray[i] == '0' ? '1' : '0';
                } else if (!flipThird && (i + 1) % 5 == 0) {
                    binaryArray[i] = binaryArray[i] == '0' ? '1' : '0';
                }
            }
            flipThird = !flipThird; // Alternate between third and fifth bit flipping

            binaryStringBuilder.append(new String(binaryArray)); // Append the modified binary string
        }

        return binaryStringBuilder.toString(); // Return the complete binary representation
    }

    private static String decode(String binaryString) {
        StringBuilder decodedStringBuilder = new StringBuilder();
        boolean flipThird = true; // Alternate flipping every third and fifth bit

        // Process the binary string in chunks of 8 bits
        for (int i = 0; i < binaryString.length(); i += 8) {
            String byteString = binaryString.substring(i, i + 8); // Get 8-bit chunk

            char[] binaryArray = byteString.toCharArray();
            for (int j = 0; j < binaryArray.length; j++) {
                // Reverse flip every third or fifth bit based on the toggle
                if (flipThird && (j + 1) % 3 == 0) {
                    binaryArray[j] = binaryArray[j] == '0' ? '1' : '0';
                } else if (!flipThird && (j + 1) % 4 == 0) {
                    binaryArray[j] = binaryArray[j] == '0' ? '1' : '0';
                }
            }
            flipThird = !flipThird; // Alternate between third and fifth bit flipping

            String originalBinary = new String(binaryArray);
            int asciiValue = Integer.parseInt(originalBinary, 2); // Convert binary to ASCII value
            char character = (char) asciiValue; // Convert ASCII value to character
            decodedStringBuilder.append(character); // Append character to result
        }

        return decodedStringBuilder.toString(); // Return the decoded string
    }

    public static void saveGame() {
        StringBuilder saveData = new StringBuilder();
        for(Product p : Headquarters.inventory)
            saveData.append(p.toString()).append("\n");
        for(Employee e : Headquarters.employeeList)
            saveData.append(e.toString()).append("\n");
        for(Factory f : Headquarters.factoryList)
            saveData.append(f.toString()).append("\n");
        for(Farm f : Headquarters.farmList)
            saveData.append(f.toString()).append("\n");
        for(Mine m : Headquarters.mineList)
            saveData.append(m.toString()).append("\n");
        for(Housing h : Headquarters.housingList)
            saveData.append(h.toString()).append("\n");
        saveData.append(Headquarters.rawMaterialStorage.getAsString()).append("\n");
        saveData.append(Headquarters.foodSupply).append("\n");
        saveData.append(Headquarters.wallet).append("\n");
        try {
            String encodedData = encode(saveData.toString());
            System.out.println(encodedData);
            System.out.println(decode(encodedData));
            FileOutputStream fileOutputStream = new FileOutputStream("save.bin");
            fileOutputStream.write(encodedData.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Headquarters.initializeGame();
        saveGame();
    }
}