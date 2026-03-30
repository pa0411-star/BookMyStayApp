import java.util.*;

// Domain Model: Room
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

// Inventory (State Holder - Read Only for this Use Case)
class Inventory {
    private Map<String, Integer> availabilityMap;

    public Inventory() {
        availabilityMap = new HashMap<>();
    }

    public void addRoom(String roomType, int count) {
        availabilityMap.put(roomType, count);
    }

    // Read-only access
    public int getAvailableCount(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availabilityMap.keySet();
    }
}

// Search Service (Read-only operations)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        System.out.println("Available Rooms:\n");

        for (String roomType : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailableCount(roomType);

            // Validation: Only show available rooms
            if (availableCount > 0) {
                Room room = roomCatalog.get(roomType);

                if (room != null) {
                    displayRoomDetails(room, availableCount);
                }
            }
        }
    }

    private void displayRoomDetails(Room room, int count) {
        System.out.println("Room Type: " + room.getType());
        System.out.println("Price: ₹" + room.getPrice());
        System.out.println("Amenities: " + String.join(", ", room.getAmenities()));
        System.out.println("Available Count: " + count);
        System.out.println("---------------------------");
    }
}

// Main Class
public class BookMyStayApp  {
    public static void main(String[] args) {

        // Step 1: Setup Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // Should not appear
        inventory.addRoom("Suite", 2);

        // Step 2: Setup Room Catalog (Domain Model)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single",
                new Room("Single", 2000,
                        Arrays.asList("WiFi", "TV", "AC")));

        roomCatalog.put("Double",
                new Room("Double", 3500,
                        Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        roomCatalog.put("Suite",
                new Room("Suite", 6000,
                        Arrays.asList("WiFi", "TV", "AC", "Mini Bar", "Jacuzzi")));

        // Step 3: Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Perform Search (Read-Only Operation)
        searchService.searchAvailableRooms();
    }
}