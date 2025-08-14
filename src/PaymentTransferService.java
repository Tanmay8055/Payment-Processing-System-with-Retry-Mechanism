package src;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PaymentTransferService {
    private static final Logger LOGGER = Logger.getLogger(PaymentTransferService.class.getName());
    private final PaymentService paymentService;
    private final Map<String, User> users;
    private int nextUserId = 1;

    public PaymentTransferService() {
        this.paymentService = new PaymentService();
        this.users = new HashMap<>();
    }

    public String addUser(String name) {
        String userId = "USER" + nextUserId++;
        User newUser = new User(userId, name);
        users.put(userId, newUser);
        System.out.println("\nUser added successfully!");
        System.out.println(newUser);
        return userId;
    }

    public void addBalanceToUser(String userId, double amount, String currency) {
        User user = users.get(userId);
        if (user != null) {
            user.addBalance(currency, amount);
            System.out.println("\nBalance added successfully!");
            System.out.println(user);
        } else {
            System.out.println("User not found!");
        }
    }

    public void displayUsers() {
        System.out.println("\nAvailable Users:");
        System.out.println("---------------");
        for (User user : users.values()) {
            System.out.println(user);
        }
        System.out.println();
    }

    public boolean transferMoney(String fromUserId, String toUserId, double amount, String currency) {
        User sender = users.get(fromUserId);
        User receiver = users.get(toUserId);

        if (sender == null || receiver == null) {
            System.out.println("Error: Invalid user ID(s)");
            return false;
        }

        if (!sender.hasBalance(currency, amount)) {
            System.out.println("Error: Insufficient balance in " + currency);
            return false;
        }

        String transactionId = generateTransactionId(fromUserId, toUserId);
        PaymentRequest request = new PaymentRequest(transactionId, amount, currency);
        
        System.out.println("\nProcessing payment...");
        System.out.printf("Sending %.2f %s from %s to %s\n", 
            amount, currency, sender.getName(), receiver.getName());

        try {
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            
            if (result.getStatus() == PaymentStatus.SUCCESS) {
                sender.deductAmount(currency, amount);
                receiver.addBalance(currency, amount);
                
                System.out.println("\nPayment successful!");
                System.out.println("Updated balances:");
                System.out.println(sender);
                System.out.println(receiver);
                return true;
            } else {
                System.out.println("\nPayment failed!");
                return false;
            }
        } catch (PaymentException e) {
            System.out.println("\nPayment failed: " + e.getMessage());
            return false;
        }
    }

    private String generateTransactionId(String fromUserId, String toUserId) {
        return "TXN_" + fromUserId + "_" + toUserId + "_" + System.currentTimeMillis();
    }

    public boolean hasUsers() {
        return !users.isEmpty();
    }
}
