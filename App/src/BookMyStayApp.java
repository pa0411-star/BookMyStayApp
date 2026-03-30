import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return guestName + " -> " + roomType;
    }
}

// Thread-safe Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Critical Section (synchronized)
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Shared Booking Queue (Thread-safe access)
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}

// Booking Processor (Runnable for threads)
class BookingProcessor implements Runnable {

    private BookingQueue queue;
    private InventoryService inventory;

    public BookingProcessor(BookingQueue queue, InventoryService inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            Reservation reservation;

            // Critical section: fetch request
            synchronized (queue) {
                reservation = queue.getNextRequest();
            }

            if (reservation == null) {
                break; // no more requests
            }

            processReservation(reservation);
        }
    }

    private void processReservation(Reservation r) {

        // Critical section: allocate room safely
        boolean success = inventory.allocateRoom(r.getRoomType());

        if (success) {
            System.out.println(Thread.currentThread().getName() +
                    " ✅ Booked for " + r);
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " ❌ Failed (No Availability) for " + r);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        InventoryService inventory = new InventoryService();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Deluxe"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Suite"));
        queue.addRequest(new Reservation("David", "Standard"));
        queue.addRequest(new Reservation("Eve", "Standard"));
        queue.addRequest(new Reservation("Frank", "Standard"));

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new Thread(new BookingProcessor(queue, inventory), "Thread-1");
        Thread t2 = new Thread(new BookingProcessor(queue, inventory), "Thread-2");
        Thread t3 = new Thread(new BookingProcessor(queue, inventory), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();
    }
}