package src;

import java.util.logging.Logger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_RETRY_DELAY_MS = 1000; // 1 second
    private static final List<String> VALID_CURRENCIES = Arrays.asList("USD", "EUR", "GBP", "INR");
    private static final double MIN_AMOUNT = 100.00;
    private static final double MAX_AMOUNT = 500.00;
    private final Random random = new Random();

    public PaymentResult processPaymentWithRetry(PaymentRequest request) throws PaymentException {
        validateRequest(request);
        
        int retryCount = 0;
        PaymentException lastException = null;

        while (retryCount <= MAX_RETRIES) {
            try {
                if (retryCount > 0) {
                    long delay = calculateExponentialDelay(retryCount);
                    System.out.println("\nPayment failed. Waiting " + (delay/1000) + " seconds before retry...");
                    Thread.sleep(delay);
                    System.out.println("Retrying payment (Attempt " + (retryCount + 1) + " of " + (MAX_RETRIES + 1) + ")...");
                }
                
                return processPayment(request);
            } catch (PaymentException e) {
                lastException = e;
                if (!isRetryableError(e) || retryCount == MAX_RETRIES) {
                    throw new PaymentException("Payment failed after " + retryCount + " retries", e);
                }
                System.out.println("Payment attempt " + (retryCount + 1) + " failed: " + e.getMessage());
                retryCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new PaymentException("Payment retry interrupted", e);
            }
        }
        
        throw new PaymentException("Payment failed after max retries", lastException);
    }

    private void validateRequest(PaymentRequest request) throws PaymentException {
        if (request.getAmount() < MIN_AMOUNT) {
            throw new PaymentException("Amount " + request.getAmount() + " is below minimum allowed amount of " + MIN_AMOUNT);
        }
        if (request.getAmount() > MAX_AMOUNT) {
            throw new PaymentException("Amount " + request.getAmount() + " exceeds maximum allowed amount of " + MAX_AMOUNT);
        }
        if (!VALID_CURRENCIES.contains(request.getCurrency())) {
            throw new PaymentException("Invalid currency: " + request.getCurrency());
        }
    }

    private PaymentResult processPayment(PaymentRequest request) throws PaymentException {
        // Simulate real payment processing with 80% success rate
        if (random.nextDouble() < 0.8) {  // 80% chance of success
            return new PaymentResult(PaymentStatus.SUCCESS, "Payment processed successfully");
        }
        
        // 20% chance of failure with a network error
        throw new PaymentException("Temporary payment processing error: Network timeout");
    }

    private boolean isRetryableError(PaymentException e) {
        // Consider all errors retryable except validation errors
        return !e.getMessage().startsWith("Invalid") && 
               !e.getMessage().contains("below minimum") && 
               !e.getMessage().contains("exceeds maximum");
    }

    private long calculateExponentialDelay(int retryCount) {
        return INITIAL_RETRY_DELAY_MS * (long) Math.pow(2, retryCount - 1);
    }
}
