package src;

public class PaymentResult {
    private PaymentStatus status;
    private String message;

    public PaymentResult(PaymentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public PaymentStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
