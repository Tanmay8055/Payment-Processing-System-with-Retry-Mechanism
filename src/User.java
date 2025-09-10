package src;

import java.util.HashMap;
import java.util.Map;

public class .User {
    private String userId;
    private String name;
    private Map<String, Double> balances;  // Map of currency -> balance

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.balances = new HashMap<>();
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    
    public double getBalance(String currency) {
        return balances.getOrDefault(currency, 0.0);
    }

    public Map<String, Double> getAllBalances() {
        return new HashMap<>(balances);
    }

    public void addBalance(String currency, double amount) {
        balances.put(currency, getBalance(currency) + amount);
    }

    public boolean deductAmount(String currency, double amount) {
        double currentBalance = getBalance(currency);
        if (currentBalance >= amount) {
            balances.put(currency, currentBalance - amount);
            return true;
        }
        return false;
    }

    public boolean hasBalance(String currency, double amount) {
        return getBalance(currency) >= amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (ID: %s)\n", name, userId));
        sb.append("Balances:\n");
        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            sb.append(String.format("  %s: %.2f\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
}
