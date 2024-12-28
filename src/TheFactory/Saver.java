package TheFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Saver {
    public static void saveArrayListToFile(ArrayList<?> list, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Object item : list) {
                writer.write(item.toString()); // Call the toString() method on each object
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main (String[] args) {
        Headquarters.initializeGame();
        try {
            saveArrayListToFile(Headquarters.farmList, "factories.txt");
        } catch (IOException _) {
        }
    }
}
