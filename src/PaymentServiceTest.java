package src;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

public class PaymentServiceTest {
    private static final Logger LOGGER = Logger.getLogger(PaymentServiceTest.class.getName());
    private PaymentService paymentService;

    public PaymentServiceTest() {
        // Configure logging to show all levels
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(handler);
        
        this.paymentService = new PaymentService();
    }

    public void runAllTests() {
        testSuccessFirstTry();
        testSuccessAfterRetry();
        testFailureAfterMaxRetries();
        testInvalidAmount();
        testInvalidCurrency();
        testINRPayment();
        testBelowMinimumAmount();
        testAboveMaximumAmount();
    }

    private void testSuccessFirstTry() {
        LOGGER.info("\n=== Testing Success on First Try ===");
        try {
            PaymentRequest request = new PaymentRequest("SUCCESS-1", 200.00, "USD");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.info("Test Success on First Try - Result: " + result.getStatus());
        } catch (PaymentException e) {
            LOGGER.severe("Test Success on First Try - Unexpected failure: " + e.getMessage());
        }
    }

    private void testSuccessAfterRetry() {
        LOGGER.info("\n=== Testing Success After Retry ===");
        try {
            PaymentRequest request = new PaymentRequest("RETRY-1", 300.00, "EUR");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.info("Test Success After Retry - Result: " + result.getStatus());
        } catch (PaymentException e) {
            LOGGER.severe("Test Success After Retry - Unexpected failure: " + e.getMessage());
        }
    }

    private void testFailureAfterMaxRetries() {
        LOGGER.info("\n=== Testing Failure After Max Retries ===");
        try {
            PaymentRequest request = new PaymentRequest("FAIL-1", 400.00, "GBP");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.severe("Test Failure After Max Retries - Unexpected success");
        } catch (PaymentException e) {
            LOGGER.info("Test Failure After Max Retries - Expected failure: " + e.getMessage());
        }
    }

    private void testInvalidAmount() {
        LOGGER.info("\n=== Testing Invalid Amount ===");
        try {
            PaymentRequest request = new PaymentRequest("INVALID-1", -100.00, "USD");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.severe("Test Invalid Amount - Unexpected success");
        } catch (PaymentException e) {
            LOGGER.info("Test Invalid Amount - Expected failure: " + e.getMessage());
        }
    }

    private void testInvalidCurrency() {
        LOGGER.info("\n=== Testing Invalid Currency ===");
        try {
            PaymentRequest request = new PaymentRequest("INVALID-2", 200.00, "INVALID");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.severe("Test Invalid Currency - Unexpected success");
        } catch (PaymentException e) {
            LOGGER.info("Test Invalid Currency - Expected failure: " + e.getMessage());
        }
    }

    private void testINRPayment() {
        LOGGER.info("\n=== Testing INR Payment ===");
        try {
            PaymentRequest request = new PaymentRequest("INR-1", 250.00, "INR");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.info("Test INR Payment - Result: " + result.getStatus() + " - " + result.getMessage());
        } catch (PaymentException e) {
            LOGGER.severe("Test INR Payment - Unexpected failure: " + e.getMessage());
        }
    }

    private void testBelowMinimumAmount() {
        LOGGER.info("\n=== Testing Below Minimum Amount ===");
        try {
            PaymentRequest request = new PaymentRequest("MIN-1", 50.00, "INR");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.severe("Test Below Minimum Amount - Unexpected success");
        } catch (PaymentException e) {
            LOGGER.info("Test Below Minimum Amount - Expected failure: " + e.getMessage());
        }
    }

    private void testAboveMaximumAmount() {
        LOGGER.info("\n=== Testing Above Maximum Amount ===");
        try {
            PaymentRequest request = new PaymentRequest("MAX-1", 600.00, "INR");
            PaymentResult result = paymentService.processPaymentWithRetry(request);
            LOGGER.severe("Test Above Maximum Amount - Unexpected success");
        } catch (PaymentException e) {
            LOGGER.info("Test Above Maximum Amount - Expected failure: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PaymentServiceTest test = new PaymentServiceTest();
        test.runAllTests();
    }
}
