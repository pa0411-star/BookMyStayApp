import java.util.*;

// Add-On Service class
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Reservation class (simplified with ID)
class Reservation {
    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    @Override
    public String toString() {
        return "Reservation [ID=" + reservationId + ", Guest=" + guestName + "]";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service +
                " to Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        double total = 0.0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }

        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services for Reservation ID: " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation ID: " + reservationId);
        for (AddOnService s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Create sample reservations (already confirmed in UC6)
        Reservation r1 = new Reservation("RES101", "Alice");
        Reservation r2 = new Reservation("RES102", "Bob");

        // Step 2: Create services
        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService wifi = new AddOnService("WiFi", 200);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1000);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Step 3: Initialize manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Step 4: Add services to reservations
        manager.addService(r1.getReservationId(), breakfast);
        manager.addService(r1.getReservationId(), wifi);
        manager.addService(r1.getReservationId(), spa);

        manager.addService(r2.getReservationId(), wifi);
        manager.addService(r2.getReservationId(), airportPickup);

        // Step 5: Display services
        manager.displayServices(r1.getReservationId());
        manager.displayServices(r2.getReservationId());
    }
}