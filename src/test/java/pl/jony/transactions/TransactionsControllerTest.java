package pl.jony.transactions;

import static org.junit.Assert.assertEquals;
import static pl.jony.transactions.util.TimestampUtil.getCurrentTimestamp;
import static pl.jony.transactions.util.TimestampUtil.getOutdatedTimestamp;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jony.transactions.domain.Statistics;
import pl.jony.transactions.domain.Transaction;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionsControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void Should_ReturnCorrectStatistics() {
    // given
    postTransaction(10, getCurrentTimestamp());
    postTransaction(20, getCurrentTimestamp());
    postTransaction(30, getOutdatedTimestamp());

    // when
    Statistics statistics = restTemplate.getForObject("http://localhost:" + port + "/statistics", Statistics.class);

    // then
    assertEquals("Sum is not correct", 30, statistics.getSum(), 0);
    assertEquals("Average is not correct", 15, statistics.getAvg(), 0);
    assertEquals("Max is not correct", 20, statistics.getMax(), 0);
    assertEquals("Min is not correct", 10, statistics.getMin(), 0);
    assertEquals("Count is not correct", 2, statistics.getCount(), 0);
  }

  @Test
  public void Should_ReturnHttp204_When_OutdatedTimestamp() {
    // given

    // when
    ResponseEntity<Void> responseEntity = postTransaction(15, getOutdatedTimestamp());

    // then
    assertEquals("Expected Http status 204", HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  private ResponseEntity<Void> postTransaction(double amount, long timestamp) {
    Transaction transaction = new Transaction(amount, timestamp);
    Map<String, String> urlVariables = new HashMap<>();
    return restTemplate.postForEntity("http://localhost:" + port + "/transactions", transaction,
        Void.class, urlVariables);
  }
}
