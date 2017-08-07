package pl.jony.transactions.logic;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.jony.transactions.domain.Transaction;
import pl.jony.transactions.exceptions.TransactionTooOldException;
import pl.jony.transactions.repository.TransactionRepository;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
  private final TransactionRepository transactionRepository;
  public static ZoneId UTC_ZONE = ZoneId.of("UTC");

  public void saveTransaction(Transaction transaction) throws TransactionTooOldException {
    log.info("saving transaction: {}", transaction);
    ZonedDateTime transactionsTime = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(transaction.getTimestamp()), UTC_ZONE
    );

    if (isTransactionOlderThan60Seconds(transactionsTime)) {
      throw new TransactionTooOldException("Transaction is older than 60 seconds");
    }

    transactionRepository.saveTransaction(transaction);
  }

  private boolean isTransactionOlderThan60Seconds(ZonedDateTime transactionsTime) {
    return transactionsTime.compareTo(ZonedDateTime.now(UTC_ZONE).minus(60, ChronoUnit.SECONDS)) < 0;
  }
}
