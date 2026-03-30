import java.util.*;

// Reservation class (same as previous use case)
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
    private Map<String, Integer> roomInventory;

    public InventoryService() {
        roomInventory = new HashMap<>();

        // Initial inventory
        roomInventory.put("Standard", 2);
        roomInventory.put("Deluxe", 2);
        roomInventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementInventory(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Booking Service (handles allocation)
class BookingService {

    private Set<String> allocatedRoomIds; // ensures uniqueness
    private Map<String, Set<String>> roomTypeToIds; // mapping
    private int roomCounter = 1;

    public BookingService() {
        allocatedRoomIds = new HashSet<>();
        roomTypeToIds = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + roomCounter++;
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    // Allocate room
    public void allocateRoom(Reservation reservation, InventoryService inventoryService) {

        String roomType = reservation.getRoomType();

        // Check availability
        if (!inventoryService.isAvailable(roomType)) {
            System.out.println("❌ No rooms available for " + reservation);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Atomic allocation
        allocatedRoomIds.add(roomId);

        roomTypeToIds.putIfAbsent(roomType, new HashSet<>());
        roomTypeToIds.get(roomType).add(roomId);

        // Update inventory immediately
        inventoryService.decrementInventory(roomType);

        // Confirm booking
        System.out.println("✅ Booking Confirmed: " + reservation +
                " | Room ID: " + roomId);
    }

    public void displayAllocations() {
        System.out.println("\nAllocated Rooms:");
        for (Map.Entry<String, Set<String>> entry : roomTypeToIds.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Create queue (from Use Case 5)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.offer(new Reservation("Alice", "Deluxe", 2));
        bookingQueue.offer(new Reservation("Bob", "Suite", 3));
        bookingQueue.offer(new Reservation("Charlie", "Standard", 1));
        bookingQueue.offer(new Reservation("David", "Suite", 2)); // should fail (only 1 suite)

        // Step 2: Initialize services
        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService();

        inventoryService.displayInventory();

        // Step 3: Process queue (FIFO)
        System.out.println("\nProcessing Booking Requests...\n");

        while (!bookingQueue.isEmpty()) {
            Reservation reservation = bookingQueue.poll();
            bookingService.allocateRoom(reservation, inventoryService);
        }

        // Step 4: Final state
        bookingService.displayAllocations();
        inventoryService.displayInventory();
    }
}