import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    public static void readFromFile(String fileName, ArrayList<Room> rooms) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            @SuppressWarnings("unchecked")
            ArrayList<Room> readRooms = (ArrayList<Room>) ois.readObject();
            rooms.addAll(readRooms);
            System.out.println("Data read from the file successfully");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            System.out.println("Creating a new file");
            writeToFile(fileName, new ArrayList<>());
        }
    }

    public static void writeToFile(String fileName, ArrayList<Room> rooms) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(rooms);
            System.out.println("Data written to the file successfully");
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public static void writeToFileDir(String directory, String fileName, ArrayList<Room> rooms) {
        File file = new File(directory, fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(rooms);
            System.out.println("Data written to the file successfully");
        } catch (IOException e) {
            System.out.println("Error writing to the file: " + e.getMessage());
        }
    }

    public static void closeFile(String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName, true)) {
            System.out.println("File closed successfully");
        } catch (IOException e) {
            System.out.println("Error closing the file: " + e.getMessage());
        }
    }
}