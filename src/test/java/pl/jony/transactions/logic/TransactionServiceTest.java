package pl.jony.transactions.logic;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.jony.transactions.util.TimestampUtil.getCurrentTimestamp;
import static pl.jony.transactions.util.TimestampUtil.getOutdatedTimestamp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.jony.transactions.domain.Transaction;
import pl.jony.transactions.exceptions.TransactionTooOldException;
import pl.jony.transactions.repository.TransactionRepository;

public class TransactionServiceTest {
  @Mock
  private TransactionRepository transactionRepository;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void Should_SaveTransaction_When_CurrentTimestamp() throws TransactionTooOldException {
    // given
    TransactionService transactionService = new TransactionService(transactionRepository);
    Transaction transaction = new Transaction(10, getCurrentTimestamp());

    // when
    transactionService.saveTransaction(transaction);

    // then
    verify(transactionRepository, times(1)).saveTransaction(eq(transaction));
  }

  @Test(expected = TransactionTooOldException.class)
  public void Should_ThrowException_When_OutdatedTimestamp() throws TransactionTooOldException {
    // given
    TransactionService transactionService = new TransactionService(transactionRepository);
    Transaction transaction = new Transaction(10, getOutdatedTimestamp());

    // when
    transactionService.saveTransaction(transaction);
  }
}
