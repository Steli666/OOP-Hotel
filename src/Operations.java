import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Operations {
    private String fileName;
    private boolean fileOpened;
    private BufferedReader reader;
    private Hotel hotel;
    private Map<String, Consumer<String[]>> commandHandlers;

    public Operations() {
        fileName = null;
        fileOpened = false;
        ArrayList<Room> rooms = new ArrayList<>();
        Room room1 = new Room(102, 1);
        Room room2 = new Room(231, 2);
        Room room3 = new Room(311, 3);
        Room room4 = new Room(420, 4);
        Room room5 = new Room(501, 5);
//        rooms.add(room1);
//        rooms.add(room2);
//        rooms.add(room3);
//        rooms.add(room4);
//        rooms.add(room5);
        hotel = new Hotel(rooms);
//        hotel.checkin(102,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",1);
//        hotel.checkin(231,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",2);
//        hotel.checkin(311,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",2);
//        hotel.checkin(420,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",2);
//        hotel.checkin(501,LocalDate.of(2024, 1, 1),LocalDate.of(2024, 1, 6),"no",3);
        reader = new BufferedReader(new InputStreamReader(System.in));
        initializeCommandHandlers();
    }

    private void initializeCommandHandlers() {
        commandHandlers = new HashMap<>();
        commandHandlers.put("open", this::openFile);
        commandHandlers.put("save", s -> saveFile());
        commandHandlers.put("saveas", this::saveFileAs);
        commandHandlers.put("close", s -> closeFile());
        commandHandlers.put("help", s -> help());
        commandHandlers.put("exit", s -> {
        });
        commandHandlers.put("checkin", this::checkin);
        commandHandlers.put("availability", this::availability);
        commandHandlers.put("checkout", this::checkout);
        commandHandlers.put("report", this::report);
        commandHandlers.put("find", this::find);
        commandHandlers.put("find!", this::findVip);
        commandHandlers.put("unavailable", this::unavailable);
    }

    public void start() {
        try {
            if (reader == null) {
                reader = new BufferedReader(new InputStreamReader(System.in));
            }
            System.out.println("Opened a new command prompt.");
            String userInput;

            do {
                System.out.print(">> ");
                userInput = reader.readLine().trim();
                String[] words = userInput.split("\\s+");
                handleCommand(words);
            } while (!userInput.equals("exit"));

            System.out.println("Command prompt closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCommand(String[] words) {
        Consumer<String[]> commandHandler = commandHandlers.getOrDefault(words[0], this::unknownCommand);
        commandHandler.accept(words);
    }

    private void unknownCommand(String[] words) {
        System.out.println("Unknown command, type help.");
    }

    private void openFile(String[] words) {
        if (!fileOpened && words.length == 2) {
            fileName = words[1];
            FileHandler.readFromFile(fileName, hotel.getRooms());
            fileOpened = true;

        } else if (isFileOpened()) {
            System.out.println("A file is already opened.");
        } else {
            System.out.println("Type help if you need to see the commands.");
        }
    }

    private void saveFile() {
        if (isFileOpened()) {
            FileHandler.writeToFile(fileName, hotel.getRooms());
        } else {
            System.out.println("No file is currently opened.");
        }
    }

    private void saveFileAs(String[] words) {
        if (isFileOpened()) {
            if (words.length > 1) {
                String dir = words[1];
                FileHandler.writeToFileDir(dir, fileName, hotel.getRooms());
                System.out.println("Successfully saved " + fileName + " in " + dir);
            } else {
                System.out.println("Specify the directory where you want to save the file.");
            }
        } else {
            System.out.println("No file is currently opened.");
        }
    }

    private void closeFile() {
        if (isFileOpened()) {
            FileHandler.closeFile(fileName);
            fileOpened = false;
            fileName = null;
            hotel.clearRooms();
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

    private void checkin(String[] words) {
        if (isFileOpened()) {
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
                    hotel.checkin(roomNumber, from, to, note, guests);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid number format.");
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (RoomNotFoundException | TooManyGuestsException | BeforeFirstReservationException | BookedRoomException | SuccessfulCheckin e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error: Insufficient input parameters.");
            }
        }
    }

    private void availability(String[] words) {
        if (isFileOpened()) {
            if (words.length >= 1) {
                try {
                    LocalDate date;
                    if (words.length == 1 || words[1].equals("")) {
                        date = LocalDate.now();
                        System.out.println("Available rooms:");
                        hotel.availability(date);
                    } else if (words.length == 2) {
                        date = LocalDate.parse(words[1]);
                        hotel.availability(date);
                    } else {
                        System.out.println("You have input too many parameters");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (NoAvailableException e){
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private void checkout(String[] words) {
        if (isFileOpened()) {
            if (words.length == 2) {
                try {
                    int room = Integer.parseInt(words[1]);
                    hotel.checkout(room);
                }
                catch (RoomNotFoundException | TooEarlyException | FalseIdException e){
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error: Incorrect number of parameters.");
            }
        }
    }

    private void report(String[] words) {
        if (isFileOpened()) {
            if (words.length == 3) {
                try {
                    LocalDate from = LocalDate.parse(words[1]);
                    LocalDate to = LocalDate.parse(words[2]);
                    if (from.isAfter(to)) {
                        System.out.println("Error: Check-in date cannot be after check-out date.");
                    } else {
                        hotel.report(from, to);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (NotMeetingRequirmentException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error: Incorrect number of parameters.");
            }
        }
    }

    private void find(String[] words) {
        if (isFileOpened()) {
            if (words.length == 4) {
                try {
                    int beds = Integer.parseInt(words[1]);
                    LocalDate from = LocalDate.parse(words[2]);
                    LocalDate to = LocalDate.parse(words[3]);
                    if (from.isAfter(to)) {
                        System.out.println("Error: Check-in date cannot be after find date.");
                    } else {
                        System.out.println("Available rooms:");
                        hotel.find(beds, from, to);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid number format for beds.");
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (NoRoomWithBedsException e){
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error: Incorrect number of parameters.");
            }
        }
    }

    private void findVip(String[] words) {
        if (isFileOpened()) {
            if (words.length == 4) {
                try {
                    int beds = Integer.parseInt(words[1]);
                    LocalDate from = LocalDate.parse(words[2]);
                    LocalDate to = LocalDate.parse(words[3]);
                    if (from.isAfter(to)) {
                        System.out.println("Error: Check-in date cannot be after find date.");
                    } else {
                        hotel.findVip(beds, from, to);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid number format for beds.");
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (AvailableRoomsException | NoOptionAvailableException e){
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("Error: Incorrect number of parameters.");
            }
        }
    }

    private void unavailable(String[] words) {
        if (isFileOpened()) {
            if (words.length == 5) {
                try {
                    int room = Integer.parseInt(words[1]);
                    LocalDate from = LocalDate.parse(words[2]);
                    LocalDate to = LocalDate.parse(words[3]);
                    String note = words[4];
                    if (from.isAfter(to)) {
                        System.out.println("Error: Check-in date cannot be after unavailable date.");
                    } else {
                        hotel.unavailable(room, from, to, note);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid number format for room.");
                } catch (DateTimeParseException e) {
                    System.out.println("Error: Invalid date format.");
                } catch (BeforeFirstReservationException | RoomNotFoundException | BookedRoomException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            } else {
                System.out.println("Error: Incorrect number of parameters.");
            }
        }
    }

    public boolean isFileOpened() {
        if(!fileOpened){System.out.println("Open a file first.");}
    return fileOpened;
    }
}