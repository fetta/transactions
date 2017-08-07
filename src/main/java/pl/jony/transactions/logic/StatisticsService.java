package pl.jony.transactions.logic;

import java.util.Collection;
import java.util.stream.DoubleStream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jony.transactions.domain.Statistics;
import pl.jony.transactions.domain.Transaction;
import pl.jony.transactions.repository.TransactionRepository;

@Service
@AllArgsConstructor
public class StatisticsService {
  private final TransactionRepository transactionRepository;

  public Statistics getCurrentStatistics() {
    Collection<Transaction> currentTransactions =
        transactionRepository.getTransactionsFromLast60Seconds();

    return calculateStatistics(currentTransactions);
  }

  private Statistics calculateStatistics(Collection<Transaction> transactions) {
    double sum = calculateSum(transactions);
    double avg = calculateAvg(transactions);
    double max = calculateMax(transactions);
    double min = calculateMin(transactions);
    int count = calculateCount(transactions);
    return new Statistics(sum, avg, max, min, count);
  }

  private double calculateSum(Collection<Transaction> transactions) {
    return getAmountStream(transactions).sum();
  }

  private double calculateAvg(Collection<Transaction> transactions) {
    return getAmountStream(transactions).average().orElse(0);
  }

  private double calculateMax(Collection<Transaction> transactions) {
    return getAmountStream(transactions).max().orElse(0);
  }

  private double calculateMin(Collection<Transaction> transactions) {
    return getAmountStream(transactions).min().orElse(0);
  }

  private int calculateCount(Collection<Transaction> transactions) {
    return transactions.size();
  }

  private DoubleStream getAmountStream(Collection<Transaction> transactions) {
    return transactions.stream().mapToDouble(Transaction::getAmount);
  }
}
