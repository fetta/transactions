package pl.jony.transactions;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.jony.transactions.domain.Transaction;

@ContextConfiguration(classes = TransactionsApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionSerializationTest {
  @Autowired
  private ObjectMapper mapper;

  @Test
  public void Should_SerializeInstant() throws JsonProcessingException {
    // given
    Transaction transaction = new Transaction(12.3, 1478192204000L);

    // when
    String serialized = mapper.writeValueAsString(transaction);

    // then
    String expectedJson =
          "{\n"
        + "  \"amount\" : 12.3,\n"
        + "  \"timestamp\" : 1478192204000\n"
        + "}";
    assertEquals(expectedJson, serialized);
  }

  @Test
  public void Should_DeserializeInstant() throws IOException {
    // given
    String transactionToDeserialize =
          "{\n"
        + "  \"amount\" : 12.3,\n"
        + "  \"timestamp\" : 1478192204000\n"
        + "}";

    // when
    Transaction deserialized = mapper.readValue(transactionToDeserialize, Transaction.class);

    // then
    assertEquals(deserialized.getAmount(), 12.3, 0);
    assertEquals(deserialized.getTimestamp(), 1478192204000L);
  }
}
