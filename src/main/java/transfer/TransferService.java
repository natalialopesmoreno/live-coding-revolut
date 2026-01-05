package transfer;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class TransferService {

    public void transferMoney(Account from, Account to, BigDecimal amount) throws InterruptedException {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("The amount should be a positive value");
        if(from.getId() == to.getId()) throw new IllegalArgumentException("Origin account and Destination account must be different");

        Account firstAccount = from.getId() < to.getId() ? from : to;
        Account secountAccount = firstAccount == from ? to : from;

              try{

                  if(firstAccount.getLock().tryLock(1000, TimeUnit.MILLISECONDS)){

                      try{
                            if(secountAccount.getLock().tryLock(1000, TimeUnit.MILLISECONDS)){
                                try{

                                    if (from.getBalance().compareTo(amount) < 0) {
                                        throw new IllegalStateException("Saldo insuficiente");
                                    }

                                    from.debit(amount);
                                    to.credit(amount);

                                    System.out.println("Transferência de " + amount +
                                            " realizada de " + from.getId() +
                                            " para " + to.getId());
                                }finally {
                                    secountAccount.getLock().unlock();
                                }

                            }else{
                                throw new IllegalStateException("Internal error. Could not lock the second account");

                            }

                      }finally{
                        secountAccount.getLock().unlock();
                      }
                  }

                  else{
                      throw new IllegalStateException("Internal error. Could not lock the first account");

                  }

              }catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  throw new RuntimeException("Thread interrompida durante a transferência", e);
              }

    }
}
