import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        ArrayList<ArrayList<Dates>> roomListOfLists = new ArrayList<>();
//        Room room1 = new Room(231, "No room service", 0, 5);
//        Room room2 = new Room(102, "Ocean view", 0, 1);
//        Room room3 = new Room(311, "Pet-friendly", 0, 3);
//        Room room4 = new Room(420, "Executive suite", 0, 2);
//        Room room5 = new Room(501, "Accessible for disabled guests", 0, 4);
//        rooms.add(room1);
//        rooms.add(room2);
//        rooms.add(room3);
//        rooms.add(room4);
//        rooms.add(room5);
        Operations operations = new Operations();
        operations.start();

        /*try {
            String filename = null;
            System.out.println("Opened a new command prompt.");
            boolean fileOpened = false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;

            do {
                System.out.print(">> ");
                userInput = reader.readLine();
                userInput = userInput.trim();
                String[] words = userInput.split("\\s+");
                switch (words[0]) {
                    case "open":
                        if (!fileOpened && words.length == 2) {
                            filename = words[1];
                            FileHandler.readFromFile(filename, rooms);
                            fileOpened = true;
                        } else if (fileOpened) {
                            System.out.println("A file is already opened.");
                        } else {
                            System.out.println("Type help if you need to see the commands.");
                        }
                        break;
                    case "save":
                        if (fileOpened) {
                            FileHandler.writeToFile(filename, rooms);
                        } else {
                            System.out.println("No file is currently opened.");
                        }
                        break;
                    case "saveas":
                        if (fileOpened) {
                            if (words.length > 1) {
                                String dir = words[1];
                                FileHandler.writeToFile_dir(dir, filename, rooms);
                                //System.out.println("Successfully saved " + filename + " in " + dir);
                            } else {
                                System.out.println("Specify the directory where you want to save the file.");
                            }
                        } else {
                            System.out.println("No file is currently opened.");
                        }
                        break;
                    case "close":
                        if (fileOpened) {
                            FileHandler.closeFile(filename);
                            fileOpened = false;
                            filename = null;
                            rooms.clear();
                        } else {
                            System.out.println("No file is currently opened.");
                        }
                        break;
                    case "help":
                        System.out.println("open <file>: Opens <file>");
                        System.out.println("close: Closes currently opened file");
                        System.out.println("save: Saves the currently open file");
                        System.out.println("saveas <file>: Saves the currently open file in <file>");
                        System.out.println("help: Prints the information");
                        System.out.println("exit: Exits the program");
                        System.out.println("checkin <room> <from> <to> <note> [<guests>]: You checkin a room with the info specified, guest is not mandatory");
                        System.out.println("availability [<date>]: You check available rooms on the given date or if there isn't a date specified - the current one");
                        System.out.println("checkout <room>: You checkout a room with number");
                        System.out.println("report <from> <to>: You check when and for how long were rooms used");
                        System.out.println("find <beds> <from> <to>: You find available rooms with the given number of beds for the given dates");
                        System.out.println("find! <beds> <from> <to>: You find available rooms for VIP guests with the given number of beds for the given dates");
                        System.out.println("unavailable <room> <from> <to> <note>: You make a room unavailable for a fixed period of time");
                        break;

                    case "exit":
                        System.out.println("Exiting the program...");
                        break;
                    case "checkin":
                        if (words.length >= 5) {
                            try {
                                int roomNumber = Integer.parseInt(words[1]);
                                LocalDate from = LocalDate.parse(words[2]);
                                LocalDate to = LocalDate.parse(words[3]);
                                String note = words[4];
                                int guests = 0;
                                if (from.isAfter(to)) {
                                    System.out.println("Error: Check-in date cannot be after check-out date.");
                                }
                                if (words.length > 5) {
                                    guests = Integer.parseInt(words[5]);
                                }
                                Hotel.checkin(roomNumber, from, to, note, guests, rooms);
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Invalid number format.");
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Error: Insufficient input parameters.");
                        }
                        break;
                    case "availability":
                        if (words.length >= 1) {
                            try {
                                LocalDate date;
                                if (words.length >= 2 && words[1].equals("")) {
                                    date = LocalDate.parse(words[1]);
                                } else {
                                    date = LocalDate.now();
                                }
                                Hotel.availability(rooms, date);
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;
                    case "checkout":
                        if (words.length == 2) {
                            try {
                                int room = Integer.parseInt(words[1]);
                                Hotel.checkout(room, rooms);
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }else {
                            System.out.println("Error: Incorrect number of parameters.");
                        }
                        break;
                    case "report":
                        if (words.length == 3) {
                            try {
                                LocalDate from = LocalDate.parse(words[1]);
                                LocalDate to = LocalDate.parse(words[2]);
                                if (from.isAfter(to)) {
                                    System.out.println("Error: Check-in date cannot be after check-out date.");
                                }
                                else {
                                    Hotel.report(rooms, from, to);
                                }
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Error: Incorrect number of parameters.");
                        }
                        break;
                    case "find":
                        if (words.length == 4) {
                            try {
                                int beds = Integer.parseInt(words[1]);
                                LocalDate from = LocalDate.parse(words[2]);
                                LocalDate to = LocalDate.parse(words[3]);
                                if (from.isAfter(to)) {
                                    System.out.println("Error: Check-in date cannot be after find date.");
                                } else {
                                    Hotel.find(beds, from, to, rooms);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Invalid number format for beds.");
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Error: Incorrect number of parameters.");
                        }
                        break;
                    case "find!":
                        if (words.length == 4) {
                            try {
                                int beds = Integer.parseInt(words[1]);
                                LocalDate from = LocalDate.parse(words[2]);
                                LocalDate to = LocalDate.parse(words[3]);
                                if (from.isAfter(to)) {
                                    System.out.println("Error: Check-in date cannot be after find date.");
                                } else {
                                    Hotel.findVip(beds, from, to, rooms);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Invalid number format for beds.");
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Error: Incorrect number of parameters.");
                        }
                        break;
                    case "unavailable":
                        if (words.length == 5) {
                            try {
                                int room = Integer.parseInt(words[1]);
                                LocalDate from = LocalDate.parse(words[2]);
                                LocalDate to = LocalDate.parse(words[3]);
                                String note = words[4];
                                if (from.isAfter(to)) {
                                    System.out.println("Error: Check-in date cannot be after unavailable date.");
                                }
                                Hotel.unavailable(room, from, to, note, rooms);
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Invalid number format for room.");
                            } catch (DateTimeParseException e) {
                                System.out.println("Error: Invalid date format.");
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Error: Incorrect number of parameters.");
                        }
                        break;
                    default:
                        System.out.println("Unknown command.");
                        break;
                }
            } while (!userInput.equals("exit"));
            System.out.println("Command prompt closed.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }
}