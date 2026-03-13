/**
 * UseCase1HotelBookingApp
 *
 * This class demonstrates the entry point for the Hotel Booking application.
 * It prints a welcome message, application name, and version to the console.
 *
 * Key Concepts:
 * - main() method as the JVM entry point
 * - Console output using System.out.println()
 * - JavaDoc documentation
 *
 * @author YourName
 * @version 1.0
 */
public class BookMyStayApp  {

    /**
     * The main method is the entry point of the application.
     * JVM starts execution from this method.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Print welcome message
        System.out.println("**************************************");
        System.out.println(" Welcome to the Hotel Booking System ");
        System.out.println(" Version: 1.0 ");
        System.out.println("**************************************");

        // Optional additional message
        System.out.println("We hope you enjoy your stay!");
    }
}