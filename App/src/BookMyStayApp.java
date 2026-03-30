import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Wrapper class to persist system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory,
                       List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Inventory Service
class InventoryService {
    Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public void decrement(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> data) {
        inventory = data;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

// Booking History
class BookingHistory {
    List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getHistory() {
        return history;
    }

    public void setHistory(List<Reservation> list) {
        history = list;
    }

    public void display() {
        System.out.println("\nBooking History:");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state
    public void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("\n💾 System state saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("\n♻️ System state restored successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("\n⚠️ No saved data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("\n❌ Corrupted data. Starting with safe defaults.");
        }
        return null;
    }
}

// Main Class
public class BookMyStayApp git {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();
        PersistenceService persistence = new PersistenceService();

        // Step 1: Try to restore state
        SystemState savedState = persistence.load();

        if (savedState != null) {
            inventory.setInventory(savedState.inventory);
            history.setHistory(savedState.bookingHistory);
        } else {
            // Fresh bookings if no saved data
            Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
            Reservation r2 = new Reservation("RES102", "Bob", "Suite");

            history.add(r1);
            history.add(r2);

            inventory.decrement("Deluxe");
            inventory.decrement("Suite");
        }

        // Display current state
        inventory.display();
        history.display();

        // Step 2: Save state before shutdown
        SystemState currentState =
                new SystemState(inventory.getInventory(), history.getHistory());

        persistence.save(currentState);

        System.out.println("\n✅ System ready for restart with persisted state.");
    }
}