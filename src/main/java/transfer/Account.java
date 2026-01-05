package transfer;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final Integer id;
    private BigDecimal balance;

    private final ReentrantLock lock = new ReentrantLock();

    public Account(Integer id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public void debit(BigDecimal amount){
        this.balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount){
        this.balance = balance.add(amount);
    }

    public Integer getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
