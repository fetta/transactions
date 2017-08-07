package pl.jony.transactions.logic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Collection;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import pl.jony.transactions.domain.Statistics;
import pl.jony.transactions.domain.Transaction;
import pl.jony.transactions.repository.TransactionRepository;

public class StatisticsServiceTest {
  @Mock
  private TransactionRepository transactionRepository;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void Should_GetCurrentStatistics_When_MoreThanZeroTransactions() {
    // given
    StatisticsService statisticsService = new StatisticsService(transactionRepository);
    Collection<Transaction> transactions = buildTransactions();
    when(transactionRepository.getTransactionsFromLast60Seconds()).thenReturn(transactions);

    // when
    Statistics statistics = statisticsService.getCurrentStatistics();

    // then
    assertEquals(statistics.getSum(), 30, 0);
    assertEquals(statistics.getAvg(), 15, 0);
    assertEquals(statistics.getMax(), 20, 0);
    assertEquals(statistics.getMin(), 10, 0);
    assertEquals(statistics.getCount(), 2, 0);
  }

  @Test
  public void Should_GetZeroStatistics_When_ZeroTransactions() {
    // given
    StatisticsService statisticsService = new StatisticsService(transactionRepository);

    // when
    Statistics statistics = statisticsService.getCurrentStatistics();

    // then
    assertEquals(statistics.getSum(), 0, 0);
    assertEquals(statistics.getAvg(), 0, 0);
    assertEquals(statistics.getMax(), 0, 0);
    assertEquals(statistics.getMin(), 0, 0);
    assertEquals(statistics.getCount(), 0, 0);
  }

  private Collection<Transaction> buildTransactions() {
    Collection<Transaction> transactions = new HashSet<>();
    transactions.add(new Transaction(10, 1478192204000L));
    transactions.add(new Transaction(20, 1478192204000L));
    return transactions;
  }


}
