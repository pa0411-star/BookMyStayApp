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

    public String getGuestName() {
        return guestName;
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

// Booking History (stores confirmed bookings)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("📌 Added to history: " + reservation);
    }

    // Retrieve all reservations
    public List<Reservation> getAllReservations() {
        return history;
    }

    // Display history
    public void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("No bookings in history.");
            return;
        }

        System.out.println("\nBooking History (Chronological Order):");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }
}

// Report Service (read-only operations)
class BookingReportService {

    // Total bookings
    public void totalBookings(List<Reservation> reservations) {
        System.out.println("\n📊 Total Bookings: " + reservations.size());
    }

    // Bookings by room type
    public void bookingsByRoomType(List<Reservation> reservations) {
        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : reservations) {
            countMap.put(r.getRoomType(),
                    countMap.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\n📊 Bookings by Room Type:");
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    // List all bookings (read-only)
    public void listAllBookings(List<Reservation> reservations) {
        System.out.println("\n📊 Detailed Booking Report:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Create booking history
        BookingHistory bookingHistory = new BookingHistory();

        // Step 2: Simulate confirmed bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe");
        Reservation r2 = new Reservation("RES102", "Bob", "Suite");
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard");
        Reservation r4 = new Reservation("RES104", "David", "Deluxe");

        // Step 3: Add to history (after confirmation)
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);
        bookingHistory.addReservation(r4);

        // Step 4: Display history
        bookingHistory.displayHistory();

        // Step 5: Generate reports
        BookingReportService reportService = new BookingReportService();

        List<Reservation> allBookings = bookingHistory.getAllReservations();

        reportService.totalBookings(allBookings);
        reportService.bookingsByRoomType(allBookings);
        reportService.listAllBookings(allBookings);
    }
}