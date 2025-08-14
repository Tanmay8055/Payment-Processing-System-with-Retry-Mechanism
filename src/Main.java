package src;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        PaymentTransferService transferService = new PaymentTransferService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Payment Transfer System ===");
            System.out.println("1. Register New User");
            System.out.println("2. Add Balance to User");
            System.out.println("3. View All Users");
            System.out.println("4. Make a Transfer");
            System.out.println("5. Exit");
            System.out.print("Choose an option (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerNewUser(scanner, transferService);
                    break;

                case 2:
                    addBalance(scanner, transferService);
                    break;

                case 3:
                    transferService.displayUsers();
                    break;

                case 4:
                    if (!transferService.hasUsers()) {
                        System.out.println("\nNo users registered yet. Please add users first.");
                    } else {
                        processTransfer(scanner, transferService);
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using the Payment Transfer System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void registerNewUser(Scanner scanner, PaymentTransferService transferService) {
        System.out.println("\n=== Register New User ===");
        
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        transferService.addUser(name);
    }

    private static void addBalance(Scanner scanner, PaymentTransferService transferService) {
        transferService.displayUsers();
        
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to add: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter currency (INR/USD/EUR/GBP): ");
        String currency = scanner.nextLine().toUpperCase();

        if (!currency.matches("INR|USD|EUR|GBP")) {
            System.out.println("Invalid currency! Please use INR, USD, EUR, or GBP.");
            return;
        }

        transferService.addBalanceToUser(userId, amount, currency);
    }

    private static void processTransfer(Scanner scanner, PaymentTransferService transferService) {
        transferService.displayUsers();

        System.out.print("Enter sender's User ID: ");
        String fromUserId = scanner.nextLine().toUpperCase();

        System.out.print("Enter receiver's User ID: ");
        String toUserId = scanner.nextLine().toUpperCase();

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter currency (INR/USD/EUR/GBP): ");
        String currency = scanner.nextLine().toUpperCase();

        transferService.transferMoney(fromUserId, toUserId, amount, currency);
    }
}