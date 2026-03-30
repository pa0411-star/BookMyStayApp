import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking request
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

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void submitRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View next request without removing
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Process next request (dequeue)
    public Reservation processNextRequest() {
        return requestQueue.poll();
    }

    // Display all requests
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No pending booking requests.");
            return;
        }

        System.out.println("\nCurrent Booking Request Queue:");
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulating guest booking requests
        Reservation r1 = new Reservation("Alice", "Deluxe", 2);
        Reservation r2 = new Reservation("Bob", "Suite", 3);
        Reservation r3 = new Reservation("Charlie", "Standard", 1);

        // Step 1: Submit requests
        queue.submitRequest(r1);
        queue.submitRequest(r2);
        queue.submitRequest(r3);

        // Step 2: Display queue (FIFO order)
        queue.displayQueue();

        // Step 3: Peek next request
        System.out.println("\nNext request to process: " + queue.peekNextRequest());

        // Step 4: Process requests (FIFO)
        System.out.println("\nProcessing requests...");
        while (queue.peekNextRequest() != null) {
            Reservation processed = queue.processNextRequest();
            System.out.println("Processed: " + processed);
        }

        // Step 5: Final state
        queue.displayQueue();
    }
}