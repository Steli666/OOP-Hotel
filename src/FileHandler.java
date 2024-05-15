import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class FileHandler {

    public static void readFromFile(String fileName, ArrayList<Room> rooms) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fileInputStream);
            ArrayList<Room> readRooms = (ArrayList<Room>) ois.readObject();
            rooms.addAll(readRooms);
            System.out.println("Data read from the file successfully");
            ois.close();
        } catch (IOException | ClassNotFoundException e) {  System.out.println("Error reading from file: " + e.getMessage());
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

    public static void writeToFile_dir(String directory, String fileName, ArrayList<Room> rooms) {
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