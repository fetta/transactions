package pl.jony.transactions.exceptions;

public class TransactionTooOldException extends Throwable {

  public TransactionTooOldException(String message) {
    super(message);
  }
}
