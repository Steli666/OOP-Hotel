import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Operations
{
    private String filename;
    private boolean fileOpened;
    private ArrayList<Room> rooms;
    private BufferedReader reader;

    public Operations() {
        filename = null;
        fileOpened = false;
        rooms = new ArrayList<>();
//        Room room1 = new Room(102, "No room service", 0, 1);
//        Room room2 = new Room(231, "Ocean view", 0, 2);
//        Room room3 = new Room(311, "Pet-friendly", 0, 3);
//        Room room4 = new Room(420, "Executive suite", 0, 4);
//        Room room5 = new Room(501, "Accessible for disabled guests", 0, 5);
//        rooms.add(room1);
//        rooms.add(room2);
//        rooms.add(room3);
//        rooms.add(room4);
//        rooms.add(room5);
//        Hotel.checkin(102,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",1,rooms);
//        Hotel.checkin(231,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",2,rooms);
//        Hotel.checkin(311,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",2,rooms);
//        Hotel.checkin(420,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",3,rooms);
//        Hotel.checkin(501,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",3,rooms);

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public boolean isFileOpened(boolean fileOpened) {
        if(fileOpened) {return true;}
        else {return false;}
    }

    public void start() {
        try {
            System.out.println("Opened a new command prompt.");
            do {
                System.out.print(">> ");
                String userInput = reader.readLine().trim();
                String[] words = userInput.split("\\s+");
                switch (words[0]) {
                    case "open":
                        openFile(words);
                        break;
                    case "save":
                        saveFile();
                        break;
                    case "saveas":
                        saveFileAs(words);
                        break;
                    case "close":
                        closeFile();
                        break;
                    case "help":
                        help();
                        break;
                    case "exit":
                        exit();
                        break;
                    case "checkin":
                        if(isFileOpened(fileOpened)) {
                            checkin(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "availability":
                        if(isFileOpened(fileOpened)) {
                            availability(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "checkout":
                        if(isFileOpened(fileOpened)) {
                            checkout(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "report":
                        if(isFileOpened(fileOpened)) {
                            report(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "find":
                        if(isFileOpened(fileOpened)) {
                            find(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "find!":
                        if(isFileOpened(fileOpened)) {
                            findVip(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    case "unavailable":
                        if(isFileOpened(fileOpened)) {
                            unavailable(words);
                        } else {
                            System.out.println("You have not opened a file.");
                        }
                        break;
                    default:
                        System.out.println("Unknown command.");
                        break;
                }
            } while (true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openFile(String[] words) {
        if (!fileOpened && words.length == 2) {
            filename = words[1];
            FileHandler.readFromFile(filename, rooms);
            fileOpened = true;

        } else if (fileOpened) {
            System.out.println("A file is already opened.");
        } else {
            System.out.println("Type help if you need to see the commands.");
        }
    }

    private void saveFile() {
        if (fileOpened) {
            FileHandler.writeToFile(filename, rooms);
        } else {
            System.out.println("No file is currently opened.");
        }
    }

    private void saveFileAs(String[] words) {
        if (fileOpened) {
            if (words.length > 1) {
                String dir = words[1];
                FileHandler.writeToFile_dir(dir, filename, rooms);
                System.out.println("Successfully saved " + filename + " in " + dir);
            } else {
                System.out.println("Specify the directory where you want to save the file.");
            }
        } else {
            System.out.println("No file is currently opened.");
        }
    }

    private void closeFile() {
        if (fileOpened) {
            FileHandler.closeFile(filename);
            fileOpened = false;
            filename = null;
            rooms.clear();
        } else {
            System.out.println("No file is currently opened.");
        }
    }

    private void help() {
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
    }

    private void exit() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }

    private void checkin(String[] words) {
        if (words.length >= 5) {
            try {
                int roomNumber = Integer.parseInt(words[1]);
                LocalDate from = LocalDate.parse(words[2]);
                LocalDate to = LocalDate.parse(words[3]);
                String note = words[4];
                int guests = 0;
                if (from.isAfter(to)) {
                    System.out.println("Error: Check-in date cannot be after check-out date.");
                    return;
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
    }

    private void availability(String[] words) {
        if (words.length >= 1) {
            try {
                LocalDate date;
                if (words.length == 1 || words[1].equals("")) {
                    date = LocalDate.now();
                    Hotel.availability(rooms, date);
                } else if (words.length == 2){
                    date = LocalDate.parse(words[1]);
                    Hotel.availability(rooms, date);
                }
                else {
                    System.out.println("You have input too many parameters");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void checkout(String[] words) {
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
    }

    private void report(String[] words) {
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
    }

    private void find(String[] words) {
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
    }

    private void findVip(String[] words) {
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
    }

    private void unavailable(String[] words) {
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
    }

    public static void main(String[] args) {
        Operations operations = new Operations();
        operations.start();
    }
}