import java.util.*;

// Reservation class
class Reservation {
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
        return "Reservation [ID=" + reservationId +
                ", Guest=" + guestName +
                ", RoomType=" + roomType + "]";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking History (with status)
class BookingHistory {
    private Map<String, String> history = new LinkedHashMap<>();

    public void addBooking(String reservationId) {
        history.put(reservationId, "CONFIRMED");
    }

    public void markCancelled(String reservationId) {
        history.put(reservationId, "CANCELLED");
    }

    public void displayHistory() {
        System.out.println("\nBooking History:");
        for (Map.Entry<String, String> entry : history.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service
class BookingService {

    private int counter = 1;
    private Map<String, String> reservationToRoom = new HashMap<>();
    private Map<String, String> roomToType = new HashMap<>();

    public void confirmBooking(Reservation reservation,
                               InventoryService inventory,
                               BookingHistory history) {

        if (!inventory.isAvailable(reservation.getRoomType())) {
            System.out.println("❌ Booking Failed: " + reservation);
            return;
        }

        String roomId = reservation.getRoomType().substring(0, 2).toUpperCase() + counter++;

        reservationToRoom.put(reservation.getReservationId(), roomId);
        roomToType.put(roomId, reservation.getRoomType());

        inventory.decrement(reservation.getRoomType());
        history.addBooking(reservation.getReservationId());

        System.out.println("✅ Confirmed: " + reservation + " | Room ID: " + roomId);
    }

    public boolean isValidReservation(String reservationId) {
        return reservationToRoom.containsKey(reservationId);
    }

    public String getRoomId(String reservationId) {
        return reservationToRoom.get(reservationId);
    }

    public String getRoomType(String roomId) {
        return roomToType.get(roomId);
    }

    public void removeReservation(String reservationId) {
        String roomId = reservationToRoom.remove(reservationId);
        if (roomId != null) {
            roomToType.remove(roomId);
        }
    }
}

// Cancellation Service (with Stack rollback)
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingService bookingService,
                              InventoryService inventory,
                              BookingHistory history) {

        System.out.println("\nCancelling: " + reservationId);

        // Step 1: Validate
        if (!bookingService.isValidReservation(reservationId)) {
            System.out.println("❌ Invalid or already cancelled booking.");
            return;
        }

        // Step 2: Fetch details
        String roomId = bookingService.getRoomId(reservationId);
        String roomType = bookingService.getRoomType(roomId);

        // Step 3: Push to stack (rollback tracking)
        rollbackStack.push(roomId);

        // Step 4: Restore inventory
        inventory.increment(roomType);

        // Step 5: Remove booking
        bookingService.removeReservation(reservationId);

        // Step 6: Update history
        history.markCancelled(reservationId);

        System.out.println("↩️ Cancelled: " + reservationId +
                " | Released Room: " + roomId);
    }

    public void displayRollbackStack() {
        System.out.println("\nRollback Stack: " + rollbackStack);
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService();
        CancellationService cancellationService = new CancellationService();

        // Create reservations
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite");
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard");

        inventory.displayInventory();

        // Confirm bookings
        bookingService.confirmBooking(r1, inventory, history);
        bookingService.confirmBooking(r2, inventory, history);
        bookingService.confirmBooking(r3, inventory, history);

        inventory.displayInventory();
        history.displayHistory();

        // Cancel bookings
        cancellationService.cancelBooking("RES102", bookingService, inventory, history);
        cancellationService.cancelBooking("RES999", bookingService, inventory, history); // invalid
        cancellationService.cancelBooking("RES102", bookingService, inventory, history); // already cancelled

        inventory.displayInventory();
        history.displayHistory();
        cancellationService.displayRollbackStack();
    }
}