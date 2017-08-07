package pl.jony.transactions.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jony.transactions.domain.Statistics;
import pl.jony.transactions.domain.Transaction;
import pl.jony.transactions.exceptions.TransactionTooOldException;
import pl.jony.transactions.logic.StatisticsService;
import pl.jony.transactions.logic.TransactionService;
import pl.jony.transactions.repository.TransactionRepository;

@Controller
@AllArgsConstructor
public class TransactionsController {
  private final TransactionService transactionService;
  private final StatisticsService statisticsService;

  @GetMapping("/statistics")
  @ResponseBody
  public Statistics currentStatistics() {
    return statisticsService.getCurrentStatistics();
  }

  @PostMapping("/transactions")
  public ResponseEntity postTransaction(@RequestBody Transaction transaction) {
    try {
      transactionService.saveTransaction(transaction);
      return new ResponseEntity(HttpStatus.CREATED);
    } catch (TransactionTooOldException e) {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
  }
}
