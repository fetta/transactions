package pl.jony.transactions.repository;

import static pl.jony.transactions.logic.TransactionService.UTC_ZONE;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import pl.jony.transactions.domain.Transaction;

/**
 * This repository is written in CQRS-style, and splits write and read logic into two fields.
 * By constantly updating lastTransactions as a cache we make the read time-constant (or near
 * time-constant, assuming that the application's throughput is relatively stable)
 */
@Repository
@Slf4j
public class TransactionRepository {

  @VisibleForTesting
  @Getter(AccessLevel.PACKAGE)
  private final SortedMultiset<Transaction> allTransactions =
      TreeMultiset.create(transactionDateComparator());
  @VisibleForTesting
  @Getter(AccessLevel.PACKAGE)
  private SortedMultiset<Transaction> lastTransactions =
      TreeMultiset.create(transactionDateComparator());

  @Async
  public synchronized void saveTransaction(Transaction transaction) {
    log.info("persisting transaction: {}", transaction);
    allTransactions.add(transaction);
    updateLastTransactions();
    log.info("allTransactions: {}", allTransactions);
    log.info("lastTransactions: {}", lastTransactions);
  }

  public synchronized Collection<Transaction> getTransactionsFromLast60Seconds() {
    return filterTransactionsFromLast60Seconds(lastTransactions);
  }

  private void updateLastTransactions() {
    lastTransactions = filterTransactionsFromLast60Seconds(allTransactions);
  }

  private SortedMultiset<Transaction> filterTransactionsFromLast60Seconds(
      SortedMultiset<Transaction> transactions) {
    ZonedDateTime now = ZonedDateTime.now(UTC_ZONE);

    // amount is ignored in comparator
    Transaction lowerBoundTransaction = new Transaction(
        0, now.minus(60L, ChronoUnit.SECONDS).toInstant().toEpochMilli()
    );

    return transactions.tailMultiset(lowerBoundTransaction, BoundType.OPEN);
  }

  private Comparator<Transaction> transactionDateComparator() {
    return Comparator.comparingLong(Transaction::getTimestamp);
  }
}
