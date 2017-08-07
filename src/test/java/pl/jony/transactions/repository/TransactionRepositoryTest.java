package pl.jony.transactions.repository;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.jony.transactions.util.TimestampUtil.getCurrentTimestamp;
import static pl.jony.transactions.util.TimestampUtil.getOutdatedTimestamp;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import org.junit.Test;
import pl.jony.transactions.domain.Transaction;

public class TransactionRepositoryTest {
  private TransactionRepository transactionRepository = new TransactionRepository();

  @Test
  public void Should_SaveTransaction() {
    // given
    Transaction transaction = new Transaction(15, getCurrentTimestamp());

    // when
    transactionRepository.saveTransaction(transaction);

    // then
    assertTrue(transactionRepository.getAllTransactions().contains(transaction));
    assertTrue(transactionRepository.getLastTransactions().contains(transaction));
  }

  @Test
  public void Should_GetTransactionsFromLast60Seconds() {
    // given
    Transaction currentTransaction = new Transaction(20, getCurrentTimestamp());
    transactionRepository.saveTransaction(currentTransaction);

    Transaction outdatedTransaction = new Transaction(30, getOutdatedTimestamp());
    transactionRepository.saveTransaction(outdatedTransaction);

    // when
    Collection<Transaction> transactions = transactionRepository.getTransactionsFromLast60Seconds();

    // then
    assertTrue(transactions.contains(currentTransaction));
    assertFalse(transactions.contains(outdatedTransaction));
  }


}
