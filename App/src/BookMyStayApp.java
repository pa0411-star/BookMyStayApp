import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName +
                ", RoomType=" + roomType +
                ", Nights=" + nights + "]";
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory;

    public InventoryService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = inventory.get(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Validator Class (Fail-Fast)
class BookingValidator {

    public static void validate(Reservation reservation, InventoryService inventory)
            throws InvalidBookingException {

        if (reservation.getGuestName() == null || reservation.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (reservation.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0.");
        }

        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (!inventory.isAvailable(reservation.getRoomType())) {
            throw new InvalidBookingException("Room not available: " + reservation.getRoomType());
        }
    }
}

// Booking Service
class BookingService {

    private int roomCounter = 1;
    private Set<String> allocatedRooms = new HashSet<>();

    public void processBooking(Reservation reservation, InventoryService inventory) {
        try {
            // Step 1: Validate (Fail Fast)
            BookingValidator.validate(reservation, inventory);

            // Step 2: Allocate room
            String roomId = generateRoomId(reservation.getRoomType());

            allocatedRooms.add(roomId);

            // Step 3: Update inventory (safe)
            inventory.decrement(reservation.getRoomType());

            // Step 4: Confirm booking
            System.out.println("✅ Booking Confirmed: " + reservation +
                    " | Room ID: " + roomId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("❌ Booking Failed: " + e.getMessage());
        }
    }

    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + (roomCounter++);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService();

        inventory.displayInventory();

        // Test cases (valid + invalid)
        List<Reservation> requests = Arrays.asList(
                new Reservation("Alice", "Deluxe", 2),   // valid
                new Reservation("", "Suite", 1),          // invalid name
                new Reservation("Bob", "Luxury", 2),      // invalid room type
                new Reservation("Charlie", "Suite", 0),   // invalid nights
                new Reservation("David", "Suite", 1),     // valid
                new Reservation("Eve", "Suite", 1)        // unavailable now
        );

        System.out.println("\nProcessing Bookings...\n");

        for (Reservation r : requests) {
            bookingService.processBooking(r, inventory);
        }

        inventory.displayInventory();
    }
}